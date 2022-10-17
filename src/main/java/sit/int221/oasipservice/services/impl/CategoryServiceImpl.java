package sit.int221.oasipservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryDescDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryDurationDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryNameDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.entities.EventCategory;
import sit.int221.oasipservice.repositories.BookingRepository;
import sit.int221.oasipservice.repositories.CategoryRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryServiceImpl {
    private final BookingRepository bookingRepo;
    private final CategoryRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    public List<CategoryDto> getCategories() {
        log.info("Fetching all categories...");
        List<EventCategory> categories = repo.findAll();
        return listMapper.mapList(categories, CategoryDto.class, modelMapper);
    }

    public CategoryDto getCategory(Integer id) throws ResourceNotFoundException {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(category, CategoryDto.class);
    }

    public List<BookingViewDto> getEventsBy(Integer id, String date, String type) throws ResourceNotFoundException {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        if (date != null && type == null) {
            List<EventBooking> bookings = bookingRepo.findAllByDateAndCategory(date, id);
            return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
        } else if (date == null && type != null) {
            switch (type) {
                case "future" -> {
                    List<EventBooking> futureEvents = bookingRepo.getFutureEventsByDateAndCategory(id);
                    return listMapper.mapList(futureEvents, BookingViewDto.class, modelMapper);
                }
                case "past" -> {
                    List<EventBooking> pastEvents = bookingRepo.getPastEventsByDateAndCategory(id);
                    return listMapper.mapList(pastEvents, BookingViewDto.class, modelMapper);
                }
                case "all" -> {
                    category.getEventBookings().sort(Comparator.comparing(EventBooking::getEventStartTime).reversed());
                    return listMapper.mapList(category.getEventBookings(), BookingViewDto.class, modelMapper);
                }
                default -> throw new ResourceNotFoundException("Type " + type + " is not supported");
            }
        }
        category.getEventBookings().sort(Comparator.comparing(EventBooking::getEventStartTime).reversed());
        return listMapper.mapList(category.getEventBookings(), BookingViewDto.class, modelMapper);
    }

    public void save(CategoryDto newCategory) throws ResponseStatusException {
        if (repo.existsById(newCategory.getId()))
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, newCategory.getId() + " is not unique");
        if (isUnique(newCategory.getCategoryName()))
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, newCategory.getCategoryName() + " is not unique");
        repo.saveAndFlush(modelMapper.map(newCategory, EventCategory.class));
    }

    private boolean isUnique(String categoryName) {
        return repo.getAllCategoryName().contains(categoryName);
    }

    public void delete(Integer id) throws ResourceNotFoundException {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("ID " + id + " is not found");
        repo.deleteById(id);
    }

    public void update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, ResponseStatusException {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
    }

    public CategoryNameDto updateCategoryName(Integer id, CategoryNameDto categoryName) throws ResourceNotFoundException, ResponseStatusException {
        EventCategory updatedCategory = repo.findById(id).map((oldCategory) -> {
            if (isUnique(categoryName.getCategoryName()))
                throw new ResponseStatusException(UNPROCESSABLE_ENTITY, categoryName.getCategoryName() + " is not unique");
            oldCategory.setCategoryName(categoryName.getCategoryName());
            return oldCategory;
        }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(updatedCategory), CategoryNameDto.class);
    }

    public CategoryDescDto updateCategoryDesc(Integer id, CategoryDescDto description) throws ResourceNotFoundException {
        EventCategory updatedCategory = repo.findById(id).map((oldCategory) -> {
            oldCategory.setCategoryDescription(description.getCategoryDescription());
            return oldCategory;
        }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(updatedCategory), CategoryDescDto.class);
    }

    public CategoryDurationDto updateDuration(Integer id, CategoryDurationDto duration) throws ResourceNotFoundException {
        EventCategory updatedCategory = repo.findById(id).map((oldCategory) -> {
            oldCategory.setEventDuration(duration.getEventDuration());
            return oldCategory;
        }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(updatedCategory), CategoryDurationDto.class);
    }
}
