package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryDescDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryDurationDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryNameDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.entities.EventCategory;
import sit.int221.oasipservice.repo.EventBookingRepository;
import sit.int221.oasipservice.repo.EventCategoryRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class EventCategoryServiceV2 {
    private final EventBookingRepository bookingRepo;
    private final EventCategoryRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Autowired
    public EventCategoryServiceV2(EventBookingRepository bookingRepo, EventCategoryRepository repo, ModelMapper modelMapper, ListMapper listMapper) {
        this.bookingRepo = bookingRepo;
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
    }

    public List<CategoryDto> getCategories() {
        return listMapper.mapList(repo.findAll(), CategoryDto.class, modelMapper);
    }

    public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(category, CategoryDto.class);
    }

    public List<EventListAllDto> getEventsBy(Integer id, String date, String type) throws ResourceNotFoundException {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        if (date != null && type == null) {
            List<EventBooking> bookings = bookingRepo.findAllByDateAndCategory(date, id);
            return listMapper.mapList(bookings, EventListAllDto.class, modelMapper);
        } else if (date == null && type != null) {
            switch (type) {
                case "future" -> {
                    List<EventBooking> futureEvents = bookingRepo.getFutureEventsByDateAndCategory(id);
                    return listMapper.mapList(futureEvents, EventListAllDto.class, modelMapper);
                }
                case "past" -> {
                    List<EventBooking> pastEvents = bookingRepo.getPastEventsByDateAndCategory(id);
                    return listMapper.mapList(pastEvents, EventListAllDto.class, modelMapper);
                }
                case "all" -> {
                    category.getEventBookings().sort(Comparator.comparing(EventBooking::getEventStartTime).reversed());
                    return listMapper.mapList(category.getEventBookings(), EventListAllDto.class, modelMapper);
                }
                default -> throw new ResourceNotFoundException("Type " + type + " is not supported");
            }
        }
        category.getEventBookings().sort(Comparator.comparing(EventBooking::getEventStartTime).reversed());
        return listMapper.mapList(category.getEventBookings(), EventListAllDto.class, modelMapper);
    }

    public void save(CategoryDto newCategory) throws ResponseStatusException {
        if (!(repo.existsById(newCategory.getId()))) {
            if (isUnique(newCategory.getCategoryName())) {
                EventCategory category = modelMapper.map(newCategory, EventCategory.class);
                repo.saveAndFlush(category);
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, newCategory.getCategoryName() + " is not unique");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, newCategory.getId() + " is not unique");
        }
    }

    private boolean isUnique(String categoryName) {
        return !(repo.getAllCategoryName().contains(categoryName));
    }

    public void delete(Integer id) throws ResourceNotFoundException {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("ID " + id + " is not found");
        }
        repo.deleteById(id);
    }

    public void update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, ResponseStatusException {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
    }

    public CategoryNameDto updateCategoryName(Integer id, CategoryNameDto categoryName) throws ResourceNotFoundException, ResponseStatusException {
        EventCategory newCategory = modelMapper.map(categoryName, EventCategory.class);
        EventCategory updatedCategory = repo.findById(id).map((oldCategory) -> {
            if (isUnique(newCategory.getCategoryName())) {
                oldCategory.setCategoryName(newCategory.getCategoryName());
                return oldCategory;
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, newCategory.getCategoryName() + " is not unique");
            }
        }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(updatedCategory), CategoryNameDto.class);
    }

    public CategoryDescDto updateCategoryDesc(Integer id, CategoryDescDto description) throws ResourceNotFoundException {
        EventCategory newCategory = modelMapper.map(description, EventCategory.class);
        EventCategory updatedCategory = repo.findById(id).map((oldCategory) -> {
            oldCategory.setCategoryDescription(newCategory.getCategoryDescription());
            return oldCategory;
        }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(updatedCategory), CategoryDescDto.class);
    }

    public CategoryDurationDto updateDuration(Integer id, CategoryDurationDto duration) throws ResourceNotFoundException {
        EventCategory newCategory = modelMapper.map(duration, EventCategory.class);
        EventCategory updatedCategory = repo.findById(id).map((oldCategory) -> {
            oldCategory.setEventDuration(newCategory.getEventDuration());
            return oldCategory;
        }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(updatedCategory), CategoryDurationDto.class);
    }
}