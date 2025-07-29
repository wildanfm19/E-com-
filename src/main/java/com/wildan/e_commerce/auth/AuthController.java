package com.wildan.e_commerce.auth;

import com.wildan.e_commerce.model.User;
import com.wildan.e_commerce.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    private final AuthUtil authUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        AuthenticationResponse response = authenticationService.register(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request
    ){
        AuthenticationResponse response = authenticationService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/demo")
    public String demo(){
        return "halo berhasil";
    }

    @GetMapping("/user/email")
    public ResponseEntity<String> getEmail(){
        String email = authUtil.loggedInEmail();;
        return new ResponseEntity<>(email , HttpStatus.OK);
    }

    @GetMapping("/user/id")
    public ResponseEntity<Long> getUserId(){
       Long userId = authUtil.loggedInUserId();
        return new ResponseEntity<>(userId , HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(){
        User user = authUtil.loggedInUser();;
        return new ResponseEntity<>(user , HttpStatus.OK);
    }
}
