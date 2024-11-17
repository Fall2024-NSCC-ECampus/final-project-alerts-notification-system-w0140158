package com.example.finalproject1.controller;

import com.example.finalproject1.model.Person;
import com.example.finalproject1.service.AlertsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AlertsController {

    private static final Logger logger = LoggerFactory.getLogger(AlertsController.class);

    @Autowired
    private AlertsService alertsService;

    @GetMapping("/firestation")
    public ResponseEntity<Map<String, Object>> getPeopleByStation(@RequestParam int stationNumber) {
        List<Person> people = alertsService.getPeopleByFireStation(stationNumber);
        Map<String, Object> response = new HashMap<>();
        response.put("persons", people); // Include empty array if no data
        response.put("numberOfAdults", people.stream().filter(p -> p.getAge() >= 18).count());
        response.put("numberOfChildren", people.stream().filter(p -> p.getAge() < 18).count());
        return ResponseEntity.ok(response);
    }



    @GetMapping("/childAlert")
    public ResponseEntity<?> getChildrenByAddress(@RequestParam("address") String address) {
        logger.info("Received request for child alert at address: {}", address);
        Map<String, Object> result = alertsService.getChildrenByAddress(address);
        if (result.isEmpty()) {
            logger.warn("No children or residents found at address: {}", address);
            return ResponseEntity.ok(Map.of());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<Map<String, Object>> getPhoneAlert(@RequestParam int stationNumber) {
        List<String> phoneNumbers = alertsService.getPhoneNumbersByFireStation(stationNumber);
        Map<String, Object> response = new HashMap<>();
        response.put("phoneNumbers", phoneNumbers); // Always include the key
        return ResponseEntity.ok(response);
    }


    @GetMapping("/fire")
    public ResponseEntity<?> getFireDetailsByAddress(@RequestParam("address") String address) {
        logger.info("Received request for fire details at address: {}", address);
        Map<String, Object> result = alertsService.getFireDetailsByAddress(address);
        if (result.isEmpty()) {
            logger.warn("No fire details found for address: {}", address);
            return ResponseEntity.ok(Map.of());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<?> getFloodDetailsByStations(@RequestParam("stations") List<Integer> stationNumbers) {
        logger.info("Received request for flood details for station numbers: {}", stationNumbers);
        Map<String, List<Person>> households = alertsService.getFloodDetailsByStations(stationNumbers);
        if (households.isEmpty()) {
            logger.warn("No households found for station numbers: {}", stationNumbers);
            return ResponseEntity.ok(Map.of());
        }
        return ResponseEntity.ok(Map.of("households", households));
    }

    @GetMapping("/personInfo")
    public ResponseEntity<?> getPersonInfo(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName) {
        logger.info("Received request for person info: {} {}", firstName, lastName);
        List<Person> persons = alertsService.getPersonInfo(firstName, lastName);
        if (persons.isEmpty()) {
            logger.warn("No person found with name: {} {}", firstName, lastName);
            return ResponseEntity.ok(Map.of());
        }
        return ResponseEntity.ok(Map.of("persons", persons));
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<?> getCommunityEmails(@RequestParam("city") String city) {
        logger.info("Received request for community emails in city: {}", city);
        List<String> emails = alertsService.getCommunityEmails(city);
        if (emails.isEmpty()) {
            logger.warn("No emails found for city: {}", city);
            return ResponseEntity.ok(Map.of());
        }
        return ResponseEntity.ok(Map.of("emails", emails));
    }


    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        List<Person> allPersons = alertsService.getAllPersons();
        return ResponseEntity.ok(allPersons);
    }
}
