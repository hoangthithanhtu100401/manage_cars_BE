package com.lixin.vehical_management.service.impl;

import com.lixin.vehical_management.entities.Employee;
import com.lixin.vehical_management.entities.EntryExitRecord;
import com.lixin.vehical_management.entities.Enum.EntryExitType;
import com.lixin.vehical_management.entities.Vehicle;
import com.lixin.vehical_management.repositories.EmployeeRepository;
import com.lixin.vehical_management.repositories.EntryExitRecordRepository;
import com.lixin.vehical_management.repositories.VehicleRepository;
import com.lixin.vehical_management.service.ExitRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExitRecordServiceImpl implements ExitRecordService {
    private final EmployeeRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final EntryExitRecordRepository exitRecordRepository;

    public void updateRecordVehicle(Long vehicleId, Long employeeId, String status) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        Optional<Employee> employee = userRepository.findById(employeeId);
        EntryExitType entryExitType = EntryExitType.of(status);
        if (vehicle.isPresent() && employee.isPresent()) {
            EntryExitRecord entryExitRecord = EntryExitRecord.builder()
                    .type(entryExitType)
                    .employee(employee.get())
                    .vehicle(vehicle.get())
                    .eventTime(LocalDateTime.now())
                    .build();
            exitRecordRepository.save(entryExitRecord);
        } else {
            throw new RuntimeException("Vehicle or Employee not found");
        }
    }
}
