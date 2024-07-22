package com.thiago.planner.link;

import com.thiago.planner.link.dto.CreateLinkDTO;
import com.thiago.planner.link.dto.ListLinkResponseDTO;
import com.thiago.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Link createLink(CreateLinkDTO createLinkDTO, Trip trip) {
        Link link = new Link(
                createLinkDTO.title(),
                createLinkDTO.url(),
                trip
        );

        return this.linkRepository.save(link);
    }

    public List<ListLinkResponseDTO> getAllLinksFromTrip(UUID tripId) {
        return this.linkRepository.findByTripId(tripId).stream().map(
                link -> new ListLinkResponseDTO(link.getId(), link.getTitle(), link.getUrl())
        ).toList();
    }
}
