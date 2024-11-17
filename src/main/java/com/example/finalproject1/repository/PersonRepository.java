package com.example.finalproject1.repository;

import com.example.finalproject1.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByAddressIn(List<String> addresses);
    List<Person> findByAddress(String address);
    List<Person> findByFirstNameAndLastName(String firstName, String lastName);
    List<Person> findByCity(String city);
}
