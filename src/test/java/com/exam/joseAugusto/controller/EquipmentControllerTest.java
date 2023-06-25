package com.exam.joseAugusto.controller;

import com.exam.joseAugusto.dto.EquipmentDto;
import com.exam.joseAugusto.entity.Equipment;
import com.exam.joseAugusto.service.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class EquipmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(equipmentController).build();
    }

    @Test
    public void saveEquip_whenValidRequest_shouldReturnSuccess() throws Exception {
        EquipmentDto requestDto = new EquipmentDto();
        requestDto.setName("Equipment 1");
        requestDto.setProvider("Provider 1");
        requestDto.setWeight(10.5);
        requestDto.setActive(true);
        requestDto.setTag("TAG-01");
        requestDto.setNextMaitenanceDate(new Date());

        Equipment savedEquipment = new Equipment();
        savedEquipment.setId(1L);
        savedEquipment.setName("Equipment 1");
        savedEquipment.setProvider("Provider 1");
        savedEquipment.setWeight(10.5);
        savedEquipment.setActive(true);
        savedEquipment.setTag("TAG-01");
        savedEquipment.setNextMaitenanceDate(new Date());

        when(equipmentService.save(any(Equipment.class))).thenReturn(savedEquipment);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Equipment 1"))
                .andExpect(jsonPath("$.provider").value("Provider 1"))
                .andExpect(jsonPath("$.weight").value(10.5))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.tag").value("TAG-01"))
                .andExpect(jsonPath("$.nextMaitenanceDate").value(requestDto.getNextMaitenanceDate()));

        verify(equipmentService, times(1)).save(any(Equipment.class));
    }

    @Test
    public void getById_whenExistingId_shouldReturnSuccess() throws Exception {
        Equipment equipment = new Equipment();
        equipment.setId(1L);
        equipment.setName("Equipment 1");
        equipment.setProvider("Provider 1");
        equipment.setWeight(10.5);
        equipment.setActive(true);
        equipment.setTag("TAG-01");
        equipment.setNextMaitenanceDate(new Date());

        when(equipmentService.findById(eq(1L))).thenReturn(Optional.of(equipment));

        mockMvc.perform(get("/equipment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Equipment 1"))
                .andExpect(jsonPath("$.provider").value("Provider 1"))
                .andExpect(jsonPath("$.weight").value(10.5))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.tag").value("TAG-01"));

        verify(equipmentService, times(1)).findById(eq(1L));
    }

    @Test
    public void getById_whenNonExistingId_shouldReturnNotFound() throws Exception {
        when(equipmentService.findById(eq(1L))).thenReturn(Optional.empty());

        mockMvc.perform(get("/equipment/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Equipment not Found!"));

        verify(equipmentService, times(1)).findById(eq(1L));
    }

    @Test
    public void getById_whenNotFindInRepository_ExpectedReturnNotFoundResponse() {
        var idMock = 123L;
        when(equipmentService.findById(idMock)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = equipmentController.getById(idMock);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Equipment not Found!", response.getBody());
    }

    @Test
    void update_whenNonExistingId_shouldReturnNotFound() throws Exception {
        Long equipmentId = 12345L;

        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("Equip test");
        equipmentDto.setNextMaitenanceDate(new Date());
        equipmentDto.setActive(true);
        equipmentDto.setTag("Equipment Tag");
        equipmentDto.setProvider("Equipment Provider");
        equipmentDto.setWeight(10.5);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(equipmentDto);

        mockMvc.perform(put("/equipment/{id}", equipmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEquip_whenExistingId_shouldReturnSuccess() throws Exception {
        Equipment existingEquipment = new Equipment();
        existingEquipment.setId(1L);
        existingEquipment.setName("Equipment 1");
        existingEquipment.setProvider("Provider 1");
        existingEquipment.setTag("TAG-01");
        existingEquipment.setWeight(10.5);
        existingEquipment.setActive(true);
        existingEquipment.setNextMaitenanceDate(new Date());

        when(equipmentService.findById(eq(1L))).thenReturn(Optional.of(existingEquipment));

        mockMvc.perform(delete("/equipment/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Equipment deleted successfully"));

        verify(equipmentService, times(1)).findById(eq(1L));
        verify(equipmentService, times(1)).delete(eq(existingEquipment));
    }

    @Test
    public void deleteEquip_whenNonExistingId_shouldReturnNotFound() throws Exception {
        when(equipmentService.findById(eq(1L))).thenReturn(Optional.empty());

        mockMvc.perform(delete("/equipment/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Equipment not found"));

        verify(equipmentService, times(1)).findById(eq(1L));
        verify(equipmentService, times(0)).delete(any(Equipment.class));
    }

    @Test
    void getAll_shouldReturnListOfEquipment() {
        Equipment equipment1 = new Equipment(1L, "Tag 1", "Equipment 1", "Provider 1",
                new Date(), 10.5, true);
        Equipment equipment2 = new Equipment(2L, "Tag 2", "Equipment 2", "Provider 2",
                new Date(), 8.2, false);
        List<Equipment> equipmentList = new ArrayList<>();
        equipmentList.add(equipment1);
        equipmentList.add(equipment2);

        EquipmentService equipmentService = Mockito.mock(EquipmentService.class);
        Mockito.when(equipmentService.findAll()).thenReturn(equipmentList);

        EquipmentController equipmentController = new EquipmentController(equipmentService);

        ResponseEntity<List<Equipment>> responseEntity = equipmentController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(equipmentList, responseEntity.getBody());
    }

    @Test
    void update_shouldReturnUpdatedEquipment() {
        Long equipmentId = 1L;

        EquipmentDto dto = new EquipmentDto();
        dto.setName("Updated Equipment");
        dto.setProvider("Updated Provider");
        dto.setWeight(15.0);
        dto.setActive(false);
        dto.setTag("Updated Tag");
        dto.setNextMaitenanceDate(new Date());

        Equipment existingEquipment = new Equipment();
        existingEquipment.setId(equipmentId);
        existingEquipment.setName("Existing Equipment");
        existingEquipment.setProvider("Existing Provider");
        existingEquipment.setWeight(10.0);
        existingEquipment.setActive(true);
        existingEquipment.setTag("Existing Tag");
        existingEquipment.setNextMaitenanceDate(new Date());

        Equipment updatedEquipment = new Equipment();
        updatedEquipment.setId(equipmentId);
        updatedEquipment.setName(dto.getName());
        updatedEquipment.setProvider(dto.getProvider());
        updatedEquipment.setWeight(dto.getWeight());
        updatedEquipment.setActive(dto.isActive());
        updatedEquipment.setTag(dto.getTag());
        updatedEquipment.setNextMaitenanceDate(dto.getNextMaitenanceDate());

        when(equipmentService.findById(eq(equipmentId))).thenReturn(Optional.of(existingEquipment));
        when(equipmentService.save(any(Equipment.class))).thenReturn(updatedEquipment);

        ResponseEntity<Object> responseEntity = equipmentController.update(equipmentId, dto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(updatedEquipment, responseEntity.getBody());
    }
}
