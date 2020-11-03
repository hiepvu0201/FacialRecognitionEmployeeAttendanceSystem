package com.FacialRecognitionEmployeeAttendanceSystem.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "tblShifts")
public class Shifts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "shift_name", nullable = false, unique = true)
    private String shiftName;

    @Column(name = "time_start", nullable = false)
    private Time timeStart;

    @Column(name = "time_end", nullable = false)
    private Time timeEnd;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_disabled", columnDefinition = "bit default 0")
    private boolean isDisabled;
}
