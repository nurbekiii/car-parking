package com.test.carparking.rest;

import com.test.carparking.entity.Parking;
import com.test.carparking.service.ParkingService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author NIsaev on 20.08.2020
 */

@WebMvcTest(ParkingResource.class)
public class WebMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingService service;

    private List<Parking> getList(){
        Parking p1 = new Parking(1, "A-1", true, null);
        Parking p2 = new Parking(2, "A-2", true, null);
        Parking p3 = new Parking(3, "A-3", true, null);
        return Arrays.asList(new Parking[]{p1, p2, p3});
    }

    @Test
    public void serviceShouldGet3FreeParkings() throws Exception {
        //выдаем свободных мест: 3
        when(service.getFreeParkings(true)).thenReturn(getList());

        this.mockMvc.perform(get("/parking/free/count")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("3")));
    }

    @Test
    public void serviceShouldGetFreeParkingsWithPagination() throws Exception {
        //выдаем свободные места с pagination
        when(service.getFreeParkings(true)).thenReturn(getList());

        this.mockMvc.perform(get("/parking/free")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("totalPages")));
    }

    @Test
    public void serviceShouldThrowException() throws Exception {
        //бросаем ошибку, что место с id=-555 не найдено и не существует
        when(service.isFree(-555)).thenThrow(new NotFoundException("The parking with id -555 does not exist"));

        this.mockMvc.perform(get("/parking/is_free/-555")).andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().string(containsString("does not exist")));
    }

    @Test
    public void serviceShouldReturnTrue() throws Exception {
        //при проверке парковочного места №1, выдаем true (свободно - можно заезжать)
        when(service.isFree(1)).thenReturn(true);

        this.mockMvc.perform(get("/parking/is_free/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }
}
