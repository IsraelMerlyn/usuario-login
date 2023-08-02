package com.josuevqz.users.usuarioslogin.controllers;

import com.josuevqz.users.usuarioslogin.models.User;
import com.josuevqz.users.usuarioslogin.models.dto.UserDTO;
import com.josuevqz.users.usuarioslogin.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping()
@CrossOrigin(originPatterns = "*")
public class UserControllers {
    @Autowired
    private UserServices services;
    @GetMapping("/users")
    public List<UserDTO> list(){
        return  services.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> show(@PathVariable  Long id){
      Optional<UserDTO> userOptional = services.findByID(id);
      if(userOptional.isPresent()) {
          return ResponseEntity.ok(userOptional.orElseThrow());
      }
      return  ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<?> create( @RequestBody @Valid User user, BindingResult result){
       if(result.hasErrors()){
           return validation(result);
       }
       return  ResponseEntity.status(HttpStatus.CREATED).body(services.save(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update( @RequestBody @Valid User user,BindingResult result,@PathVariable Long id){
        if(result.hasErrors()){
            return validation(result);
        }
        Optional<UserDTO> o= services.update(user, id);

        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/users/{id}")
    public  ResponseEntity<?> remove(@PathVariable Long id){
        Optional<UserDTO> o=services.findByID(id);
        if (o.isPresent()){
            services.remove(id);
            return ResponseEntity.noContent().build(); // respuesta 204
        }
       return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String,String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(),"El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
