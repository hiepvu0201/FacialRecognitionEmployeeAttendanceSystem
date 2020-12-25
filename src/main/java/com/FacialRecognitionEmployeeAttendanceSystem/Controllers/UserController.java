package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Shifts;
import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Users;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.DepartmentRepository;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.RoleRepository;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.ShiftRepository;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @GetMapping("/")
    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        Users user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found on id: "+userId));
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/fullname/{fullName}")
    public ResponseEntity<Users> getUserByFullName(@PathVariable(value = "fullName") String fullName) throws ResourceNotFoundException {
        Users user = userRepository.findByFullName(fullName);
        if(user==null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/pin/{pin}")
    public ResponseEntity<Users> getUserByPin(@PathVariable(value = "pin") String userPin) throws ResourceNotFoundException {
        Users user = userRepository.findByPin(userPin);
        if(user==null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/add")
    public Users create(@Validated @RequestBody Users users) throws Exception{
        String userFullName = users.getFullName();
        if(userFullName!=null&&!"".equals(userFullName)){
            Users tempUserFullName = userRepository.findByFullName(userFullName);
            if(tempUserFullName!=null){
                throw new Exception("user name: "+userFullName+" is already exist");
            }
        }

        users.setDepartments(departmentRepository.findById(users.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + users.getDepartmentId())));
        users.setRoles(roleRepository.findById(users.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + users.getRoleId())));
        users.setShifts(shiftRepository.findById(users.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id " + users.getShiftId())));

        return userRepository.save(users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Users> update(@PathVariable(value = "id") Long userId,
                                        @Validated @RequestBody Users userDetails) throws Exception{

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("This user not found on:" + userId));

        boolean isDisabled = user.isDisabled();
        if(isDisabled==false){
            throw new Exception("This user has already been disabled!");
        }

        user.setFullName(userDetails.getFullName());
        user.setPin(userDetails.getPin());
        user.setDob(userDetails.getDob());
        user.setHomeAddress(userDetails.getHomeAddress());
        user.setGrossSalary(userDetails.getGrossSalary());
        user.setNetSalary(userDetails.getNetSalary());
        user.setNote(userDetails.getNote());
        user.setDepartmentId(userDetails.getDepartmentId());
        user.setRoleId(userDetails.getRoleId());
        user.setDepartments(departmentRepository.findById(user.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + user.getDepartmentId())));
        user.setRoles(roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + user.getRoleId())));
        user.setShiftId(userDetails.getShiftId());
        user.setShifts(shiftRepository.findById(user.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id " + user.getShiftId())));

        final Users updateUser = userRepository.save(user);

        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Users> disable(@PathVariable(value = "id") Long userId) throws Exception{

        Users Users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found on: " + userId));

        boolean isDisabled = Users.isDisabled();
        if(isDisabled==true)
        {
            throw new Exception("user has already been disabled!");
        }
        Users.setDisabled(true);
        final Users updateUser = userRepository.save(Users);

        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Users> enable(@PathVariable(value = "id") Long userId) throws Exception{
        Users Users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found on:" + userId));

        boolean isDisabled = Users.isDisabled();
        if(isDisabled==false)
        {
            throw new Exception("user has not been disabled yet!");
        }
        Users.setDisabled(false);
        final Users updateUser = userRepository.save(Users);

        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long userId) throws
            Exception {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found on: " + userId));
        userRepository.delete(user);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
