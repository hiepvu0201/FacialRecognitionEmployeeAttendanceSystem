package com.FacialRecognitionEmployeeAttendanceSystem.Repositories;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Payslips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Date;
import java.util.List;

@Repository
@CrossOrigin(origins = "*")
public interface PayslipRepository extends JpaRepository<Payslips, Long> {
    public Payslips findByPayDate(Date date);
    public List<Payslips> findAllByPayDate(Date date);
}
