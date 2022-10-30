package sit.int221.oasipservice.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.exceptions.UnprocessableException;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<CategoryDto> getCategories();

    CategoryDto getCategory(Integer id) throws ResourceNotFoundException;

    List<BookingViewDto> getEventsByCategoryId(Integer id) throws ResourceNotFoundException;

    void save(@NotNull CategoryDto newCategory) throws UnprocessableException;

    void delete(Integer id) throws ResourceNotFoundException;

    CategoryDto update(Integer id, @NotNull Map<String, Object> changes) throws ResourceNotFoundException;
}
