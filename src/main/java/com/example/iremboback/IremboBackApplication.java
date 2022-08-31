package com.example.iremboback;

import com.example.iremboback.model.Role;
import com.example.iremboback.model.Users;
import com.example.iremboback.service.RoleService;
import com.example.iremboback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
@EnableEurekaClient
public class IremboBackApplication implements ApplicationRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(IremboBackApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args)  {
        if(roleService.getAllRoles().isEmpty()){
            roleService.create(new Role("USER", "Normal User of the Software"));
            roleService.create(new Role("ADMIN", "Administrator User of the Software"));
            System.out.println("Roles Created Successful");
        }
        boolean adminNotExist = userService.getUsers().stream().filter(user -> user.getRoleName().getName().equals("ADMIN")).findFirst().isEmpty();
        if(adminNotExist){
            userService.createAdmin(new Users("Tester", "AD", "tester@admin.com", passwordEncoder.encode("pass@#"), "M", new Date(), "Single", "Rwanda", "java.png"));
            userService.createAdmin(new Users("Tester_Assist", "AD", "testerASS@admin.com",  passwordEncoder.encode("pass@#1"), "F", new Date(), "Single", "Rwanda", "java.png"));
            System.out.println("Administrators Created");
        }
    }
}
