package sit.int221.oasipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.services.impl.CategoryServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @PatchMapping("/{id}")
    public CategoryDto updateCategoryName(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return service.update(id, body);
    }
}
