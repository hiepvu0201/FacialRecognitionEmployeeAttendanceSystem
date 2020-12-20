package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Attendances;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.AttendanceRepository;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.ShiftRepository;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShiftRepository shiftRepository;

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
    public ResponseEntity<List<Attendances>> getAllAttendanceByDateCheck(@PathVariable(value = "dateCheck") Date dateCheck) throws ResourceNotFoundException {
        List<Attendances> attendance = attendanceRepository.findAllByDateCheck(dateCheck);
        if(attendance==null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok().body(attendance);
    }

    @GetMapping("/userId/dateCheck")
    public ResponseEntity<Attendances> getAttendanceByDateCheckForUser(@PathVariable(value = "dateCheck") Date dateCheck,
                                                                       @PathVariable(value = "userId") long userId) throws ResourceNotFoundException {
        Attendances attendance = attendanceRepository.findByDateCheckAndUserId(dateCheck, userId);
        if(attendance==null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok().body(attendance);
    }

    @PostMapping("/add")
    public Attendances create(@Validated @RequestBody Attendances attendances) throws Exception{
        Date dateCheck = attendances.getDateCheck();
        if(dateCheck!=null&&!"".equals(dateCheck)){
            List<Attendances> tempListAttendances = attendanceRepository.findAllByDateCheck(dateCheck);
            if(tempListAttendances!=null){
                throw new Exception("attendance date check: "+dateCheck+" is already exist");
            }
        }

        attendances.setShifts(shiftRepository.findById(attendances.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id " + attendances.getShiftId())));
        attendances.setUsers(userRepository.findById(attendances.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + attendances.getUserId())));
        return attendanceRepository.save(attendances);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Attendances> update(@PathVariable(value = "id") Long attendanceId,
                                              @Validated @RequestBody Attendances attendanceDetails) throws Exception{

        Attendances attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("This attendance not found on:" + attendanceId));

        boolean isDisabled = attendance.isDisabled();
        if(isDisabled==true){
            throw new Exception("This attendance has been disabled!");
        }

        attendance.setDateCheck(attendanceDetails.getDateCheck());
        attendance.setStatus(attendanceDetails.getStatus());
        attendance.setNote(attendanceDetails.getNote());
        attendance.setWorkingHours(attendanceDetails.getWorkingHours());
        attendance.setUserId(attendanceDetails.getUserId());
        attendance.setShiftId(attendanceDetails.getShiftId());
        attendance.setShifts(shiftRepository.findById(attendance.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id " + attendance.getShiftId())));
        attendance.setUsers(userRepository.findById(attendance.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + attendance.getUserId())));

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

    //Roll up
    @PostMapping("/checkin")
    public Attendances checkin(@Validated @RequestBody Attendances attendances) throws Exception{
        boolean isDisabled = attendances.isDisabled();
        if(isDisabled==true)
        {
            throw new Exception("Attendance has been disabled!");
        }

        List<Attendances> tempListAttendace = attendanceRepository.findAllByDateCheck(attendances.getDateCheck());
        if(tempListAttendace==null) {
            attendances.setUsers(userRepository.findById(attendances.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + attendances.getUserId())));

            return attendanceRepository.save(attendances);
        }
        return null;
    }

    @PutMapping("/checkout/{id}")
    public ResponseEntity<Attendances> checkout(@PathVariable(value = "id") Long attendanceId,
                                              @Validated @RequestBody Attendances attendanceDetails) throws Exception{

        Attendances attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("This attendance not found on:" + attendanceId));

        boolean isDisabled = attendanceDetails.isDisabled();
        if(isDisabled==true)
        {
            throw new Exception("Attendance has been disabled!");
        }

        List<Attendances> tempListAttendance = attendanceRepository.findAllByDateCheck(attendanceDetails.getDateCheck());
        if(tempListAttendance.size()>0) {
            throw new Exception("User has not checked in yet!");
        }

        attendance.setStatus(true);
        attendance.setWorkingHours(attendanceDetails.getWorkingHours());
        attendance.setCheckoutAt(attendanceDetails.getCheckoutAt());

        final Attendances updateattendance = attendanceRepository.save(attendance);

        return ResponseEntity.ok(updateattendance);
    }
}
