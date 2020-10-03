package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Payslips;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.PayslipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/payslips")
public class PayslipController {
    @Autowired
    private PayslipRepository payslipRepository;

    @GetMapping("/")
    public List<Payslips> getAllPayslips(){
        return payslipRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payslips> getpayslipById(@PathVariable(value = "id") Long payslipId) throws ResourceNotFoundException {
        Payslips payslip = payslipRepository.findById(payslipId).orElseThrow(()->new ResourceNotFoundException("payslip not found on id: "+payslipId));
        return ResponseEntity.ok().body(payslip);
    }

    @PostMapping("/add")
    public Payslips create(@Validated @RequestBody Payslips Payslips) throws Exception{
        Date daypay = Payslips.getPayDate();
        if(daypay!=null&&!"".equals(daypay)){
            Payslips temppayslipName = payslipRepository.findByPayDay(daypay);
            if(temppayslipName!=null){
                throw new Exception("payslip date check: "+daypay+" is already exist");
            }
        }
        return payslipRepository.save(Payslips);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Payslips> update(@PathVariable(value = "id") Long payslipId,
                                              @Validated @RequestBody Payslips payslipDetails) throws Exception{

        Payslips payslip = payslipRepository.findById(payslipId)
                .orElseThrow(() -> new ResourceNotFoundException("This payslip not found on:" + payslipId));

        boolean isActive = payslip.isActive();
        if(isActive==false){
            throw new Exception("This payslip has already been disabled!");
        }

        final Payslips updatepayslip = payslipRepository.save(payslipDetails);

        return ResponseEntity.ok(updatepayslip);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Payslips> disable(@PathVariable(value = "id") Long payslipId) throws Exception{

        Payslips Payslips = payslipRepository.findById(payslipId)
                .orElseThrow(() -> new ResourceNotFoundException("payslip not found on: " + payslipId));

        boolean isActive = Payslips.isActive();
        if(isActive==false)
        {
            throw new Exception("payslip has already been disabled!");
        }
        Payslips.setActive(false);
        final Payslips updatepayslip = payslipRepository.save(Payslips);

        return ResponseEntity.ok(updatepayslip);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Payslips> enable(@PathVariable(value = "id") Long payslipId) throws Exception{

        Payslips Payslips = payslipRepository.findById(payslipId)
                .orElseThrow(() -> new ResourceNotFoundException("payslip not found on:" + payslipId));

        boolean isActive = Payslips.isActive();
        if(isActive==true)
        {
            throw new Exception("payslip has not been disabled yet!");
        }
        Payslips.setActive(true);
        final Payslips updatepayslip = payslipRepository.save(Payslips);

        return ResponseEntity.ok(updatepayslip);
    }
}
