package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Departments;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/departments")
public class DepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/")
    public List<Departments> getAllDepartments(){
        return departmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departments> getDepartmentById(@PathVariable(value = "id") Long departmentId) throws ResourceNotFoundException {
        Departments department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("department not found on id: "+departmentId));
        return ResponseEntity.ok().body(department);
    }

    @PostMapping("/add")
    public Departments create(@Validated @RequestBody Departments Departments) throws Exception{
        String departmentName = Departments.getDepartmentName();
        if(departmentName!=null&&!"".equals(departmentName)){
            Departments tempdepartmentName = departmentRepository.findByDepartmentName(departmentName);
            if(tempdepartmentName!=null){
                throw new Exception("department name: "+departmentName+" is already exist");
            }
        }
        return departmentRepository.save(Departments);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Departments> update(@PathVariable(value = "id") Long departmentId,
                                        @Validated @RequestBody Departments departmentDetails) throws Exception{

        Departments department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("This department not found on:" + departmentId));

        boolean isActive = department.isActive();
        if(isActive==false){
            throw new Exception("This department has already been disabled!");
        }

        final Departments updatedepartment = departmentRepository.save(departmentDetails);

        return ResponseEntity.ok(updatedepartment);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Departments> disable(@PathVariable(value = "id") Long departmentId) throws Exception{

        Departments Departments = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("department not found on: " + departmentId));

        boolean isActive = Departments.isActive();
        if(isActive==false)
        {
            throw new Exception("department has already been disabled!");
        }
        Departments.setActive(false);
        final Departments updatedepartment = departmentRepository.save(Departments);

        return ResponseEntity.ok(updatedepartment);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Departments> enable(@PathVariable(value = "id") Long departmentId) throws Exception{

        Departments Departments = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("department not found on:" + departmentId));

        boolean isActive = Departments.isActive();
        if(isActive==true)
        {
            throw new Exception("department has not been disabled yet!");
        }
        Departments.setActive(true);
        final Departments updatedepartment = departmentRepository.save(Departments);

        return ResponseEntity.ok(updatedepartment);
    }
}
