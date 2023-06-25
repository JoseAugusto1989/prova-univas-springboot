package com.exam.joseAugusto.service;

import com.exam.joseAugusto.entity.Equipment;
import com.exam.joseAugusto.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EquipmentServiceTest {

    @Mock
    private EquipmentRepository repository;

    private EquipmentService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new EquipmentService(repository);
    }

    @Test
    void save_shouldReturnSavedEquipment() {
        Equipment equipment = new Equipment(1L,"TAG-01", "Equipment 1", "Provider 1", new Date(), 10.5, true);

        when(repository.saveAndFlush(equipment)).thenReturn(equipment);

        Equipment savedEquipment = service.save(equipment);

        assertEquals(equipment, savedEquipment);

        verify(repository, times(1)).saveAndFlush(equipment);
    }

    @Test
    void findAll_shouldReturnListOfEquipment() {
        Equipment equipment1 = new Equipment(1L,"TAG-01", "Equipment 1", "Provider 1", new Date(), 10.5, true);

        Equipment equipment2 = new Equipment(2L,"TAG-02", "Equipment 2", "Provider 2", new Date(), 10.5, true);

        List<Equipment> equipmentList = Arrays.asList(equipment1, equipment2);

        when(repository.findAll()).thenReturn(equipmentList);

        List<Equipment> result = service.findAll();

        assertEquals(equipmentList, result);

        verify(repository, times(1)).findAll();
    }

    @Test
    void update_shouldReturnUpdatedEquipment() {
        Equipment equipment = new Equipment(1L,"TAG-01", "Equipment 1", "Provider 1", new Date(), 10.5, true);

        when(repository.saveAndFlush(equipment)).thenReturn(equipment);

        Equipment updatedEquipment = service.update(equipment);

        assertEquals(equipment, updatedEquipment);

        verify(repository, times(1)).saveAndFlush(equipment);
    }

    @Test
    void findById_shouldReturnOptionalOfEquipment() {
        Long id = 1L;

        Equipment equipment = new Equipment(1L,"TAG-01", "Equipment 1", "Provider 1", new Date(), 10.5, true);

        when(repository.findById(id)).thenReturn(Optional.of(equipment));

        Optional<Equipment> result = service.findById(id);

        assertEquals(Optional.of(equipment), result);

        verify(repository, times(1)).findById(id);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        Equipment equipment = new Equipment(1L,"TAG-01", "Equipment 1", "Provider 1", new Date(), 10.5, true);

        service.delete(equipment);

        verify(repository, times(1)).delete(equipment);
    }
}
