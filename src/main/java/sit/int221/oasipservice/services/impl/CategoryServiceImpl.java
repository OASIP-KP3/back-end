package sit.int221.oasipservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.entities.EventCategory;
import sit.int221.oasipservice.exceptions.UnprocessableException;
import sit.int221.oasipservice.repositories.CategoryRepository;
import sit.int221.oasipservice.services.CategoryService;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Override
    public List<CategoryDto> getCategories() {
        log.info("Fetching all categories...");
        List<EventCategory> categories = repo.findAll();
        return listMapper.mapList(categories, CategoryDto.class, modelMapper);
    }

    @Override
    public CategoryDto getCategory(Integer id) throws ResourceNotFoundException {
        log.info("Fetching category id: " + id);
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<BookingViewDto> getEventsByCategoryId(Integer id) throws ResourceNotFoundException {
        log.info("Fetching all bookings from category id: " + id);
        EventCategory category = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        List<EventBooking> bookings = category.getEventBookings();
        return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
    }

    @Override
    public void save(@NotNull CategoryDto newCategory) throws UnprocessableException {
        log.info("Saving a new category...");
        if (repo.existsById(newCategory.getId()))
            throw new UnprocessableException(newCategory.getId() + " is not unique");
        if (isUnique(newCategory.getCategoryName()))
            throw new UnprocessableException(newCategory.getCategoryName() + " is not unique");
        repo.saveAndFlush(modelMapper.map(newCategory, EventCategory.class));
    }

    private boolean isUnique(String categoryName) {
        return repo.getAllCategoryName().contains(categoryName);
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException {
        log.info("Deleting category id: " + id);
        if (!repo.existsById(id)) throw new ResourceNotFoundException("ID " + id + " is not found");
        repo.deleteById(id);
    }

    @Override
    public CategoryDto update(Integer id, @NotNull Map<String, Object> changes) throws ResourceNotFoundException {
        EventCategory category = repo.findById(id)
                .map(c -> mapCategory(c, changes))
                .orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(category), CategoryDto.class);
    }

    private EventCategory mapCategory(EventCategory category, Map<String, Object> changes) throws UnprocessableException, IllegalArgumentException {
        Integer id = category.getId();
        changes.forEach((field, value) -> {
            switch (field) {
                case "categoryName" -> {
                    String categoryName = (String) value;
                    if (categoryName == null || categoryName.isBlank())
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    if (categoryName.length() > 100 || categoryName.length() < 1)
                        throw new IllegalArgumentException("size must be between 1 and 100");
                    if (isUnique(categoryName))
                        throw new UnprocessableException(categoryName + " is not unique");
                    log.info("Updating category name of id: " + id);
                    category.setCategoryName(categoryName.trim());
                }
                case "categoryDescription" -> {
                    String description = (String) value;
                    if (description != null && description.length() > 500)
                        throw new IllegalArgumentException("size must be between 0 and 500");
                    log.info("Updating category description of id: " + id);
                    category.setCategoryDescription(description == null ? null : description.trim());
                }
                case "eventDuration" -> {
                    if (value == null)
                        throw new IllegalArgumentException(field + " is must not be null");
                    Integer duration = (Integer) value;
                    if (duration < 1 || duration > 480)
                        throw new IllegalArgumentException("duration must be between 1 and 480");
                    log.info("Updating category duration of id: " + id);
                    category.setEventDuration(duration);
                }
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });
        return category;
    }
}
