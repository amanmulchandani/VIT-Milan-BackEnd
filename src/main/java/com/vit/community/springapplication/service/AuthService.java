package com.vit.community.springapplication.service;

import com.vit.community.springapplication.dto.AuthenticationResponse;
import com.vit.community.springapplication.dto.LoginRequest;
import com.vit.community.springapplication.dto.RefreshTokenRequest;
import com.vit.community.springapplication.dto.RegisterRequest;
import com.vit.community.springapplication.exceptions.SpringCommunityException;
import com.vit.community.springapplication.model.NotificationEmail;
import com.vit.community.springapplication.model.User;
import com.vit.community.springapplication.model.VerificationToken;
import com.vit.community.springapplication.repository.UserRepository;
import com.vit.community.springapplication.repository.VerificationTokenRepository;
import com.vit.community.springapplication.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/*
* The AuthService class contains the main business logic to register the users,
* generate verification token, verify the user account and logging in the user.
*
* It also handles the beginning and termination of transactions with the relational
* database, which is specified using the @Transactional annotation.
*
* The Lombok library generates the all arguments constructor, and the fields
* are automatically autowired (initialised) by Spring at runtime.
* */

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final MailContentBuilder mailContentBuilder;

    /*
    The signup method maps the RegisterRequest object to a new User object and also
    calls the encodePassword() method before setting the password. This method is using
    the BCryptPasswordEncoder to encode our password. After that, the user is saved into
    the database, a verification token is generated and sent as part of an email to the
    user. Note that the enabled flag is set as false, as the user is disabled
    after registration, and enabled only after verifying the user’s email address.
    * */

    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        if(registerRequest.getPassword() != null && !registerRequest.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        }
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build("Thank you for signing up for VIT Community, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token);

        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), message));
    }

    /*
    * Method to get the current logged in user authenticated in the security context.
    * Used for constructing Post, Comment and Vote objects.
    * */

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    /*
    * This method generates a random 128-bit string token which is assigned to
    * a new VerificationToken object along with the user details, and then saved
    * onto the database.
    * */

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    /*
    * If the verification token exists in database, it will enable the user associated with that token,
    * else it will throw an exception.
    * */

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringCommunityException("Invalid Token")));
    }

    /*
     * If the user associated with the token exists in database, it will enable the user,
     * else it will throw an exception.
     * */

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringCommunityException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    /*
    * The login method creates a UsernamePasswordAuthenticationToken object to encapsulate the username and password entered by user and
    * is authenticated by the AuthenticationManager interface provided by Spring Security.
    * Further, stores the authentication details of the user in the SecurityContext. This helps in checking whether user is logged in or not.
    *
    * The AuthenticationManager interface interacts with the UserDetailsService interface for database authentication.
    * Once the credentials are authenticated, a JWT token is created and sent back to the user as an AuthenticationResponse.
    *
    * */

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    /*
    * Validates the refresh token contained in the RefreshTokenRequest using the RefreshTokenService class
    * and if successful generates a new JWT token using the JwtProvider class and returns it as a
    * AuthenticationResponse object back to the controller.
    * */

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}