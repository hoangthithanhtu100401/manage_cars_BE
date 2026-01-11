package com.lixin.vehical_management.service.impl;

import com.lixin.vehical_management.dto.vehicalDto.VehicleImportResponse;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final EmployeeRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final EntryExitRecordRepository exitRecordRepository;

    @Override
    public VehicleResponse registerVehicle(VehicleRequest vehicleRequest) {
        Employee user = (Employee) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
//        Employee employee = userRepository.findById(employeeId).orElseThrow();
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
                        .employee(user)
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
            if (records.isEmpty()) {
                continue;
            }
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

        return responses.stream().sorted((v1, v2) -> v2.getCreatedAt().compareTo(v1.getCreatedAt())).toList();
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

    @Override
    public VehicleImportResponse importVehiclesFromExcel(MultipartFile file) {
        List<VehicleResponse> importedVehicles = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int totalRecords = 0;
        int successCount = 0;
        int failureCount = 0;

        Employee currentUser = (Employee) Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        try (ReadableWorkbook workbook = new ReadableWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getFirstSheet();

            try (Stream<Row> rows = sheet.openStream()) {
                List<Row> rowList = rows.toList();

                if (rowList.isEmpty()) {
                    throw new ApplicationRuntimeException(
                            "File Excel trống",
                            HttpStatus.BAD_REQUEST);
                }

                // Validate header row
                Row headerRow = rowList.get(0);
                if (!isValidHeader(headerRow)) {
                    throw new ApplicationRuntimeException(
                            "Invalid Excel format. Expected headers: 車主姓名, 行車執照, 手機號碼, 狀態",
                            HttpStatus.BAD_REQUEST);
                }

                // Process data rows (starting from index 1, skipping header)
                for (int i = 1; i < rowList.size(); i++) {
                    Row row = rowList.get(i);
                    int rowNumber = i + 1; // Excel row number (1-based)

                    if (isEmptyRow(row)) {
                        continue;
                    }

                    totalRecords++;

                    try {
                        // Extract data from cells
                        String ownerName = getCellValueAsString(row, 0);
                        String plateNumber = getCellValueAsString(row, 1);
                        String phone = getCellValueAsString(row, 2);
                        String status = getCellValueAsString(row, 3);

                        // Validate required fields
                        if (ownerName == null || ownerName.trim().isEmpty()) {
                            throw new IllegalArgumentException("車主姓名不能留空。");
                        }
                        if (plateNumber == null || plateNumber.trim().isEmpty()) {
                            throw new IllegalArgumentException("行車執照不能留空。");
                        }
                        if (phone == null || phone.trim().isEmpty()) {
                            throw new IllegalArgumentException("手機號碼不能留空。");
                        }
                        if (status == null || status.trim().isEmpty()) {
                            throw new IllegalArgumentException("狀態不能留空。");
                        }

                        // Validate status (must be IN or OUT)
                        status = status.trim().toUpperCase();
                        if (!status.equals("IN") && !status.equals("OUT")) {
                            throw new IllegalArgumentException("狀態不能留空。");
                        }

                        // Check if vehicle already exists
                        Optional<Vehicle> existingVehicle = vehicleRepository.findByPlateNumber(plateNumber.trim());

                        if (existingVehicle.isPresent()) {
                            errors.add(String.format("第 %d 行：車牌號碼為「%s」的車輛已存在。",
                                    rowNumber, plateNumber));
                            failureCount++;
                            continue;
                        }

                        // Create new vehicle
                        Vehicle vehicle = Vehicle.builder()
                                .plateNumber(plateNumber.trim())
                                .owner(ownerName.trim())
                                .active(true)
                                .licenseNumber(plateNumber.trim())
                                .phone(phone.trim())
                                .createdAt(LocalDateTime.now())
                                .build();
                        vehicleRepository.save(vehicle);

                        // Create entry/exit record
                        EntryExitType entryExitType = EntryExitType.of(status);
                        EntryExitRecord entryExitRecord = EntryExitRecord.builder()
                                .employee(currentUser)
                                .vehicle(vehicle)
                                .type(entryExitType)
                                .eventTime(LocalDateTime.now())
                                .build();
                        exitRecordRepository.save(entryExitRecord);

                        // Build response
                        VehicleResponse response = VehicleResponse.builder()
                                .id(vehicle.getId())
                                .plateNumber(vehicle.getPlateNumber())
                                .ownerName(vehicle.getOwner())
                                .licenseNumber(vehicle.getLicenseNumber())
                                .phone(vehicle.getPhone())
                                .createdAt(vehicle.getCreatedAt())
                                .lastIn(entryExitType.equals(EntryExitType.IN) ?
                                        entryExitRecord.getEventTime().toString() : "not yet")
                                .lastOut(entryExitType.equals(EntryExitType.OUT) ?
                                        entryExitRecord.getEventTime().toString() : "not yet")
                                .status(entryExitType.name)
                                .build();

                        importedVehicles.add(response);
                        successCount++;

                    } catch (Exception e) {
                        errors.add(String.format("第 %d 行: %s", rowNumber, e.getMessage()));
                        failureCount++;
                    }
                }
            }

        } catch (IOException e) {
            throw new ApplicationRuntimeException(
                    "無法讀取Excel文件: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        return VehicleImportResponse.builder()
                .totalRecords(totalRecords)
                .successCount(successCount)
                .failureCount(failureCount)
                .errors(errors)
                .importedVehicles(importedVehicles)
                .build();
    }

    private boolean isValidHeader(Row headerRow) {
        try {
            String col0 = getCellValueAsString(headerRow, 0);
            String col1 = getCellValueAsString(headerRow, 1);
            String col2 = getCellValueAsString(headerRow, 2);
            String col3 = getCellValueAsString(headerRow, 3);

            return "車主姓名".equals(col0) &&
                    "行車執照".equals(col1) &&
                    "手機號碼".equals(col2) &&
                    "狀態".equals(col3);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }
        for (int cellNum = 0; cellNum < 4; cellNum++) {
            String value = getCellValueAsString(row, cellNum);
            if (value != null && !value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String getCellValueAsString(Row row, int columnIndex) {
        try {
            return row.getCellAsString(columnIndex).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
