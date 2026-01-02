package com.lixin.vehical_management.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
}
