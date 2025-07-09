package port.management.system.security.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import port.management.system.model.AppRole;
import port.management.system.model.User;
import port.management.system.repository.UserRepository;
import port.management.system.security.dto.LoginDTO;
import port.management.system.security.dto.SignupDTO;
import port.management.system.security.jwt.JwtService;
import port.management.system.security.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.app.adminSecretKey}")
    private String adminSecretKey;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(
            @RequestBody LoginDTO request,
            HttpServletResponse response
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24 Hours

        response.addCookie(cookie);

        return new ResponseEntity<>("Login Successful!", HttpStatus.OK);

    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> userSignup(@RequestBody SignupDTO request, HttpServletResponse response) {

        AppRole roleToAssign = AppRole.ROLE_USER;

        if (adminSecretKey.equals(request.getSecretKey())) {
            roleToAssign = AppRole.ROLE_ADMIN;
        }

        // Check for duplicates
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        if (userRepository.findByMobile(request.getMobile()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Mobile number already exists");
        }

        // Save the user
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleToAssign)
                .build();

        userRepository.save(newUser);

        System.out.println("User registered: " + request.getUsername() + " as " + roleToAssign.name());

        return new ResponseEntity<>(
                "User Registered Successfully as " + roleToAssign.name(),
                HttpStatus.OK
        );

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }

}
