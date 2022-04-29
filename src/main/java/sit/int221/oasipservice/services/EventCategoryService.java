package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.api.CategoryDto;
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

    public CategoryDto getCategoryById(Integer id) {
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(category, CategoryDto.class);
    }
}
