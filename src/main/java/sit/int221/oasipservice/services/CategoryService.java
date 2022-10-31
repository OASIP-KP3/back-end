package sit.int221.oasipservice.services;

import org.jetbrains.annotations.NotNull;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.categories.CategoryDto;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<CategoryDto> getCategories();

    CategoryDto getCategory(Integer id);

    List<BookingViewDto> getEventsByCategoryId(Integer id);

    void save(@NotNull CategoryDto newCategory);

    void delete(Integer id);

    CategoryDto update(Integer id, @NotNull Map<String, Object> changes);
}
