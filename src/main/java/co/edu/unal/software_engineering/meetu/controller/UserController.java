package co.edu.unal.software_engineering.meetu.controller;

import co.edu.unal.software_engineering.meetu.auth.configuration.WebSecurityConfiguration;
import co.edu.unal.software_engineering.meetu.model.Role;
import co.edu.unal.software_engineering.meetu.model.User;
import co.edu.unal.software_engineering.meetu.pojo.RegisterUserPOJO;
import co.edu.unal.software_engineering.meetu.service.RoleService;
import co.edu.unal.software_engineering.meetu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@CrossOrigin
@RestController
public class UserController{

    private final UserService userService;

    private final RoleService roleService;

    private WebSecurityConfiguration webSecurityConfiguration;

    public UserController(UserService userService, RoleService roleService, WebSecurityConfiguration webSecurityConfiguration){
        this.userService = userService;
        this.roleService = roleService;
        this.webSecurityConfiguration = webSecurityConfiguration;
    }

/*
    @Bean
    public PasswordEncoder passwordEncoder( ){
        return new BCryptPasswordEncoder( );
    }
*/

    @PostMapping( value = { "/registro/{roleId}" } )
    public ResponseEntity register(@PathVariable Integer roleId, @RequestBody RegisterUserPOJO userPOJO ){
        Role role = roleService.findById( roleId );
        User existingUser = userService.findByEmail( userPOJO.getEmail( ) );
        boolean correcto = userService.isRightUser(userPOJO);
        if( role == null || existingUser != null || !correcto ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        User newUser = new User( );
        PasswordEncoder passwordEncoder = webSecurityConfiguration.passwordEncoder();
        newUser.setPassword(passwordEncoder.encode(userPOJO.getPassword()));
        newUser.setUsername( userPOJO.getUsername( ).toLowerCase() );
        newUser.setLast_name(userPOJO.getLast_name().toLowerCase());
        newUser.setPhone_number(userPOJO.getPhone_number());
        newUser.setEmail(userPOJO.getEmail().toLowerCase());
        newUser.setCity(userPOJO.getCity().toLowerCase());
        newUser.setRoles( Collections.singletonList( role ) );

        userService.save( newUser );
        return new ResponseEntity( HttpStatus.CREATED );
    }
/*
    @PostMapping( value = { "/registro/nuevo-rol/{roleId}" } )
    public ResponseEntity registerRoleToUser(@PathVariable Integer roleId, @RequestBody LoginUserPOJO userPOJO ){
        Role role = roleService.findById( roleId );
        User existingUser = userService.findByEmail( userPOJO.getEmail( ) );
        if( role == null || existingUser == null || existingUser.getRoles( ).contains( role ) ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }else if( !passwordEncoder.matches( userPOJO.getPassword( ), existingUser.getPassword( ) ) ){
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }
        existingUser.addRole( role );
        userService.save( existingUser );
        return new ResponseEntity( HttpStatus.CREATED );
    }

    @PostMapping( value = { "/login" } )
    public ResponseEntity login(@RequestBody LoginUserPOJO userPOJO ){

        User existingUser = userService.findByEmail( userPOJO.getEmail( ) );

        if (existingUser != null){
            boolean esValido = passwordEncoder.matches(userPOJO.getPassword(), existingUser.getPassword());
            if(esValido){
                return new ResponseEntity(HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

 */
}