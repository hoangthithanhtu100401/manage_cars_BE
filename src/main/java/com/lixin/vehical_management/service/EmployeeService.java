package com.lixin.vehical_management.service;

import com.lixin.vehical_management.dto.EmployeeRequest;
import com.lixin.vehical_management.dto.UpdateEmployeeRequest;
import com.lixin.vehical_management.entities.Employee;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {
    ResponseEntity<?> registerUser(EmployeeRequest userRequest);


    void updatePassWord(UpdateEmployeeRequest updateUserRequest, Employee user);
}
