package com.test.noverina.transaction.service;

import com.test.noverina.transaction.dto.AuthDto;
import com.test.noverina.transaction.exception.BadLogicException;
import com.test.noverina.transaction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authManager;
    @Value("${jwt.expiration}")
    private long expiration;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public String auth(AuthDto dto) {
        Authentication auth = null;
        try {
            auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadLogicException("Invalid credentials");
        }
        if (auth == null) throw new RuntimeException("Unable to authenticate into Spring Security");
        SecurityContextHolder.getContext().setAuthentication(auth);

        return jwtService.generateToken(dto.getUsername());
    }

}
