package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.events.EventBookingDto;
import sit.int221.oasipservice.dto.events.EventDetailsBaseDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.repo.EventBookingRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.List;

@Service
public class EventBookingService {
    private final EventBookingRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Autowired
    public EventBookingService(EventBookingRepository repo, ModelMapper modelMapper, ListMapper listMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
    }

    public List<EventListAllDto> getEventListSorted() {
        Sort sort = Sort.by("eventStartTime");
        List<EventBooking> bookings = repo.findAll(sort.descending());
        return listMapper.mapList(bookings, EventListAllDto.class, modelMapper);
    }

    public EventDetailsBaseDto getEventDetails(Integer id) {
        EventBooking booking = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(booking, EventDetailsBaseDto.class);
    }

    public void save(EventBookingDto newBooking) {
        EventBooking booking = modelMapper.map(newBooking, EventBooking.class);
        repo.saveAndFlush(booking);
    }

    public void delete(Integer id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist");
        }
    }
}
