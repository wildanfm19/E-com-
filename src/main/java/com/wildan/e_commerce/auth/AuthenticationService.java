package com.wildan.e_commerce.auth;


public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse login(LoginRequest request);
}
