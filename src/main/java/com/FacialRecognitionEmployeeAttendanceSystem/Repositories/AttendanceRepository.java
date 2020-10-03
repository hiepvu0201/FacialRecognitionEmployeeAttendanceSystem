package com.FacialRecognitionEmployeeAttendanceSystem.Repositories;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Attendances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Date;

@Repository
@CrossOrigin(origins = "*")
public interface AttendanceRepository extends JpaRepository<Attendances, Long> {
    public Attendances findByDateCheck(Date dateCheck);
}
