package com.lixin.vehical_management.repositories;

import com.lixin.vehical_management.entities.EntryExitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntryExitRecordRepository extends JpaRepository<EntryExitRecord, Long> {
    List<EntryExitRecord> findByVehicleIdOrderByEventTimeDesc(Long vehicleId);
    List<EntryExitRecord> findByEmployeeId(Long employeeId);
    List<EntryExitRecord> findByEventTimeBetween(LocalDateTime start, LocalDateTime end);
}