package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Roles;
import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Shifts;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
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
@RequestMapping("api/v1/shifts")
public class ShiftController {
    @Autowired
    private ShiftRepository shiftRepository;

    @GetMapping("/")
    public List<Shifts> getAllShifts(){
        return shiftRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shifts> getShiftById(@PathVariable(value = "id") Long shiftId) throws ResourceNotFoundException {
        Shifts shift = shiftRepository.findById(shiftId).orElseThrow(()->new ResourceNotFoundException("shift not found on id: "+shiftId));
        return ResponseEntity.ok().body(shift);
    }

    @PostMapping("/add")
    public Shifts create(@Validated @RequestBody Shifts Shifts) throws Exception{
        String shiftName = Shifts.getShiftName();
        if(shiftName!=null&&!"".equals(shiftName)){
            Shifts tempshiftName = shiftRepository.findByShiftName(shiftName);
            if(tempshiftName!=null){
                throw new Exception("shift name: "+shiftName+" is already exist");
            }
        }
        return shiftRepository.save(Shifts);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Shifts> update(@PathVariable(value = "id") Long shiftId,
                                              @Validated @RequestBody Shifts shiftDetails) throws Exception{

        Shifts shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("This shift not found on:" + shiftId));

        boolean isDisabled = shift.isDisabled();
        if(isDisabled==true){
            throw new Exception("This shift has already been disabled!");
        }

        shift.setShiftName(shiftDetails.getShiftName());
        shift.setTimeStart(shiftDetails.getTimeStart());
        shift.setTimeEnd(shift.getTimeEnd());

        final Shifts updateshift = shiftRepository.save(shiftDetails);

        return ResponseEntity.ok(updateshift);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Shifts> disable(@PathVariable(value = "id") Long shiftId) throws Exception{

        Shifts Shifts = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("shift not found on: " + shiftId));

        boolean isDisabled = Shifts.isDisabled();
        if(isDisabled==true)
        {
            throw new Exception("shift has already been disabled!");
        }
        Shifts.setDisabled(true);
        final Shifts updateshift = shiftRepository.save(Shifts);

        return ResponseEntity.ok(updateshift);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Shifts> enable(@PathVariable(value = "id") Long shiftId) throws Exception{

        Shifts Shifts = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("shift not found on:" + shiftId));

        boolean isDisabled = Shifts.isDisabled();
        if(isDisabled==false)
        {
            throw new Exception("shift has not been disabled yet!");
        }
        Shifts.setDisabled(false);
        final Shifts updateshift = shiftRepository.save(Shifts);

        return ResponseEntity.ok(updateshift);
    }
    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long shiftId) throws
            Exception {
        Shifts shift = shiftRepository.findById(shiftId).orElseThrow(() -> new ResourceNotFoundException("Shift not found on: " + shiftId));
        shiftRepository.delete(shift);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
