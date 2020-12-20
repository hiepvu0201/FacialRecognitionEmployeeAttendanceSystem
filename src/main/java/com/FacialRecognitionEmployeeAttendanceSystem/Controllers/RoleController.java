package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Roles;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public List<Roles> getAllRoles(){
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Roles> getRoleById(@PathVariable(value = "id") Long roleId) throws ResourceNotFoundException{
        Roles role = roleRepository.findById(roleId).orElseThrow(()->new ResourceNotFoundException("Role not found on id: "+roleId));
        return ResponseEntity.ok().body(role);
    }

    @PostMapping("/add")
    public Roles create(@Validated @RequestBody Roles Roles) throws Exception{
        String roleName = Roles.getRoleName();
        if(roleName!=null&&!"".equals(roleName)){
            Roles tempRoleName = roleRepository.findByRoleName(roleName);
            if(tempRoleName!=null){
                throw new Exception("Role name: "+roleName+" is already exist");
            }
        }
        return roleRepository.save(Roles);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Roles> update(@PathVariable(value = "id") Long roleId,
                                               @Validated @RequestBody Roles roleDetails) throws Exception{

        Roles role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("This role not found on:" + roleId));

        role.setRoleName(roleDetails.getRoleName());
        role.setNote(roleDetails.getNote());
        role.setDescription(roleDetails.getDescription());
        role.setSalaryRate(roleDetails.getSalaryRate());

        final Roles updateRole = roleRepository.save(roleDetails);

        return ResponseEntity.ok(updateRole);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Roles> disable(@PathVariable(value = "id") Long roleId) throws Exception{

        Roles Roles = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found on: " + roleId));

        boolean isDisabled = Roles.isDisabled();
        if(isDisabled==true)
        {
            throw new Exception("Role has already been disabled!");
        }
        Roles.setDisabled(true);
        final Roles updateRole = roleRepository.save(Roles);

        return ResponseEntity.ok(updateRole);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Roles> enable(@PathVariable(value = "id") Long roleId) throws Exception{

        Roles Roles = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found on:" + roleId));

        boolean isDisabled = Roles.isDisabled();
        if(isDisabled==false)
        {
            throw new Exception("Role has not been disabled yet!");
        }
        Roles.setDisabled(false);
        final Roles updateRole = roleRepository.save(Roles);

        return ResponseEntity.ok(updateRole);
    }
    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long roleId) throws
            Exception {
        Roles role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found on: " + roleId));
        roleRepository.delete(role);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
