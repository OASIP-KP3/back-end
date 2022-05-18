package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.services.EventCategoryService;

import java.util.List;

@CrossOrigin(origins = {"http://ip21kp3.sit.kmutt.ac.th", "http://localhost:3000", "http://intproj21.sit.kmutt.ac.th"})
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
