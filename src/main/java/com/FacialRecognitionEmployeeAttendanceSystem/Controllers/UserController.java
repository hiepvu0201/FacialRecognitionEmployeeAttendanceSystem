package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Users;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.DepartmentRepository;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.RoleRepository;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/")
    public List<Users> getAllRoles(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getRoleById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        Users user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found on id: "+userId));
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
        user.setImgPath(userDetails.getImgPath());
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

        final Users updateUser = userRepository.save(userDetails);

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
        Users.setDisabled(false);
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
}
