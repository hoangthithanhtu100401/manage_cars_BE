package com.lixin.vehical_management.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest implements Serializable {
//    @NotEmpty
//    private Long userId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
    private String role;
    private String phone;
    private String email;
}
