package com.thiago.planner.link;

import com.thiago.planner.link.dto.CreateLinkDTO;
import com.thiago.planner.link.dto.ListLinkResponseDTO;
import com.thiago.planner.trip.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkService linkService;

    @Test
    void createLink() {
        Trip trip = new Trip();

        CreateLinkDTO createLinkDTO = new CreateLinkDTO("Google", "https://google.com");

        Link link = new Link();

        when(this.linkRepository.save(any(Link.class))).thenReturn(link);

        Link result = this.linkService.createLink(createLinkDTO, trip);

        assertNotNull(result);
        assertEquals(result, link);
        verify(this.linkRepository, times(1)).save(any(Link.class));
    }

    @Test
    void getAllLinksFromTrip() {
        List<Link> links = List.of(
                new Link(),
                new Link()
        );

        UUID tripId = UUID.randomUUID();

        when(this.linkRepository.findByTripId(tripId)).thenReturn(links);

        List<ListLinkResponseDTO> result = this.linkService.getAllLinksFromTrip(tripId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(this.linkRepository, times(1)).findByTripId(tripId);
    }
}