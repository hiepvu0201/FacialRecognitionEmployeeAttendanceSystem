package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Departments;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.DepartmentRepository;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/departments")
public class DepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ShiftRepository shiftRepository;

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
    public Departments create(@Validated @RequestBody Departments departments) throws Exception{
        String departmentName = departments.getDepartmentName();
        if(departmentName!=null&&!"".equals(departmentName)){
            Departments tempdepartmentName = departmentRepository.findByDepartmentName(departmentName);
            if(tempdepartmentName!=null){
                throw new Exception("department name: "+departmentName+" is already exist");
            }
        }

        departments.setShifts(shiftRepository.findById(departments.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id " + departments.getShiftId())));

        return departmentRepository.save(departments);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Departments> update(@PathVariable(value = "id") Long departmentId,
                                        @Validated @RequestBody Departments departmentDetails) throws Exception{

        Departments department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("This department not found on:" + departmentId));

        boolean isDisabled = department.isDisabled();
        if(isDisabled==true){
            throw new Exception("This department has already been disabled!");
        }

        department.setDepartmentName(departmentDetails.getDepartmentName());
        department.setShiftId(departmentDetails.getShiftId());
        department.setShifts(shiftRepository.findById(departmentDetails.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + departmentDetails.getShiftId())));

        final Departments updatedepartment = departmentRepository.save(department);

        return ResponseEntity.ok(updatedepartment);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Departments> disable(@PathVariable(value = "id") Long departmentId) throws Exception{

        Departments Departments = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("department not found on: " + departmentId));

        boolean isDisabled = Departments.isDisabled();
        if(isDisabled==true)
        {
            throw new Exception("department has already been disabled!");
        }
        Departments.setDisabled(true);
        final Departments updatedepartment = departmentRepository.save(Departments);

        return ResponseEntity.ok(updatedepartment);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Departments> enable(@PathVariable(value = "id") Long departmentId) throws Exception{

        Departments Departments = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("department not found on:" + departmentId));

        boolean isDisabled = Departments.isDisabled();
        if(isDisabled==false)
        {
            throw new Exception("department has not been disabled yet!");
        }
        Departments.setDisabled(false);
        final Departments updatedepartment = departmentRepository.save(Departments);

        return ResponseEntity.ok(updatedepartment);
    }
    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long departmentId) throws
            Exception {
        Departments department = departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Departments not found on: " + departmentId));
        departmentRepository.delete(department);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
