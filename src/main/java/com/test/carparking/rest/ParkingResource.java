package com.test.carparking.rest;

import com.test.carparking.entity.Parking;
import com.test.carparking.exception.NotBlockedException;
import com.test.carparking.exception.NotFreeException;
import com.test.carparking.service.ParkingService;
import javassist.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NIsaev on 20.08.2020
 */

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ParkingResource {

    //@Autowired
    private final ParkingService parkingService;

    public ParkingResource(ParkingService parkingService) {
        this.parkingService = parkingService;
    }


    @GetMapping("/parking/all")
    public Page<Parking> getAllParkings(@RequestParam(defaultValue = "0", required = false) int page,
                                        @RequestParam(defaultValue = "20", required = false) int size) {
        //получить список всех парковочных мест (через pagination)
        Pageable paging = PageRequest.of(page, size, Sort.by("id"));
        Page<Parking> list = parkingService.getAllParkings(paging);
        return list;
    }

    @GetMapping("/parking/free")
    public Page<Parking> getFreeParkings() {
        //получить список всех СВОБОДНЫХ парковочных мест (через pagination)
        List<Parking> list = parkingService.getFreeParkings(true);
        Page<Parking> page = new PageImpl<>(list);
        return page;
    }

    @GetMapping("/parking/free/count")
    public ResponseEntity<Integer> getFreeParkingsCount() {
        //получить кол-во СВОБОДНЫХ парковочных мест (через pagination)
        List<Parking> list = parkingService.getFreeParkings(true);
        int res = (list != null && !list.isEmpty() ? list.size() : 0);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/parking/is_free/{id}")
    public ResponseEntity<Boolean> checkIsFreeParking(@PathVariable int id) throws NotFoundException {
        //проверка парковочного места, true - место свободно
        Boolean res = parkingService.isFree(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/parking/block/{id}")
    public ResponseEntity<Parking> blockParking(@PathVariable int id) throws NotFoundException, NotFreeException {
        //въезд авто - блокировка парковочного места
        Boolean res = parkingService.isFree(id);
        if(!res)
            throw new NotFreeException("The parking place with id " + id + " is not free. It can not be blocked");

        Parking ent = parkingService.blockParking(id);
        return new ResponseEntity<>(ent, HttpStatus.OK);
    }

    @PostMapping("/parking/unblock/{id}")
    public ResponseEntity<Parking> unblockParking(@PathVariable int id) throws NotFoundException, NotBlockedException {
        //выезд авто - разблокировка парковочного места
        Boolean res = parkingService.isFree(id);
        if(res)
            throw new NotBlockedException("The parking place with id " + id + " is already free. It can not be unblocked");

        Parking ent = parkingService.unblockParking(id);
        return new ResponseEntity<>(ent, HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleException(NotFoundException ex) {
        //когда не найдено парковочное место
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFreeException.class)
    public ResponseEntity<String> handleException(NotFreeException ex) {
        //когда не свободно парковочное место
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotBlockedException.class)
    public ResponseEntity<String> handleException(NotBlockedException ex) {
        //когда не забронировано парковочное место
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
