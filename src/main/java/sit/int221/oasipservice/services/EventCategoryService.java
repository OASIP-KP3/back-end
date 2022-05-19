package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.entities.EventCategory;
import sit.int221.oasipservice.repo.EventCategoryRepository;
import sit.int221.oasipservice.utils.ListMapper;

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

    public List<EventListAllDto> getEventsByCategoryId(Integer id) {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return listMapper.mapList(category.getEventBookings(), EventListAllDto.class, modelMapper);
    }
}
