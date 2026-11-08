package com.crypto.casino.auth;

import com.crypto.casino.security.JwtService;
import com.crypto.casino.user.Role;
import com.crypto.casino.user.User;
import com.crypto.casino.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest registerRequest){
        if(userRepository.existsByEmail(
                registerRequest.getEmail().toLowerCase(Locale.ROOT ))){
            throw new EmailAlreadyExistException("User is already registered");
        }
        User user = new User();

        user.setEmail(registerRequest.getEmail().toLowerCase(Locale.ROOT));
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if(passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())){
            LoginResponse loginResponse = new LoginResponse(jwtService
                    .generateToken(user.getEmail()));
            return loginResponse;
        }
        else {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}
