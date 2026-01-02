package com.lixin.vehical_management.dto.vehicalDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
    private Long id;
    private String plateNumber;
    private String licenseNumber;
    private String ownerName;
    private String phone;
    private LocalDateTime createdAt;
    private String lastIn;
    private String lastOut;
    private String status;
}
