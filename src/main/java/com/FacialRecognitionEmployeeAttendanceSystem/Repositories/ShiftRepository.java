package com.FacialRecognitionEmployeeAttendanceSystem.Repositories;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Shifts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin(origins = "*")
public interface ShiftRepository extends JpaRepository<Shifts, Long> {
    public Shifts findByShiftName(String shiftName);
}
