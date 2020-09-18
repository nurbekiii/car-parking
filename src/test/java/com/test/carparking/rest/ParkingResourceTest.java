package com.test.carparking.rest;

import com.test.carparking.entity.Parking;
import com.test.carparking.exception.NotBlockedException;
import com.test.carparking.exception.NotFreeException;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author NIsaev on 20.08.2020
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParkingResourceTest {

    @Autowired
    private ParkingResource parkingResource;

    @Test
    public void contexLoads() throws Exception {
        //assertThat(parkingResource).isNotNull();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void freeParkingPlacesMoreThan6() throws Exception {
        //кол-во свободных парковочных > 6 мест
        String res = this.restTemplate.getForObject("http://localhost:" + port + "/parking/free/count", String.class);
        assertThat(Integer.valueOf(res) > 6);
    }

    @Test
    public void parkingPlaceNotFoundVerify() {
        //парковочное место с id=999 не существует
        try {
            String res = this.restTemplate.getForObject("http://localhost:" + port + "/parking/is_free/999", String.class);
        } catch (Exception t) {
            assertThat(t instanceof NotFoundException);
            assertThat(t.getMessage().contains("does not exist"));
        }
    }

    @Test
    public void canNotBlockNonExistingParkingPlace() throws Exception {
        //нельзя забронировать место с id=999, т.к. оно не существует
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/parking/block/999", null,
                String.class)).contains("does not exist");
    }

    @Test
    public void canNotUnblockNonExistingParkingPlace() throws Exception {
        //нельзя освободить место с id=999, т.к. оно не существует
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/parking/unblock/999", null,
                String.class)).contains("does not exist");
    }

    @Test
    public void freeParkingPlacesMoreThan5() throws Exception {
        //свободных мест больше 5
        assertThat(parkingResource.getFreeParkingsCount().getBody() > 5);
    }

    @Test
    public void parkingPlace3IsFree() throws Exception {
        //место №3 - свободно
        assertThat(parkingResource.checkIsFreeParking(3).getBody() == true);
    }

    @Test
    public void checkToTryBlockPlace9() throws Exception {
        //место №9 - пытаемся блокировать(въезд авто), и провести проверки
        try {
            Parking parking = parkingResource.blockParking(9).getBody();
            if (parking != null) {
                assertThat(parking.getFree() == false);
                assertThat(parking.getRegDate() != null);
            }
        } catch (Exception t) {
            //при повторной блокировке будет Exception, его и проверяем
            assertThat(t instanceof NotFreeException);
            assertThat(t.getMessage().contains("It can not be blocked"));
        }
    }

    @Test
    public void checkToTryUnblockPlace7() throws Exception {
        //место №7 - пытаемся освободить(выезд авто), и провести проверки
        try {
            Parking parking = parkingResource.unblockParking(7).getBody();
            if (parking != null) {
                assertThat(parking.getFree() == true);
                assertThat(parking.getRegDate() == null);
            }
        } catch (Exception t) {
            //при повторной разблокировке будет Exception, его и проверяем
            assertThat(t instanceof NotBlockedException);
            assertThat(t.getMessage().contains("It can not be unblocked"));
        }
    }
}
