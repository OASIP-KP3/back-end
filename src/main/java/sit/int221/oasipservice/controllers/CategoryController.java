package sit.int221.oasipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.services.CategoryService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryController {
    private final CategoryService service;

    @GetMapping("")
    public List<CategoryDto> getCategories() {
        return service.getCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Integer id) {
        return service.getCategory(id);
    }

    @GetMapping("/{id}/events")
    public List<BookingViewDto> getEventsByCategoryId(@PathVariable Integer id) {
        return service.getEventsByCategoryId(id);
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
