package com.test.carparking.repos;

import com.test.carparking.entity.Parking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author NIsaev on 20.08.2020
 */

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Integer> {
    Page<Parking> findAll(Pageable pageable);

    List<Parking> findAllByIsFreeEqualsOrderById(boolean isFree);
}