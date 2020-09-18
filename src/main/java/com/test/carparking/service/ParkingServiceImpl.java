package com.test.carparking.service;

import com.test.carparking.entity.Parking;
import com.test.carparking.repos.ParkingRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author NIsaev on 20.08.2020
 */

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Override
    public boolean isFree(int parkingId) throws NotFoundException {
        Optional<Parking> entity = parkingRepository.findById(parkingId);

        if (!entity.isPresent())
            throw new NotFoundException("The parking with id " + parkingId + " does not exist");

        return entity.get().getFree();
    }

    @Override
    public Parking blockParking(int parkingId) {
        //бронирование парковки
        Optional<Parking> entity = parkingRepository.findById(parkingId);
        Parking prk = entity.get();
        prk.setFree(false);
        prk.setRegDate(LocalDateTime.now());
        return parkingRepository.save(prk);
    }

    @Override
    public Parking unblockParking(int parkingId) {
        //разбронирование парковки
        Optional<Parking> entity = parkingRepository.findById(parkingId);
        Parking prk = entity.get();
        prk.setFree(true);
        prk.setRegDate(null);
        return parkingRepository.save(prk);
    }

    @Override
    public List<Parking> getFreeParkings(boolean isFree) {
        //получить все свободные парковачные места
        return parkingRepository.findAllByIsFreeEqualsOrderById(isFree);
    }

    @Override
    public Page<Parking> getAllParkings(Pageable pageable) {
        //получить все парковачные места
        return parkingRepository.findAll(pageable);
    }
}
