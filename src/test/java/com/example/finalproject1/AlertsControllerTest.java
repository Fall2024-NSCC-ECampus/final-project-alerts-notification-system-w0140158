package com.example.finalproject1;

import com.example.finalproject1.controller.AlertsController;
import com.example.finalproject1.model.Person;
import com.example.finalproject1.service.AlertsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertsController.class)
class AlertsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertsService alertsService;

    @Autowired
    private ObjectMapper objectMapper;

    // Endpoint Constants
    private static final String FIRESTATION_ENDPOINT = "/firestation";
    private static final String CHILD_ALERT_ENDPOINT = "/childAlert";
    private static final String PHONE_ALERT_ENDPOINT = "/phoneAlert";
    private static final String FIRE_ENDPOINT = "/fire";
    private static final String FLOOD_STATIONS_ENDPOINT = "/flood/stations";
    private static final String PERSON_INFO_ENDPOINT = "/personInfo";
    private static final String COMMUNITY_EMAIL_ENDPOINT = "/communityEmail";

    private Person homer;
    private Person bart;

    @BeforeEach
    void setUp() {
        homer = new Person("Homer", "Simpson", "742 Evergreen Terrace", "Springfield",
                "555-1234", "homer.simpson@example.com", 42,
                List.of("aspirin:100mg"), List.of("none"));
        bart = new Person("Bart", "Simpson", "742 Evergreen Terrace", "Springfield",
                "555-1236", "bart.simpson@example.com", 12,
                Collections.emptyList(), Collections.emptyList());
    }

    @Test
    void testGetPeopleByFireStation_Found() throws Exception {
        int stationNumber = 1;
        List<Person> people = List.of(homer);
        long numberOfAdults = 1;
        long numberOfChildren = 0;

        when(alertsService.getPeopleByFireStation(stationNumber)).thenReturn(people);

        mockMvc.perform(get(FIRESTATION_ENDPOINT)
                        .param("stationNumber", String.valueOf(stationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons[0].firstName").value("Homer"))
                .andExpect(jsonPath("$.numberOfAdults").value(numberOfAdults))
                .andExpect(jsonPath("$.numberOfChildren").value(numberOfChildren));
    }

    @Test
    void testGetPeopleByFireStation_NotFound() throws Exception {
        int stationNumber = 99;
        List<Person> people = Collections.emptyList();

        when(alertsService.getPeopleByFireStation(stationNumber)).thenReturn(people);

        mockMvc.perform(get(FIRESTATION_ENDPOINT)
                        .param("stationNumber", String.valueOf(stationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty());
    }

    @Test
    void testGetPeopleByFireStation_MissingParam() throws Exception {
        mockMvc.perform(get(FIRESTATION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required parameter: stationNumber"));
    }

    @Test
    void testGetChildAlert_Found() throws Exception {
        String address = "742 Evergreen Terrace";
        Map<String, Object> response = new HashMap<>();
        response.put("children", List.of(bart));
        response.put("otherResidents", List.of(homer));

        when(alertsService.getChildrenByAddress(address)).thenReturn(response);

        mockMvc.perform(get(CHILD_ALERT_ENDPOINT)
                        .param("address", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children[0].firstName").value("Bart"))
                .andExpect(jsonPath("$.otherResidents[0].firstName").value("Homer"));
    }

    @Test
    void testGetChildAlert_NotFound() throws Exception {
        String address = "123 Unknown Street";
        Map<String, Object> response = Collections.emptyMap();

        when(alertsService.getChildrenByAddress(address)).thenReturn(response);

        mockMvc.perform(get(CHILD_ALERT_ENDPOINT)
                        .param("address", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").doesNotExist())
                .andExpect(jsonPath("$.otherResidents").doesNotExist());
    }

    @Test
    void testGetPhoneAlert_Found() throws Exception {
        int stationNumber = 1;
        List<String> phoneNumbers = List.of("555-1234", "555-5678");

        when(alertsService.getPhoneNumbersByFireStation(stationNumber)).thenReturn(phoneNumbers);

        mockMvc.perform(get(PHONE_ALERT_ENDPOINT)
                        .param("stationNumber", String.valueOf(stationNumber)) // Corrected parameter
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumbers[0]").value("555-1234"))
                .andExpect(jsonPath("$.phoneNumbers[1]").value("555-5678"));
    }


    @Test
    void testGetPhoneAlert_NotFound() throws Exception {
        int stationNumber = 99;
        List<String> phoneNumbers = Collections.emptyList();

        when(alertsService.getPhoneNumbersByFireStation(stationNumber)).thenReturn(phoneNumbers);

        mockMvc.perform(get(PHONE_ALERT_ENDPOINT)
                        .param("stationNumber", String.valueOf(stationNumber)) // Corrected parameter
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumbers").isArray()) // Ensure key exists
                .andExpect(jsonPath("$.phoneNumbers").isEmpty());
    }


    @Test
    void testGetFloodDetailsByStations_Found() throws Exception {
        String stationsParam = "1,2";
        List<Integer> stationNumbers = List.of(1, 2);
        String address1 = "742 Evergreen Terrace";
        String address2 = "300 School Lane";

        Person nelson = new Person("Nelson", "Muntz", address2, "Springfield",
                "555-8903", "nelson.muntz@example.com", 14,
                Collections.emptyList(), List.of("peanut"));

        Map<String, List<Person>> households = new HashMap<>();
        households.put(address1, List.of(homer, bart));
        households.put(address2, List.of(nelson));

        when(alertsService.getFloodDetailsByStations(stationNumbers)).thenReturn(households);

        mockMvc.perform(get(FLOOD_STATIONS_ENDPOINT)
                        .param("stations", stationsParam)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.households['742 Evergreen Terrace'][0].firstName").value("Homer"))
                .andExpect(jsonPath("$.households['742 Evergreen Terrace'][1].firstName").value("Bart"))
                .andExpect(jsonPath("$.households['300 School Lane'][0].firstName").value("Nelson"));
    }

    @Test
    void testGetFloodDetailsByStations_NotFound() throws Exception {
        String stationsParam = "99";
        List<Integer> stationNumbers = List.of(99);
        Map<String, List<Person>> households = Collections.emptyMap();

        when(alertsService.getFloodDetailsByStations(stationNumbers)).thenReturn(households);

        mockMvc.perform(get(FLOOD_STATIONS_ENDPOINT)
                        .param("stations", stationsParam)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.households").doesNotExist());
    }
}
