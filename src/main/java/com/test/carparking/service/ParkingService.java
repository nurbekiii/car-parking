package com.test.carparking.service;

import com.test.carparking.entity.Parking;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author NIsaev on 20.08.2020
 */
public interface ParkingService {
    boolean isFree(int parkingId) throws NotFoundException;

    Parking blockParking(int parkingId);

    Parking unblockParking(int parkingId);

    List<Parking> getFreeParkings(boolean isFree);

    Page<Parking> getAllParkings(Pageable pageable);
}
