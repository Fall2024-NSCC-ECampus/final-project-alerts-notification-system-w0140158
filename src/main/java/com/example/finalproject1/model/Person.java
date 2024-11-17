package com.example.finalproject1.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Ensure this field exists

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String phone;
    private String email;
    private Integer age; // Ensure this field exists

    @ElementCollection
    @CollectionTable(name = "person_medications", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "medication")
    private List<String> medications;

    @ElementCollection
    @CollectionTable(name = "person_allergies", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "allergy")
    private List<String> allergies;

    // **Constructors**

    public Person() {
    }

    public Person(String firstName, String lastName, String address, String city, String phone, String email, Integer age, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    // **Getters and Setters**

    public Long getId() {
        return id;
    }

    // If you don't want to expose the setter for 'id', you can omit it
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    // **toString(), equals(), and hashCode() Methods** (Optional)

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", medications=" + medications +
                ", allergies=" + allergies +
                '}';
    }
}
