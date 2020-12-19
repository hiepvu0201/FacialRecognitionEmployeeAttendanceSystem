package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Payslips;
import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Users;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.PayslipRepository;
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
@RequestMapping("api/v1/payslips")
public class PayslipController {
    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public List<Payslips> getAllPayslips(){
        return payslipRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payslips> getpayslipById(@PathVariable(value = "id") Long payslipId) throws ResourceNotFoundException {
        Payslips payslip = payslipRepository.findById(payslipId).orElseThrow(()->new ResourceNotFoundException("payslip not found on id: "+payslipId));
        return ResponseEntity.ok().body(payslip);
    }

    @GetMapping("/datecheck/{dateTime}")
    public ResponseEntity<List<Payslips>> getAllPayslipByPayDate(@PathVariable(value = "dateTime") Date payDate) throws Exception {
        List<Payslips> payslip = payslipRepository.findAllByPayDate(payDate);
        if (payslip==null){
            throw new Exception("Payslip not found at date: "+payDate);
        }
        return ResponseEntity.ok().body(payslip);
    }

    @PostMapping("/add")
    public Payslips create(@Validated @RequestBody Payslips Payslips) throws Exception{
        Date daypay = Payslips.getPayDate();
        if(daypay!=null&&!"".equals(daypay)){
            Payslips temppayslipName = payslipRepository.findByPayDate(daypay);
            if(temppayslipName!=null){
                throw new Exception("payslip date check: "+daypay+" is already exist");
            }
        }
        Payslips.setUsers(userRepository.findById(Payslips.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + Payslips.getUserId())));
        return payslipRepository.save(Payslips);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Payslips> update(@PathVariable(value = "id") Long payslipId,
                                              @Validated @RequestBody Payslips payslipDetails) throws Exception{

        Payslips payslip = payslipRepository.findById(payslipId)
                .orElseThrow(() -> new ResourceNotFoundException("This payslip not found on:" + payslipId));

        boolean isDisabled = payslip.isDisabled();
        if(isDisabled==false){
            throw new Exception("This payslip has already been disabled!");
        }

        payslip.setPayDate(payslipDetails.getPayDate());
        payslip.setWorkingSalary(payslipDetails.getWorkingSalary());
        payslip.setPublicSalary(payslipDetails.getPublicSalary());
        payslip.setOtherSalary(payslipDetails.getOtherSalary());
        payslip.setAnnualLeaveSalary(payslipDetails.getAnnualLeaveSalary());
        payslip.setOvertimeSalary(payslipDetails.getOvertimeSalary());
        payslip.setAllowance(payslipDetails.getAllowance());
        payslip.setBonus(payslipDetails.getBonus());
        payslip.setTax(payslipDetails.getTax());
        payslip.setDeductionSalary(payslipDetails.getDeductionSalary());

        final Payslips updatepayslip = payslipRepository.save(payslipDetails);

        return ResponseEntity.ok(updatepayslip);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Payslips> disable(@PathVariable(value = "id") Long payslipId) throws Exception{

        Payslips Payslips = payslipRepository.findById(payslipId)
                .orElseThrow(() -> new ResourceNotFoundException("payslip not found on: " + payslipId));

        boolean isDisabled = Payslips.isDisabled();
        if(isDisabled==true)
        {
            throw new Exception("payslip has already been disabled!");
        }
        Payslips.setDisabled(true);
        final Payslips updatepayslip = payslipRepository.save(Payslips);

        return ResponseEntity.ok(updatepayslip);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Payslips> enable(@PathVariable(value = "id") Long payslipId) throws Exception{

        Payslips Payslips = payslipRepository.findById(payslipId)
                .orElseThrow(() -> new ResourceNotFoundException("payslip not found on:" + payslipId));

        boolean isDisabled = Payslips.isDisabled();
        if(isDisabled==false)
        {
            throw new Exception("payslip has not been disabled yet!");
        }
        Payslips.setDisabled(false);
        final Payslips updatepayslip = payslipRepository.save(Payslips);

        return ResponseEntity.ok(updatepayslip);
    }
    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long payslipId) throws
            Exception {
        Payslips payslip = payslipRepository.findById(payslipId).orElseThrow(() -> new ResourceNotFoundException("Payslip not found on: " + payslipId));
        payslipRepository.delete(payslip);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
