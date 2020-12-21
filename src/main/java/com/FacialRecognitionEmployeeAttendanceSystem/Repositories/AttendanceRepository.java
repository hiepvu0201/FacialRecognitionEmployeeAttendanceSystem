package com.FacialRecognitionEmployeeAttendanceSystem.Repositories;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Attendances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Date;
import java.util.List;

@Repository
@CrossOrigin(origins = "*")
public interface AttendanceRepository extends JpaRepository<Attendances, Long> {
    public Attendances findByDateCheckAndUserId(Date dateCheck, long userId);
    public Attendances findByUserId(long userId);
    public List<Attendances> findAllByDateCheck(Date dateCheck);
    public List<Attendances> findAllByUserId(long userId);
}
