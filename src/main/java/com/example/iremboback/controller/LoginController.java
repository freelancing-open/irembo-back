package com.example.iremboback.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.iremboback.config.security.JWTConstant;
import com.example.iremboback.config.security.JWTUtil;
import com.example.iremboback.dto.*;
import com.example.iremboback.model.Users;
import com.example.iremboback.service.UserService;
import com.example.iremboback.validation.EmailValidation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.example.iremboback.config.Constant.*;


@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailValidation emailValidation;

    @Autowired
    private JWTConstant config;

    @Autowired
    public RestTemplate restTemplate;

    private ApiError error;
    private ApiResponse response;
    private ApiSuccess success;
    private EmailDto emailDto;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyUser(@RequestBody ApiRequest request) {
        init();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPwd()));
            //user details ===>
            final String jwt = jwtUtil.createToken(request.getEmail());

            success.setCode(HttpStatus.OK.value());
            response.setApiSuccess(success);
            response.setData(jwt);

        } catch (AuthenticationException e) {
            error.setErrorCode(401);
            error.setErrorMessage("Incorrect Credentials");
            response.setApiError(error);
        } catch (Exception e) {
            error.setErrorCode(500);
            error.setErrorMessage("JWT Not Created");
            response.setApiError(error);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(Authentication authentication) {
        error = new ApiError();
        UserDto userDto = new UserDto();
        Optional<Users> users = userService.getUser(authentication.getName());
        if (users.isEmpty()) {
            error.setErrorCode(403);
            error.setErrorMessage("Unauthorized Access");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        BeanUtils.copyProperties(users.get(), userDto);
        userDto.setRole(users.get().getRoleName().getName());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> createUser(@RequestBody Users user){
        init();
        if(emailValidation(user)){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        UserDto userDto = new UserDto();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Users> created = userService.create(user);
        if(created.isPresent()) {
            BeanUtils.copyProperties(created.get(), userDto);
            userDto.setRole(created.get().getRoleName().getName());
            success.setCode(HttpStatus.CREATED.value());
            response.setApiSuccess(success);
            response.setData(userDto);
        }else{
            error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
            error.setErrorMessage("Not Implemented");
            response.setApiError(error);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reset/{email}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> resetPasswordFront(@PathVariable String email, HttpServletRequest request){
        //For Sending Email.
        init();
        String resetPasswordLink = getSiteURL(request) + "/verify-reset?resetToken=";
        Optional<Users> users = userService.getUser(email);
        try{
            if(users.isPresent()){

                String token = jwtUtil.createToken(email);
                resetPasswordLink = resetPasswordLink + token;

                users.get().setToken(token);
                userService.update(users.get());

                emailDto.setEmail(email);
                emailDto.setLink(resetPasswordLink);
                emailDto.setUsername(users.get().getFirstName());

                return getResponseEntity(EMAIL_RESET_PASSWORD_LINK);
            }else{
                error.setErrorCode(404);
                error.setErrorMessage("User Doesn't Exist");
                response.setApiError(error);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
            error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
            error.setErrorMessage("Error Occurred. Try Back!");
            response.setApiError(error);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/verify-reset", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam String resetToken){
        //For Verify if Token has not expired password
        init();
        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getSignatureKey());

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(config.getIssue()).withAudience(config.getAudience())
                    .build(); //Reusable verifier instance

            DecodedJWT jwt = verifier.verify(resetToken);

            Optional<Users> user = userService.getUser(jwt.getSubject());
            if(user.isPresent()){
                success.setCode(HttpStatus.OK.value());
                response.setApiSuccess(success);
                response.setData(true);
            }else{
                error.setErrorCode(404);
                error.setErrorMessage("Incorrect Reset Password Link");
                response.setApiError(error);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
            error.setErrorCode(HttpStatus.NOT_FOUND.value());
            error.setErrorMessage("Link has Expired. Try Back!");
            response.setApiError(error);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/reset/{email}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> resetPasswordBack(@PathVariable String email, @RequestBody PassDto pwd){
        //For Resetting password
        init();
        Optional<Users> users = userService.getUser(email);
        if(users.isPresent()){
            users.get().setPassword(passwordEncoder.encode(pwd.getPassword()));
            userService.update(users.get());

            success.setCode(HttpStatus.OK.value());
            response.setApiSuccess(success);
            response.setData(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        error.setErrorCode(404);
        error.setErrorMessage("User Doesn't Exist");
        response.setApiError(error);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/registration/verification/{email}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> registrationConfirmationFront(@PathVariable String email, HttpServletRequest request){
        //For Sending Email.
        init();
        String verificationLink = getSiteURL(request) + "/registration/"+email+"/verification?code=";
        Optional<Users> users = userService.getUser(email);
        try{
            if(users.isPresent()){

                String code = RANDOM_ALPHANUMERIC();
                verificationLink = verificationLink + code;

                users.get().setVerificationCode(code);
                userService.update(users.get());

                emailDto.setEmail(email);
                emailDto.setLink(verificationLink);

                return getResponseEntity(EMAIL_REGISTRATION_VERIFICATION_LINK);
            }else{
                error.setErrorCode(404);
                error.setErrorMessage("User Doesn't Exist");
                response.setApiError(error);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
            error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
            error.setErrorMessage("Error Occurred. Try Back!");
            response.setApiError(error);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    private ResponseEntity<?> getResponseEntity(String emailServiceLink) {
        Boolean result = restTemplate.postForEntity(emailServiceLink, emailDto, Boolean.class).getBody();

        if(Boolean.TRUE.equals(result)){
            success.setCode(HttpStatus.OK.value());
            response.setApiSuccess(success);
            response.setData("Link Send To Email");
        }else {
            error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
            error.setErrorMessage("Error Occurred. Try Back!");
            response.setApiError(error);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/registration/{email}/verification", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> registrationConfirmationBack(@PathVariable String email, @RequestParam String code){
        //For checking and then changing account to verification
        init();
        Optional<Users> users = userService.getUser(email);
        if(users.isPresent()){
            if(users.get().getVerificationCode().equals(code)){

                users.get().setVerificationCode(RANDOM_ALPHANUMERIC());
                users.get().setVerified(true);
                userService.update(users.get());

                success.setCode(HttpStatus.OK.value());
                response.setApiSuccess(success);
                response.setData(true);
            }else{
                error.setErrorCode(404);
                error.setErrorMessage("Incorrect Registration Link");
                response.setApiError(error);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            error.setErrorCode(404);
            error.setErrorMessage("User Doesn't Exist");
            response.setApiError(error);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/mfa-verification/{email}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> multiFactorAuthenticationFront(@PathVariable String email){
        //For Sending Email.
        init();
        Optional<Users> users = userService.getUser(email);
        if(users.isPresent()){
            Integer otp = RANDOM_NUMBER();

            users.get().setOtp(otp);
            userService.update(users.get());

            emailDto.setEmail(email);
            emailDto.setOtp(otp);

            return getResponseEntity(EMAIL_OTP_LINK);
        }else{
            error.setErrorCode(404);
            error.setErrorMessage("User Doesn't Exist");
            response.setApiError(error);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/mfa-verification/{email}/{code}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> multiFactorAuthenticationBack(@PathVariable String email, @PathVariable Integer code){
        //For validating user to access the system
        init();
        Optional<Users> users = userService.getUser(email);
        if(users.isPresent()){
            if(users.get().getOtp().intValue() == code.intValue()){

                users.get().setOtp(RANDOM_NUMBER());
                userService.update(users.get());

                success.setCode(HttpStatus.OK.value());
                response.setApiSuccess(success);
                response.setData(true);
            }else{
                error.setErrorCode(404);
                error.setErrorMessage("Incorrect OTP Code");
                response.setApiError(error);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            error.setErrorCode(404);
            error.setErrorMessage("User Doesn't Exist");
            response.setApiError(error);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    private void init(){
        error = new ApiError();
        response = new ApiResponse();
        success = new ApiSuccess();
        emailDto = new EmailDto();
    }

    private boolean emailValidation(Users u){
        boolean validationResult = emailValidation.isAlreadyRegister(u.getEmail());
        if(validationResult){
            error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
            error.setErrorMessage("Email Already Exist");
            response.setApiError(error);
            return true;
        }
        return false;
    }


    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "") + AUTH_PATH;
    }
}
