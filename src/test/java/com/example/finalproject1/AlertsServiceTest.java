package com.example.finalproject1;

import com.example.finalproject1.model.FireStation;
import com.example.finalproject1.model.Person;
import com.example.finalproject1.repository.FireStationRepository;
import com.example.finalproject1.repository.PersonRepository;
import com.example.finalproject1.service.AlertsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AlertsService.
 */
@SpringBootTest
class AlertsServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FireStationRepository fireStationRepository;

    @InjectMocks
    private AlertsService alertsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test getPeopleByFireStation with existing station number.
     */
    @Test
    void testGetPeopleByFireStation_Found() {
        int stationNumber = 1;
        FireStation fs = new FireStation();
        fs.setAddress("742 Evergreen Terrace");
        fs.setStationNumber(stationNumber);

        when(fireStationRepository.findByStationNumber(stationNumber)).thenReturn(List.of(fs));

        Person person = new Person("Homer", "Simpson", "742 Evergreen Terrace", "Springfield",
                "555-1234", "homer.simpson@example.com", 42,
                List.of("aspirin:100mg"), List.of("none"));

        when(personRepository.findByAddressIn(List.of("742 Evergreen Terrace"))).thenReturn(List.of(person));

        List<Person> result = alertsService.getPeopleByFireStation(stationNumber);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Homer", result.get(0).getFirstName());
    }

    /**
     * Test getPeopleByFireStation with non-existing station number.
     */
    @Test
    void testGetPeopleByFireStation_NotFound() {
        int stationNumber = 99;
        when(fireStationRepository.findByStationNumber(stationNumber)).thenReturn(Collections.emptyList());

        List<Person> result = alertsService.getPeopleByFireStation(stationNumber);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test getCommunityEmails with existing city.
     */
    @Test
    void testGetCommunityEmails_Found() {
        String city = "Springfield";
        Person person1 = new Person("Homer", "Simpson", "742 Evergreen Terrace", city,
                "555-1234", "homer.simpson@example.com", 42,
                List.of("aspirin:100mg"), List.of("none"));
        Person person2 = new Person("Marge", "Simpson", "742 Evergreen Terrace", city,
                "555-1235", "marge.simpson@example.com", 40,
                List.of("vitaminC:500mg"), List.of("shellfish"));

        when(personRepository.findByCity(city)).thenReturn(List.of(person1, person2));

        List<String> emails = alertsService.getCommunityEmails(city);

        assertNotNull(emails);
        assertEquals(2, emails.size());
        assertTrue(emails.contains("homer.simpson@example.com"));
        assertTrue(emails.contains("marge.simpson@example.com"));
    }

    /**
     * Test getCommunityEmails with non-existing city.
     */
    @Test
    void testGetCommunityEmails_NotFound() {
        String city = "Nowhere";
        when(personRepository.findByCity(city)).thenReturn(Collections.emptyList());

        List<String> emails = alertsService.getCommunityEmails(city);

        assertNotNull(emails);
        assertTrue(emails.isEmpty());
    }

    /**
     * Test getChildrenByAddress with existing address.
     */
    @Test
    void testGetChildrenByAddress_Found() {
        String address = "742 Evergreen Terrace";
        Person child = new Person("Bart", "Simpson", address, "Springfield",
                "555-1236", "bart.simpson@example.com", 12,
                Collections.emptyList(), Collections.emptyList());
        Person adult = new Person("Homer", "Simpson", address, "Springfield",
                "555-1234", "homer.simpson@example.com", 42,
                List.of("aspirin:100mg"), List.of("none"));
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("children", List.of(child));
        expectedResponse.put("otherResidents", List.of(adult));

        when(personRepository.findByAddress(address)).thenReturn(List.of(child, adult));

        Map<String, Object> result = alertsService.getChildrenByAddress(address);

        assertNotNull(result);
        assertTrue(result.containsKey("children"));
        assertTrue(result.containsKey("otherResidents"));
        assertEquals(1, ((List<Person>) result.get("children")).size());
        assertEquals(1, ((List<Person>) result.get("otherResidents")).size());
    }

    /**
     * Test getChildrenByAddress with non-existing address.
     */
    @Test
    void testGetChildrenByAddress_NotFound() {
        String address = "123 Unknown Street";
        when(personRepository.findByAddress(address)).thenReturn(Collections.emptyList());

        Map<String, Object> result = alertsService.getChildrenByAddress(address);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test getPhoneNumbersByFireStation with existing station number.
     */
    @Test
    void testGetPhoneNumbersByFireStation_Found() {
        int stationNumber = 1;
        FireStation fs = new FireStation();
        fs.setAddress("742 Evergreen Terrace");
        fs.setStationNumber(stationNumber);

        when(fireStationRepository.findByStationNumber(stationNumber)).thenReturn(List.of(fs));

        Person person = new Person("Homer", "Simpson", "742 Evergreen Terrace", "Springfield",
                "555-1234", "homer.simpson@example.com", 42,
                List.of("aspirin:100mg"), List.of("none"));

        when(personRepository.findByAddressIn(List.of("742 Evergreen Terrace"))).thenReturn(List.of(person));

        List<String> phoneNumbers = alertsService.getPhoneNumbersByFireStation(stationNumber);

        assertNotNull(phoneNumbers);
        assertEquals(1, phoneNumbers.size());
        assertEquals("555-1234", phoneNumbers.get(0));
    }

    /**
     * Test getPhoneNumbersByFireStation with non-existing station number.
     */
    @Test
    void testGetPhoneNumbersByFireStation_NotFound() {
        int stationNumber = 99;
        when(fireStationRepository.findByStationNumber(stationNumber)).thenReturn(Collections.emptyList());

        List<String> phoneNumbers = alertsService.getPhoneNumbersByFireStation(stationNumber);

        assertNotNull(phoneNumbers);
        assertTrue(phoneNumbers.isEmpty());
    }

    /**
     * Test getFireDetailsByAddress with existing address.
     */
    @Test
    void testGetFireDetailsByAddress_Found() {
        String address = "742 Evergreen Terrace";
        int stationNumber = 1;
        FireStation fs = new FireStation();
        fs.setAddress(address);
        fs.setStationNumber(stationNumber);
        Person person = new Person("Homer", "Simpson", address, "Springfield",
                "555-1234", "homer.simpson@example.com", 42,
                List.of("aspirin:100mg"), List.of("none"));

        when(personRepository.findByAddress(address)).thenReturn(List.of(person));
        when(fireStationRepository.findByAddress(address)).thenReturn(List.of(fs));

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("stationNumber", stationNumber);
        expectedResponse.put("residents", List.of(person));

        Map<String, Object> result = alertsService.getFireDetailsByAddress(address);

        assertNotNull(result);
        assertTrue(result.containsKey("stationNumber"));
        assertTrue(result.containsKey("residents"));
        assertEquals(stationNumber, result.get("stationNumber"));
        assertEquals(1, ((List<Person>) result.get("residents")).size());
        assertEquals("Homer", ((List<Person>) result.get("residents")).get(0).getFirstName());
    }

    /**
     * Test getFireDetailsByAddress with non-existing address.
     */
    @Test
    void testGetFireDetailsByAddress_NotFound() {
        String address = "123 Unknown Street";
        when(personRepository.findByAddress(address)).thenReturn(Collections.emptyList());

        Map<String, Object> result = alertsService.getFireDetailsByAddress(address);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test getFloodDetailsByStations with existing station numbers.
     */
    @Test
    void testGetFloodDetailsByStations_Found() {
        List<Integer> stationNumbers = List.of(1, 2);
        String address1 = "742 Evergreen Terrace";
        String address2 = "300 School Lane";
        FireStation fs1 = new FireStation();
        fs1.setAddress(address1);
        fs1.setStationNumber(1);
        FireStation fs2 = new FireStation();
        fs2.setAddress(address2);
        fs2.setStationNumber(2);

        when(fireStationRepository.findByStationNumberIn(stationNumbers)).thenReturn(List.of(fs1, fs2));

        Person person1 = new Person("Homer", "Simpson", address1, "Springfield",
                "555-1234", "homer.simpson@example.com", 42,
                List.of("aspirin:100mg"), List.of("none"));
        Person person2 = new Person("Bart", "Simpson", address1, "Springfield",
                "555-1236", "bart.simpson@example.com", 12,
                Collections.emptyList(), Collections.emptyList());
        Person person3 = new Person("Nelson", "Muntz", address2, "Springfield",
                "555-8903", "nelson.muntz@example.com", 14,
                Collections.emptyList(), List.of("peanut"));

        when(personRepository.findByAddressIn(List.of(address1, address2)))
                .thenReturn(List.of(person1, person2, person3));

        Map<String, List<Person>> expectedHouseholds = new HashMap<>();
        expectedHouseholds.put(address1, List.of(person1, person2));
        expectedHouseholds.put(address2, List.of(person3));

        Map<String, List<Person>> result = alertsService.getFloodDetailsByStations(stationNumbers);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(address1));
        assertTrue(result.containsKey(address2));
        assertEquals(2, result.get(address1).size());
        assertEquals(1, result.get(address2).size());
    }

    /**
     * Test getFloodDetailsByStations with non-existing station numbers.
     */
    @Test
    void testGetFloodDetailsByStations_NotFound() {
        List<Integer> stationNumbers = List.of(99);
        when(fireStationRepository.findByStationNumberIn(stationNumbers)).thenReturn(Collections.emptyList());

        Map<String, List<Person>> result = alertsService.getFloodDetailsByStations(stationNumbers);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test getPersonInfo with existing person.
     */
    @Test
    void testGetPersonInfo_Found() {
        String firstName = "Homer";
        String lastName = "Simpson";
        Person person = new Person(firstName, lastName, "742 Evergreen Terrace", "Springfield",
                "555-1234", "homer.simpson@example.com", 42,
                List.of("aspirin:100mg"), List.of("none"));
        List<Person> persons = List.of(person);

        when(personRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(persons);

        List<Person> result = alertsService.getPersonInfo(firstName, lastName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Homer", result.get(0).getFirstName());
        assertEquals("Simpson", result.get(0).getLastName());
    }

    /**
     * Test getPersonInfo with non-existing person.
     */
    @Test
    void testGetPersonInfo_NotFound() {
        String firstName = "John";
        String lastName = "Doe";
        when(personRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Collections.emptyList());

        List<Person> result = alertsService.getPersonInfo(firstName, lastName);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
