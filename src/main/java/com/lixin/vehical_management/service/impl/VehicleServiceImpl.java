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

import java.time.LocalDateTime;
import java.util.Comparator;
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
                        .createdAt(LocalDateTime.now())
                        .build();
                vehicleRepository.save(vehicle);
                EntryExitType entryExitType = EntryExitType.of(vehicleRequest.getVehicleStatus());
                EntryExitRecord entryExitRecord = EntryExitRecord.builder()
                        .employee(employee)
                        .vehicle(vehicle)
                        .type(entryExitType)
                        .eventTime(LocalDateTime.now())
                        .build();
                exitRecordRepository.save(entryExitRecord);
                return VehicleResponse.builder()
                        .id(vehicle.getId())
                        .plateNumber(vehicle.getPlateNumber())
                        .ownerName(vehicle.getOwner())
                        .licenseNumber(vehicle.getLicenseNumber())
                        .phone(vehicle.getPhone())
                        .createdAt(vehicle.getCreatedAt())
                        .lastIn(entryExitType.equals(EntryExitType.IN) ? entryExitRecord.getEventTime().toString() : "not yet")
                        .lastOut(entryExitType.equals(EntryExitType.OUT) ? entryExitRecord.getEventTime().toString() : "not yet")
                        .status(entryExitType.name)
                        .build();
            }
        }
        else {
            throw new ApplicationRuntimeException("Invalid vehicle registration information", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public VehicleResponse updateVehicle(VehicleRequest userRequest, Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow();
        vehicle.setPlateNumber(userRequest.getPlateNumber());
        vehicle.setOwner(userRequest.getOwner());
        vehicle.setLicenseNumber(userRequest.getLicenseNumber());
        vehicle.setPhone(userRequest.getPhone());
        vehicleRepository.save(vehicle);

        List<EntryExitRecord> records = exitRecordRepository.findByVehicleIdOrderByEventTimeDesc(vehicle.getId());
        if (!ObjectUtils.isEmpty(records)) {
            EntryExitRecord lastRecord = records.get(0);
            Optional<EntryExitRecord> lastIn = records.stream()
                    .filter(record -> record.getType() == EntryExitType.IN)
                    .max(Comparator.comparing(EntryExitRecord::getEventTime));
            Optional<EntryExitRecord> lastOut = records.stream()
                    .filter(record -> record.getType() == EntryExitType.OUT)
                    .max(Comparator.comparing(EntryExitRecord::getEventTime));
            return VehicleResponse.builder()
                    .id(vehicle.getId())
                    .plateNumber(vehicle.getPlateNumber())
                    .ownerName(vehicle.getOwner())
                    .licenseNumber(vehicle.getLicenseNumber())
                    .phone(vehicle.getPhone())
                    .createdAt(vehicle.getCreatedAt())
                    .lastIn(lastIn.map(entryExitRecord -> entryExitRecord.getEventTime().toString()).orElse("not yet"))
                    .lastOut(lastOut.map(entryExitRecord -> entryExitRecord.getEventTime().toString()).orElse("not yet"))
                    .status(lastRecord.getType() == EntryExitType.IN ? EntryExitType.IN.name : EntryExitType.OUT.name)
                    .build();
        } else  {
            throw new ApplicationRuntimeException("Invalid vehicle registration information", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow();
        vehicleRepository.delete(vehicle);
    }

    @Override
    public List<VehicleResponse> getVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (ObjectUtils.isEmpty(vehicles)) {
            return List.of();
        }

        List<VehicleResponse> responses = new java.util.ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            List<EntryExitRecord> records = exitRecordRepository.findByVehicleIdOrderByEventTimeDesc(vehicle.getId());
                EntryExitRecord lastRecord = records.get(0);
                Optional<EntryExitRecord> lastIn = records.stream()
                        .filter(record -> record.getType() == EntryExitType.IN)
                        .max(Comparator.comparing(EntryExitRecord::getEventTime));
                Optional<EntryExitRecord> lastOut = records.stream()
                        .filter(record -> record.getType() == EntryExitType.OUT)
                        .max(Comparator.comparing(EntryExitRecord::getEventTime));
                responses.add(VehicleResponse.builder()
                        .id(vehicle.getId())
                        .plateNumber(vehicle.getPlateNumber())
                        .ownerName(vehicle.getOwner())
                        .licenseNumber(vehicle.getLicenseNumber())
                        .phone(vehicle.getPhone())
                        .createdAt(vehicle.getCreatedAt())
                        .lastIn(lastIn.map(entry -> entry.getEventTime().toString()).orElse("not yet"))
                        .lastOut(lastOut.map(entry -> entry.getEventTime().toString()).orElse("not yet"))
                        .status(lastRecord.getType() == EntryExitType.IN ? EntryExitType.IN.name : EntryExitType.OUT.name)
                        .build());

        }

        return responses;
    }

    @Override
    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow();
        List<EntryExitRecord> records = exitRecordRepository.findByVehicleIdOrderByEventTimeDesc(vehicle.getId());
        if (!ObjectUtils.isEmpty(records)) {
            EntryExitRecord lastRecord = records.get(0);
            Optional<EntryExitRecord> lastIn = records.stream()
                    .filter(record -> record.getType() == EntryExitType.IN)
                    .max(Comparator.comparing(EntryExitRecord::getEventTime));
            Optional<EntryExitRecord> lastOut = records.stream()
                    .filter(record -> record.getType() == EntryExitType.OUT)
                    .max(Comparator.comparing(EntryExitRecord::getEventTime));
            return VehicleResponse.builder()
                    .id(vehicle.getId())
                    .plateNumber(vehicle.getPlateNumber())
                    .ownerName(vehicle.getOwner())
                    .licenseNumber(vehicle.getLicenseNumber())
                    .phone(vehicle.getPhone())
                    .createdAt(vehicle.getCreatedAt())
                    .lastIn(lastIn.map(entryExitRecord -> entryExitRecord.getEventTime().toString()).orElse("not yet"))
                    .lastOut(lastOut.map(entryExitRecord -> entryExitRecord.getEventTime().toString()).orElse("not yet"))
                    .status(lastRecord.getType() == EntryExitType.IN ? EntryExitType.IN.name : EntryExitType.OUT.name)
                    .build();
        } else  {
            throw new ApplicationRuntimeException("Invalid vehicle registration information", HttpStatus.BAD_REQUEST);
        }
    }
}
