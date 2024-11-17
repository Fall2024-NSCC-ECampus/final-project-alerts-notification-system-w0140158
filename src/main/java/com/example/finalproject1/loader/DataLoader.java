package com.example.finalproject1.loader;

import com.example.finalproject1.model.FireStation;
import com.example.finalproject1.model.Person;
import com.example.finalproject1.repository.FireStationRepository;
import com.example.finalproject1.repository.PersonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, List<Map<String, Object>>>> typeRef = new TypeReference<>() {};

        InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");
        if (inputStream == null) {
            throw new RuntimeException("data.json file not found in resources.");
        }

        Map<String, List<Map<String, Object>>> data = mapper.readValue(inputStream, typeRef);

        // Load fire stations
        List<Map<String, Object>> fireStations = data.get("firestations");
        for (Map<String, Object> fsMap : fireStations) {
            FireStation fs = new FireStation();
            fs.setAddress((String) fsMap.get("address"));
            fs.setStationNumber((Integer) fsMap.get("station"));
            fireStationRepository.save(fs);
        }

        // Load persons
        List<Map<String, Object>> persons = data.get("persons");
        for (Map<String, Object> personMap : persons) {
            Person person = new Person();
            person.setFirstName((String) personMap.get("firstName"));
            person.setLastName((String) personMap.get("lastName"));
            person.setAddress((String) personMap.get("address"));
            person.setCity((String) personMap.get("city"));
            person.setPhone((String) personMap.get("phone"));
            person.setEmail((String) personMap.get("email"));
            person.setAge((Integer) personMap.get("age"));
            person.setMedications((List<String>) personMap.get("medications"));
            person.setAllergies((List<String>) personMap.get("allergies"));
            personRepository.save(person);
        }

        System.out.println("Data loading completed successfully.");
    }
}
