package com.lixin.vehical_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", unique = true, nullable = false)
    private Employee employee;

    @Column(name = "refresh_token_code", nullable = false, length = 255)
    private String refreshTokenCode;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
}
