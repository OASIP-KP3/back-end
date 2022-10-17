package sit.int221.oasipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryDescDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryDurationDto;
import sit.int221.oasipservice.dto.categories.fields.CategoryNameDto;
import sit.int221.oasipservice.services.impl.CategoryServiceImpl;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryControllerV2 {
    private final CategoryServiceImpl service;

    @GetMapping("")
    public List<CategoryDto> getCategories() {
        return service.getCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Integer id) {
        return service.getCategory(id);
    }

    @GetMapping("/{id}/events")
    public List<BookingViewDto> getEventsByCategoryId(
            @PathVariable Integer id,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "type", required = false) String type) {
        return service.getEventsBy(id, date, type);
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
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
