package com.lixin.vehical_management.dto.vehicalDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {
    private String plateNumber;
    private String licenseNumber;
    private String owner;
    private String phone;
}
