package com.exam.joseAugusto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Table(name = "EQUIPAMENTO")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "NOME")
    private String name;

    @Column(name = "FORNECEDOR")
    private String provider;

    @Column(name = "DATA_MANUTENCAO")
    private Date nextMaitenanceDate;

    @Column(name = "PESO")
    private Double weight;

    @Column(name = "ATIVO")
    private boolean active;
}
