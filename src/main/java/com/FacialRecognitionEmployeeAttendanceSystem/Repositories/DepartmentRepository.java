package com.FacialRecognitionEmployeeAttendanceSystem.Repositories;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin(origins = "*")
public interface DepartmentRepository extends JpaRepository<Departments, Long> {
    public Departments findByDepartmentName(String departmentName);
}
