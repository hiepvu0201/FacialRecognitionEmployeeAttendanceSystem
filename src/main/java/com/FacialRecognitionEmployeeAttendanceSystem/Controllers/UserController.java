package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Users;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
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
    public Users create(@Validated @RequestBody Users Users) throws Exception{
        String userFullName = Users.getFullName();
        if(userFullName!=null&&!"".equals(userFullName)){
            Users tempUserFullName = userRepository.findByUserFullName(userFullName);
            if(tempUserFullName!=null){
                throw new Exception("user name: "+userFullName+" is already exist");
            }
        }
        return userRepository.save(Users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Users> update(@PathVariable(value = "id") Long userId,
                                        @Validated @RequestBody Users userDetails) throws Exception{

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("This user not found on:" + userId));

        boolean isActive = user.isActive();
        if(isActive==false){
            throw new Exception("This user has already been disabled!");
        }

        final Users updateUser = userRepository.save(userDetails);

        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Users> disable(@PathVariable(value = "id") Long userId) throws Exception{

        Users Users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found on: " + userId));

        boolean isActive = Users.isActive();
        if(isActive==false)
        {
            throw new Exception("user has already been disabled!");
        }
        Users.setActive(false);
        final Users updateUser = userRepository.save(Users);

        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Users> enable(@PathVariable(value = "id") Long userId) throws Exception{

        Users Users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found on:" + userId));

        boolean isActive = Users.isActive();
        if(isActive==true)
        {
            throw new Exception("user has not been disabled yet!");
        }
        Users.setActive(true);
        final Users updateUser = userRepository.save(Users);

        return ResponseEntity.ok(updateUser);
    }
}
