package com.exam.joseAugusto.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class EquipmentDto {

    @NotBlank
    private String tag;

    @NotBlank
    private String name;

    @NotBlank
    private String provider;

    @NotNull
    private Date nextMaitenanceDate;

    @NotNull
    private Double weight;

    @NotNull
    private boolean active;
}
