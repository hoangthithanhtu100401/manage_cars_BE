package com.lixin.vehical_management.entities;

import com.lixin.vehical_management.entities.Enum.EntryExitType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "entry_exit_records", indexes = {
        @Index(name = "idx_record_vehicle_time", columnList = "vehicle_id, event_time")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntryExitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee; // who processed the event

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private EntryExitType type;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime = LocalDateTime.now();

}
