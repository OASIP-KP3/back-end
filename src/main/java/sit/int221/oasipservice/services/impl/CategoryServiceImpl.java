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
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.repositories.CategoryRepository;
import sit.int221.oasipservice.repositories.UserRepository;
import sit.int221.oasipservice.services.CategoryService;
import sit.int221.oasipservice.utils.JwtUtils;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static sit.int221.oasipservice.entities.ERole.ROLE_LECTURER;
import static sit.int221.oasipservice.entities.ERole.ROLE_STUDENT;

@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;
    private final JwtUtils jwtUtils;

    @Override
    public List<CategoryDto> getCategories() {
        log.info("Fetching all categories...");
        List<EventCategory> categories = repo.findAll();
        return listMapper.mapList(categories, CategoryDto.class, modelMapper);
    }

    @Override
    public CategoryDto getCategory(Integer id) {
        log.info("Fetching category id: " + id);
        return repo.findById(id)
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("category id " + id + " is not found"));
    }

    @Override
    public List<BookingViewDto> getEventsByCategoryId(Integer id) {
        List<EventBooking> bookings = repo.findById(id)
                .map(EventCategory::getEventBookings)
                .orElseThrow(() -> new ResourceNotFoundException("category id " + id + " is not found"));
        if (jwtUtils.getRoles().contains(ROLE_STUDENT.getRole())) {
            List<EventBooking> studentBookings = bookings
                    .stream()
                    .filter(b -> b.getBookingEmail().equals(jwtUtils.getEmail()))
                    .toList();
            return listMapper.mapList(studentBookings, BookingViewDto.class, modelMapper);
        }
        if (jwtUtils.getRoles().contains(ROLE_LECTURER.getRole())) {
            User lecturer = userRepo.findByUserEmail(jwtUtils.getEmail());
            Set<EventCategory> categories = lecturer.getOwnCategories();
            if (categories.stream().noneMatch(c -> c.getId().equals(id))) {
                log.info("[" + ROLE_LECTURER.getRole() + "]" + " does not own this category id: " + id);
                return new ArrayList<>();
            }
            return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
        }
        log.info("Fetching all bookings from category id: " + id);
        return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
    }

    @Override
    public CategoryDto save(@NotNull CategoryDto newCategory) {
        log.info("Saving a new category...");
        EventCategory category = modelMapper.map(newCategory, EventCategory.class);
        repo.saveAndFlush(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting category id: " + id);
        if (!repo.existsById(id)) throw new ResourceNotFoundException("category id " + id + " is not found");
        repo.deleteById(id);
    }

    @Override
    public CategoryDto update(Integer id, @NotNull Map<String, Object> changes) {
        return repo.findById(id)
                .map(category -> mapCategory(category, changes))
                .map(repo::saveAndFlush)
                .map(updatedCategory -> modelMapper.map(updatedCategory, CategoryDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("category id " + id + " is not found"));
    }

    private EventCategory mapCategory(EventCategory category, Map<String, Object> changes) {
        Integer id = category.getId();
        changes.forEach((field, value) -> {
            switch (field) {
                case "categoryName" -> {
                    String categoryName = (String) value;
                    if (categoryName == null || categoryName.isBlank())
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    if (categoryName.length() > 100 || categoryName.length() < 1)
                        throw new IllegalArgumentException("size must be between 1 and 100");
                    if (repo.findByCategoryName(categoryName) != null)
                        throw new IllegalArgumentException(categoryName + " is not unique");
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
