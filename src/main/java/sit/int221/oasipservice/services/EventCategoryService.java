package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.entities.EventCategory;
import sit.int221.oasipservice.repo.EventCategoryRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.Comparator;
import java.util.List;

@Service
public class EventCategoryService {
    private final EventCategoryRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Autowired
    public EventCategoryService(EventCategoryRepository repo, ModelMapper modelMapper, ListMapper listMapper) {
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

    public List<EventListAllDto> getEventsByCategoryId(Integer id) throws ResourceNotFoundException {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        category.getEventBookings().sort(Comparator.comparing(EventBooking::getEventStartTime).reversed());
        return listMapper.mapList(category.getEventBookings(), EventListAllDto.class, modelMapper);
    }

    public void save(CategoryDto newCategory) throws ResponseStatusException {
        if (isUnique(newCategory.getCategoryName())) {
            EventCategory category = modelMapper.map(newCategory, EventCategory.class);
            repo.saveAndFlush(category);
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, newCategory.getCategoryName() + " is not unique");
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
}
