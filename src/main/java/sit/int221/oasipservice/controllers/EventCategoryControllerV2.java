package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryDescDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryDurationDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryNameDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.services.EventCategoryService;
import sit.int221.oasipservice.services.EventCategoryServiceV2;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2/categories")
public class EventCategoryControllerV2 {
    private final EventCategoryServiceV2 service;

    @Autowired
    public EventCategoryControllerV2(EventCategoryServiceV2 service) {
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

    @GetMapping("/{id}/events")
    public List<EventListAllDto> getEventsByCategoryId(
            @PathVariable Integer id,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "type", required = false) String type) {
        return service.getEventsBy(id, date, type);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@Valid @RequestBody CategoryDto newCategory) {
        service.save(newCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/names")
    public CategoryNameDto updateCategoryName(@PathVariable Integer id, @Valid @RequestBody CategoryNameDto categoryName) {
        return service.updateCategoryName(id, categoryName);
    }

    @PatchMapping("/{id}/descriptions")
    public CategoryDescDto updateCategoryDesc(@PathVariable Integer id, @Valid @RequestBody CategoryDescDto description) {
        return service.updateCategoryDesc(id, description);
    }

    @PatchMapping("/{id}/durations")
    public CategoryDurationDto updateDuration(@PathVariable Integer id, @Valid @RequestBody CategoryDurationDto duration) {
        return service.updateDuration(id, duration);
    }
}
