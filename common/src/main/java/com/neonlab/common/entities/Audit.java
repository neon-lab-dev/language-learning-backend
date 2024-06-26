package com.neonlab.common.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor

//No read operation from this table
//common repo

public class Audit extends Generic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "raw_request")
    private String rawRequest;

    @Column(name = "raw_response")
    private String rawResponse;

    @Column(name = "service_provider")
    private String serviceProvider;

    //generic

}
