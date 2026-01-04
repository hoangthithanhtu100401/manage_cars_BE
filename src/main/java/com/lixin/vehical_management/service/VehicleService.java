package com.lixin.vehical_management.service;

import com.lixin.vehical_management.dto.vehicalDto.VehicleRequest;
import com.lixin.vehical_management.dto.vehicalDto.VehicleResponse;

import java.util.List;

public interface VehicleService {
    VehicleResponse registerVehicle(VehicleRequest vehicleRequest);
    VehicleResponse updateVehicle(VehicleRequest userRequest, Long vehicleId);
    void deleteVehicle(Long vehicleId);
    List<VehicleResponse> getVehicles();
    VehicleResponse getVehicleById(Long id);
}
