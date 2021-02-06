package com.vit.community.springapplication.controller;

import com.vit.community.springapplication.dto.AuthenticationResponse;
import com.vit.community.springapplication.dto.LoginRequest;
import com.vit.community.springapplication.dto.RefreshTokenRequest;
import com.vit.community.springapplication.dto.RegisterRequest;
import com.vit.community.springapplication.service.AuthService;
import com.vit.community.springapplication.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

/*
* The AuthController class handles all the HTTP GET/POST requests related
* to signup, login and token verification.
*
* The Lombok library generates the all arguments constructor, and the fields
* are automatically autowired (initialised) by Spring at runtime.
*
* */

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    /* The signup POST API call should contain the request body which is of type RegisterRequest.
    Through this class we are transferring the user details like username, password and email
    as part of the RequestBody. We call this kind of classes as a DTO (Data Transfer Object).

    Once signup is completed, user gets a OK response.
    */

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                OK);
    }

    /*
    * The accountVerification API call contains the token as a path variable which is
    * validated against the token and the corresponding user in the database followed by
    * which the user is enabled.
    * */

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    /*
    * The login POST API call contains the RequestBody which is of type LoginRequest
    * which is a DTO that encapsulates the username and password entered by user,
    * and delegates the login functionality to AuthService.
    * */

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /*
    * Whenever the JWT gets expired or about to be expired, the client makes a POST API call to the server.
    * This method uses the AuthService class to validate the refresh token, generate a new JWT token, and
    * return it to the client.
    * */

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    /*
    * The POST API call to logout from the website. This method uses the RefreshTokenService to
    * delete the refresh token from the database when user logs out so that new JWT will not be
    * generated.
    * */

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
