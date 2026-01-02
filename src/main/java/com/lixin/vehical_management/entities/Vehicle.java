package com.lixin.vehical_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles", indexes = {
        @Index(name = "idx_vehicle_plate", columnList = "plate_number", unique = true)
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner", nullable = false, length = 50)
    private String owner;
    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Column(name = "plate_number", nullable = false, unique = true, length = 50)
    private String plateNumber;

    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntryExitRecord> records = new ArrayList<>();

}
