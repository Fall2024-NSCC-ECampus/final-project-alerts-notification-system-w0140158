package com.example.finalproject1.service;

import com.example.finalproject1.model.FireStation;
import com.example.finalproject1.model.Person;
import com.example.finalproject1.repository.FireStationRepository;
import com.example.finalproject1.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// Additional imports
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlertsService {

    private static final Logger logger = LoggerFactory.getLogger(AlertsService.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;


    public List<Person> getPeopleByFireStation(int stationNumber) {
        logger.debug("Fetching fire stations with station number: {}", stationNumber);
        List<FireStation> stations = fireStationRepository.findByStationNumber(stationNumber);
        if (stations.isEmpty()) {
            logger.warn("No fire stations found for station number: {}", stationNumber);
            return Collections.emptyList();
        }
        List<String> addresses = stations.stream()
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
        logger.debug("Addresses found: {}", addresses);
        List<Person> people = personRepository.findByAddressIn(addresses);
        logger.debug("Number of people found: {}", people.size());
        return people;
    }

    /**
     * Retrieves a list of children at a specific address along with other residents.
     *
     * @param address the address to search
     * @return map containing children and other residents or empty map if none found
     */
    public Map<String, Object> getChildrenByAddress(String address) {
        logger.debug("Fetching children at address: {}", address);
        List<Person> allResidents = personRepository.findByAddress(address);
        if (allResidents.isEmpty()) {
            logger.warn("No residents found at address: {}", address);
            return Collections.emptyMap();
        }
        List<Person> children = allResidents.stream()
                .filter(p -> p.getAge() <= 18)
                .collect(Collectors.toList());
        if (children.isEmpty()) {
            logger.info("No children found at address: {}", address);
            return Collections.emptyMap();
        }
        List<Person> otherResidents = allResidents.stream()
                .filter(p -> p.getAge() > 18)
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("children", children);
        response.put("otherResidents", otherResidents);
        logger.debug("Found {} children and {} other residents at address: {}", children.size(), otherResidents.size(), address);
        return response;
    }

    /**
     * Retrieves phone numbers of all persons within the jurisdiction of a specific fire station.
     *
     * @param stationNumber the fire station number
     * @return list of phone numbers or empty list if none found
     */
    public List<String> getPhoneNumbersByFireStation(int stationNumber) {
        logger.debug("Fetching phone numbers for fire station number: {}", stationNumber);
        List<Person> people = getPeopleByFireStation(stationNumber);
        List<String> phoneNumbers = people.stream()
                .map(Person::getPhone)
                .collect(Collectors.toList());
        logger.debug("Number of phone numbers found: {}", phoneNumbers.size());
        return phoneNumbers;
    }

    /**
     * Retrieves fire station details and residents at a specific address.
     *
     * @param address the address to search
     * @return map containing fire station number and list of residents or empty map if none found
     */
    public Map<String, Object> getFireDetailsByAddress(String address) {
        logger.debug("Fetching fire details for address: {}", address);
        List<FireStation> stations = fireStationRepository.findByAddress(address);
        if (stations.isEmpty()) {
            logger.warn("No fire station found for address: {}", address);
            return Collections.emptyMap();
        }
        int stationNumber = stations.get(0).getStationNumber(); // Assuming one station per address
        List<Person> residents = personRepository.findByAddress(address);
        if (residents.isEmpty()) {
            logger.warn("No residents found at address: {}", address);
            return Collections.emptyMap();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("stationNumber", stationNumber);
        response.put("residents", residents);
        logger.debug("Returning fire details for address: {}", address);
        return response;
    }

    /**
     * Retrieves households covered by a list of fire station numbers.
     *
     * @param stationNumbers list of fire station numbers
     * @return map of address to list of residents or empty map if none found
     */
    public Map<String, List<Person>> getFloodDetailsByStations(List<Integer> stationNumbers) {
        logger.debug("Fetching flood details for station numbers: {}", stationNumbers);
        List<FireStation> stations = fireStationRepository.findByStationNumberIn(stationNumbers);
        if (stations.isEmpty()) {
            logger.warn("No fire stations found for station numbers: {}", stationNumbers);
            return Collections.emptyMap();
        }
        List<String> addresses = stations.stream()
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
        logger.debug("Addresses found: {}", addresses);
        List<Person> people = personRepository.findByAddressIn(addresses);
        if (people.isEmpty()) {
            logger.warn("No people found for the provided station numbers.");
            return Collections.emptyMap();
        }
        Map<String, List<Person>> households = people.stream()
                .collect(Collectors.groupingBy(Person::getAddress));
        logger.debug("Number of households found: {}", households.size());
        return households;
    }

    /**
     * Retrieves person information by first and last name.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @return list of persons or empty list if none found
     */
    public List<Person> getPersonInfo(String firstName, String lastName) {
        logger.debug("Fetching person info for: {} {}", firstName, lastName);
        List<Person> persons = personRepository.findByFirstNameAndLastName(firstName, lastName);
        logger.debug("Number of persons found: {}", persons.size());
        return persons;
    }

    /**
     * Retrieves email addresses of all persons in a specific city.
     *
     * @param city the city to search
     * @return list of email addresses or empty list if none found
     */
    public List<String> getCommunityEmails(String city) {
        logger.debug("Fetching community emails for city: {}", city);
        List<Person> persons = personRepository.findByCity(city);
        if (persons.isEmpty()) {
            logger.warn("No persons found in city: {}", city);
            return Collections.emptyList();
        }
        List<String> emails = persons.stream()
                .map(Person::getEmail)
                .filter(email -> email != null && !email.isEmpty())
                .collect(Collectors.toList());
        logger.debug("Number of emails found: {}", emails.size());
        return emails;
    }

    /**
     * Retrieves all persons in the database.
     *
     * @return list of all persons
     */
    public List<Person> getAllPersons() {
        logger.debug("Fetching all persons from the database.");
        return personRepository.findAll();
    }
}
