package com.lixin.vehical_management.controller;

import com.lixin.vehical_management.constants.BaseResponse;
import com.lixin.vehical_management.constants.CommonMessage;
import com.lixin.vehical_management.dto.EmployeeRequest;
import com.lixin.vehical_management.dto.vehicalDto.VehicleRequest;
import com.lixin.vehical_management.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Main Controller")
@RequiredArgsConstructor
public class MainController {
    private final VehicleService vehicleService;

    @PostMapping("/vehicle" )
    @Operation(summary = "Register new vehicle", description = "Register a new vehicle in the system")
    public ResponseEntity<?> registerNewVehicle(@Valid @RequestBody VehicleRequest vehicleRequest,
                                                @RequestParam(name = "employeeId") Long employeeId) {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.registerVehicle(vehicleRequest, employeeId)));
    }

    @GetMapping("/vehicle" )
    @Operation(summary = "Get list all vehicle", description = "Get list all vehicle")
    public ResponseEntity<?> getAllVehicle(@Valid @RequestBody VehicleRequest vehicleRequest,
                                                @RequestParam(name = "employeeId") Long employeeId) {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.registerVehicle(vehicleRequest, employeeId)));
    }

    @GetMapping("/vehicle/{id}" )
    @Operation(summary = "Get vehicle by id", description = "Get vehicle by id")
    public ResponseEntity<?> getVehicleById(@Valid @RequestBody VehicleRequest vehicleRequest,
                                              @RequestParam(name = "employeeId") Long employeeId) {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.registerVehicle(vehicleRequest, employeeId)));
    }

    @PutMapping("/vehicle" )
    @Operation(summary = "Update vehicle", description = "Update vehicle information")
    public ResponseEntity<?> updateVehicle(@Valid @RequestBody VehicleRequest vehicleRequest,
                                                @RequestParam(name = "employeeId") Long employeeId) {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.registerVehicle(vehicleRequest, employeeId)));
    }
    @DeleteMapping("/vehicle" )
        @Operation(summary = "Delete vehicle", description = "Delete vehicle from the system")
    public ResponseEntity<?> deleteVehicle(@Valid @RequestBody VehicleRequest vehicleRequest,
                                                @RequestParam(name = "employeeId") Long employeeId) {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.registerVehicle(vehicleRequest, employeeId)));
    }

}
