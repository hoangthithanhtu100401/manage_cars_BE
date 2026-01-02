package com.lixin.vehical_management.service.impl;

import com.lixin.vehical_management.dto.vehicalDto.VehicleRequest;
import com.lixin.vehical_management.dto.vehicalDto.VehicleResponse;
import com.lixin.vehical_management.entities.Employee;
import com.lixin.vehical_management.entities.EntryExitRecord;
import com.lixin.vehical_management.entities.Enum.EntryExitType;
import com.lixin.vehical_management.entities.Vehicle;
import com.lixin.vehical_management.exception.ApplicationRuntimeException;
import com.lixin.vehical_management.repositories.EmployeeRepository;
import com.lixin.vehical_management.repositories.EntryExitRecordRepository;
import com.lixin.vehical_management.repositories.VehicleRepository;
import com.lixin.vehical_management.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final EmployeeRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final EntryExitRecordRepository exitRecordRepository;

    @Override
    public VehicleResponse registerVehicle(VehicleRequest vehicleRequest, Long employeeId) {
//        Employee user = (Employee) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        Employee employee = userRepository.findById(employeeId).orElseThrow();
        if (!Objects.isNull(vehicleRequest)) {
            Optional<Vehicle> existingVehicle = vehicleRepository.findByPlateNumber(vehicleRequest.getPlateNumber());
            if (existingVehicle.isPresent()) {
                throw new ApplicationRuntimeException("Vehicle already exists", HttpStatus.BAD_REQUEST);
            } else  {
                Vehicle vehicle = Vehicle.builder()
                        .plateNumber(vehicleRequest.getPlateNumber())
                        .owner(vehicleRequest.getOwner())
                        .active(true)
                        .licenseNumber(vehicleRequest.getLicenseNumber())
                        .phone(vehicleRequest.getPhone())
                        .build();
                vehicleRepository.save(vehicle);
                EntryExitRecord entryExitRecord = EntryExitRecord.builder()
                        .employee(employee)
                        .vehicle(vehicle)
                        .type(EntryExitType.IN)
                        .build();
                exitRecordRepository.save(entryExitRecord);
                return VehicleResponse.builder()
                        .id(vehicle.getId())
                        .plateNumber(vehicle.getPlateNumber())
                        .ownerName(vehicle.getOwner())
                        .licenseNumber(vehicle.getLicenseNumber())
                        .phone(vehicle.getPhone())
                        .createdAt(vehicle.getCreatedAt())
                        .lastIn(entryExitRecord.getEventTime().toString())
                        .lastOut("not yet")
                        .status(EntryExitType.IN.name)
                        .build();
            }
        }
        else {
            throw new ApplicationRuntimeException("Invalid vehicle registration information", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public VehicleResponse updateVehicle(VehicleRequest userRequest) {
        return null;
    }

    @Override
    public void deleteVehicle(VehicleRequest userRequest) {

    }

    @Override
    public List<VehicleResponse> getVehicles() {
        return List.of();
    }
}
