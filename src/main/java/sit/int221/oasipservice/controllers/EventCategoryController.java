package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.services.EventCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class EventCategoryController {
    private final EventCategoryService service;

    @Autowired
    public EventCategoryController(EventCategoryService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<CategoryDto> getCategories() {
        return service.getCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Integer id) {
        return service.getCategoryById(id);
    }
}
