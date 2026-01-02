package com.lixin.vehical_management.service;

import com.lixin.vehical_management.dto.EmployeeRequest;
import com.lixin.vehical_management.dto.vehicalDto.VehicleRequest;
import com.lixin.vehical_management.dto.vehicalDto.VehicleResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VehicleService {
    VehicleResponse registerVehicle(VehicleRequest vehicleRequest, Long employeeId);
    VehicleResponse updateVehicle(VehicleRequest userRequest);
    void deleteVehicle(VehicleRequest userRequest);
    List<VehicleResponse> getVehicles();
}
