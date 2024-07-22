package com.thiago.planner.trip;

import com.thiago.planner.activity.Activity;
import com.thiago.planner.activity.ActivityRepository;
import com.thiago.planner.activity.dto.CreateActivityDTO;
import com.thiago.planner.link.Link;
import com.thiago.planner.link.LinkRepository;
import com.thiago.planner.link.dto.CreateLinkDTO;
import com.thiago.planner.participant.Participant;
import com.thiago.planner.participant.ParticipantRepository;
import com.thiago.planner.participant.ParticipantService;
import com.thiago.planner.participant.dto.InviteParticipantsDTO;
import com.thiago.planner.trip.dto.CreateTripDTO;
import com.thiago.planner.trip.dto.UpdateTripDTO;
import com.thiago.planner.utils.TestUtils;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TripControllerTest {

    private MockMvc mvc;
    private final WebApplicationContext context;
    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;
    private final ActivityRepository activityRepository;
    private final LinkRepository linkRepository;

    @Autowired
    public TripControllerTest(
            TripService tripService,
            ParticipantService participantService,
            WebApplicationContext context,
            TripRepository tripRepository,
            ParticipantRepository participantRepository,
            ActivityRepository activityRepository,
            LinkRepository linkRepository
    ) {
        this.context = context;
        this.tripRepository = tripRepository;
        this.participantRepository = participantRepository;
        this.activityRepository = activityRepository;
        this.linkRepository = linkRepository;
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void create() throws Exception {
        CreateTripDTO createTripDTO = new CreateTripDTO(
          "TEST user",
          "test@email.com",
                List.of("test1@email.com", "test2@email.com"),
                "Cristo Redentor",
                true,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(5).toString()
        );

        mvc.perform(
                MockMvcRequestBuilders.post("/trip")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(createTripDTO))
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void update() throws Exception {
        CreateTripDTO createTripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("test2@email.com", "test2@email.com"),
                "Cristo Redentor",
                true,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(4).toString()
        );
        Trip trip = new Trip(createTripDTO);

        Trip tripOnDatabase = this.tripRepository.save(trip);

        UpdateTripDTO updateTripDTO = new UpdateTripDTO(
                "New York, USA",
                LocalDateTime.now().plusDays(3).toString(),
                LocalDateTime.now().plusDays(5).toString()
        );

        mvc.perform(
                MockMvcRequestBuilders.put("/trip/" + tripOnDatabase.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(updateTripDTO))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllParticipants() throws Exception {
        CreateTripDTO createTripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("test2@email.com", "test2@email.com"),
                "Cristo Redentor",
                true,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(4).toString()
        );
        Trip trip = new Trip(createTripDTO);

        Trip tripCreated = this.tripRepository.save(trip);

        List<Participant> participants = List.of(
          new Participant("test1@email.com", tripCreated),
          new Participant("test2@email.com", tripCreated)
        );

        this.participantRepository.saveAll(participants);

        mvc.perform(
                MockMvcRequestBuilders.get("/trip/" + tripCreated.getId() + "/participants")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void inviteParticipants() throws Exception {
        CreateTripDTO createTripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("test2@email.com", "test2@email.com"),
                "Cristo Redentor",
                true,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(4).toString()
        );
        Trip trip = new Trip(createTripDTO);

        Trip tripCreated = this.tripRepository.save(trip);

        InviteParticipantsDTO inviteParticipantsDTO = new InviteParticipantsDTO(List.of("Test@email.com", "test2@email.com"));

        mvc.perform(
                MockMvcRequestBuilders.post("/trip/" + tripCreated.getId() + "/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(inviteParticipantsDTO))
            ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createActivity() throws Exception {
        CreateTripDTO createTripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("test2@email.com", "test2@email.com"),
                "Cristo Redentor",
                true,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(4).toString()
        );
        Trip trip = new Trip(createTripDTO);

        Trip tripCreated = this.tripRepository.save(trip);

        CreateActivityDTO createActivityDTO = new CreateActivityDTO("Dinner", LocalDateTime.now().plusDays(3).toString());

        mvc.perform(
                MockMvcRequestBuilders.post("/trip/" + tripCreated.getId() + "/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(createActivityDTO))
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void listActivities() throws Exception {
        CreateTripDTO createTripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("test2@email.com", "test2@email.com"),
                "Cristo Redentor",
                true,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(4).toString()
        );
        Trip trip = new Trip(createTripDTO);

        Trip tripCreated = this.tripRepository.save(trip);

        List<Activity> activities = List.of(
                new Activity("Dinner", LocalDateTime.now().plusDays(3), tripCreated),
                new Activity("lunch", LocalDateTime.now().plusDays(3).plusHours(2), tripCreated)
        );

        this.activityRepository.saveAll(activities);

        mvc.perform(
                        MockMvcRequestBuilders.get("/trip/" + tripCreated.getId() + "/activity")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createLink() throws Exception {
        CreateTripDTO createTripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("test2@email.com", "test2@email.com"),
                "Cristo Redentor",
                true,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(4).toString()
        );
        Trip trip = new Trip(createTripDTO);

        Trip tripCreated = this.tripRepository.save(trip);

        CreateLinkDTO createLinkDTO = new CreateLinkDTO("google", "https://google.com");

        mvc.perform(
                MockMvcRequestBuilders.post("/trip/" + tripCreated.getId() + "/link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(createLinkDTO))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    void listLinks() throws Exception {
        CreateTripDTO createTripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("test2@email.com", "test2@email.com"),
                "Cristo Redentor",
                true,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(4).toString()
        );
        Trip trip = new Trip(createTripDTO);

        Trip tripCreated = this.tripRepository.save(trip);

        List<Link> links = List.of(
                new Link("google", "https://google.com", trip),
                new Link("Wikipedia", "https://wikipedia.com", trip)
        );

        this.linkRepository.saveAll(links);

        mvc.perform(
                        MockMvcRequestBuilders.get("/trip/" + tripCreated.getId() + "/link")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}