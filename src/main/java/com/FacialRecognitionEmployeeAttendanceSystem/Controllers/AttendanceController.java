package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Attendances;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/attendances")
public class AttendanceController {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/")
    public List<Attendances> getAllAttendances(){
        return attendanceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendances> getattendanceById(@PathVariable(value = "id") Long attendanceId) throws ResourceNotFoundException {
            Attendances attendance = attendanceRepository.findById(attendanceId).orElseThrow(()->new ResourceNotFoundException("attendance not found on id: "+attendanceId));
        return ResponseEntity.ok().body(attendance);
    }

    @GetMapping("/datecheck/{dateCheck}")
    public ResponseEntity<Attendances> getattendanceByDateCheck(@PathVariable(value = "dateCheck") Date dateCheck) throws ResourceNotFoundException {
        Attendances attendance = attendanceRepository.findByDateCheck(dateCheck);
        if(attendance==null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok().body(attendance);
    }

    @PostMapping("/add")
    public Attendances create(@Validated @RequestBody Attendances Attendances) throws Exception{
        Date dateCheck = Attendances.getDateCheck();
        if(dateCheck!=null&&!"".equals(dateCheck)){
            Attendances tempattendanceName = attendanceRepository.findByDateCheck(dateCheck);
            if(tempattendanceName!=null){
                throw new Exception("attendance date check: "+dateCheck+" is already exist");
            }
        }
        return attendanceRepository.save(Attendances);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Attendances> update(@PathVariable(value = "id") Long attendanceId,
                                              @Validated @RequestBody Attendances attendanceDetails) throws Exception{

        Attendances attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("This attendance not found on:" + attendanceId));

        boolean isDisabled = attendance.isDisabled();
        if(isDisabled==false){
            throw new Exception("This attendance has already been disabled!");
        }

        attendance.setDateCheck(attendanceDetails.getDateCheck());
        attendance.setStatus(attendanceDetails.getStatus());
        attendance.setNote(attendanceDetails.getNote());
        attendance.setWorkingHours(attendanceDetails.getWorkingHours());

        final Attendances updateattendance = attendanceRepository.save(attendanceDetails);

        return ResponseEntity.ok(updateattendance);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Attendances> disable(@PathVariable(value = "id") Long attendanceId) throws Exception{

        Attendances Attendances = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("attendance not found on: " + attendanceId));

        boolean isDisabled = Attendances.isDisabled();
        if(isDisabled==true)
        {
            throw new Exception("attendance has already been disabled!");
        }
        Attendances.setDisabled(true);
        final Attendances updateattendance = attendanceRepository.save(Attendances);

        return ResponseEntity.ok(updateattendance);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Attendances> enable(@PathVariable(value = "id") Long attendanceId) throws Exception{

        Attendances Attendances = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("attendance not found on:" + attendanceId));

        boolean isDisabled = Attendances.isDisabled();
        if(isDisabled==false)
        {
            throw new Exception("attendance has not been disabled yet!");
        }
        Attendances.setDisabled(false);
        final Attendances updateattendance = attendanceRepository.save(Attendances);

        return ResponseEntity.ok(updateattendance);
    }
    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long attendanceId) throws
            Exception {
        Attendances attendance = attendanceRepository.findById(attendanceId).orElseThrow(() -> new ResourceNotFoundException("Attendances not found on: " + attendanceId));
        attendanceRepository.delete(attendance);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
