package com.lixin.vehical_management.controller;

import com.lixin.vehical_management.constants.BaseResponse;
import com.lixin.vehical_management.constants.CommonMessage;
import com.lixin.vehical_management.dto.vehicalDto.VehicleRequest;
import com.lixin.vehical_management.service.ExitRecordService;
import com.lixin.vehical_management.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Main Controller")
public class MainController {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ExitRecordService exitRecordService;

    @PostMapping("/vehicle" )
    @Operation(summary = "Register new vehicle", description = "Register a new vehicle in the system")
    public ResponseEntity<?> registerNewVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.registerVehicle(vehicleRequest)));
    }

    @GetMapping("/vehicle" )
    @Operation(summary = "Get list all vehicle", description = "Get list all vehicle")
    public ResponseEntity<?> getAllVehicle() {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.getVehicles()));
    }

    @GetMapping("/vehicle/{id}" )
    @Operation(summary = "Get vehicle by id", description = "Get vehicle by id")
    public ResponseEntity<?> getVehicleById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.getVehicleById(id)));
    }

    @PutMapping("/vehicle/{id}" )
    @Operation(summary = "Update vehicle", description = "Update vehicle information")
    public ResponseEntity<?> updateVehicle(@Valid @RequestBody VehicleRequest vehicleRequest,
                                           @PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS, vehicleService.updateVehicle(vehicleRequest, id)));
    }
    @DeleteMapping("/vehicle" )
        @Operation(summary = "Delete vehicle", description = "Delete vehicle from the system")
    public ResponseEntity<?> deleteVehicle(@RequestParam(name = "vehicleId") Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS));
    }

    @PostMapping("/vehicle/status" )
    @Operation(summary = "update status", description = "update vehicle status in the system. Note: status can be 'IN', 'OUT'")
    public ResponseEntity<?> updateStatusVehicle(@RequestParam(name = "vehicleId") Long vehicleId,
                                                 @RequestParam(name = "employeeId") Long employeeId,
                                                 @RequestParam(name = "status") String status) {
        exitRecordService.updateRecordVehicle(vehicleId, employeeId, status);
        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS));
    }

    @PostMapping(value = "/import-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    @Operation(summary = "Import vehicles from Excel",

            description = "Import vehicles from Excel file. Headers: 車主姓名, 行車執照, 手機號碼, 狀態 (IN/OUT)")
    public ResponseEntity<?> importVehiclesFromExcel(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new BaseResponse<>("File không được để trống"));
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
            return ResponseEntity.badRequest().body(new BaseResponse<>("Chỉ chấp nhận file Excel (.xlsx, .xls)"));
        }

        return ResponseEntity.ok(new BaseResponse<>(CommonMessage.SUCCESS,
                vehicleService.importVehiclesFromExcel(file)));
    }

}
