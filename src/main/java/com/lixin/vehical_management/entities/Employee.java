package com.lixin.vehical_management.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lixin.vehical_management.entities.Enum.Role;
import com.lixin.vehical_management.entities.converter.RoleConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, unique = true)
    private String email;

    @Convert(converter = RoleConverter.class)
    @Column(name = "access_type", columnDefinition = "text")
    private Role role;

    @Column(length = 50)
    private String phone;

    @Column(name = "name", length = 10)
    private String userName;

    @NotBlank
    @Column(name = "pwd", length = 100, nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "hired_at", nullable = false)
    private LocalDateTime hiredAt = LocalDateTime.now();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<EntryExitRecord> performedRecords = new ArrayList<>();

}
