package com.example.finalproject1.repository;

import com.example.finalproject1.model.FireStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FireStationRepository extends JpaRepository<FireStation, Long> {
    List<FireStation> findByStationNumber(int stationNumber);
    List<FireStation> findByAddress(String address);
    List<FireStation> findByStationNumberIn(List<Integer> stationNumbers);
}
