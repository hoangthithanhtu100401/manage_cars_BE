package com.lixin.vehical_management.dto.vehicalDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleImportResponse {
    private int totalRecords;
    private int successCount;
    private int failureCount;
    private List<String> errors;
    private List<VehicleResponse> importedVehicles;
}
