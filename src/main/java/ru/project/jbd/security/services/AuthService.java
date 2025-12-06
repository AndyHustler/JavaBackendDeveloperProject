package ru.project.jbd.security.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.project.jbd.domain.model.ERole;
import ru.project.jbd.domain.model.User;
import ru.project.jbd.exception.TokenRefreshException;
import ru.project.jbd.repository.UserRepository;
import ru.project.jbd.security.jwt.JwtUtils;
import ru.project.jbd.security.jwt.RefreshToken;
import ru.project.jbd.security.payload.request.LoginRequest;
import ru.project.jbd.security.payload.request.SignupRequest;
import ru.project.jbd.security.payload.response.MessageResponse;
import ru.project.jbd.security.payload.response.AutheticatedResponse;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private Set<ERole> roles;

    public ResponseEntity<?> authentication(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("#authentication UserDetails = " + userDetails);

        User user = userRepository.findById(userDetails.getId()).get();
        log.info("#authentication User = " + user);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
        log.info("Generating refresh token by id = " + userDetails.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
        log.info("#authentication jwtCookie = " + jwtCookie.toString());
        log.info("#authentication jwtRefreshCookie = " + jwtRefreshCookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new AutheticatedResponse(userDetails.getId(),
                                            userDetails.getEmail(),
                                            roles,
                                            jwtUtils.generateToken(userDetails.getEmail(), userDetails.getAuthorities().toString()),
                                            refreshToken.toString()
                                        ));
    }

    public ResponseEntity<?> registration(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Пользователь с таким именем уже существует!"));
        }

        if (userRepository.existsByEmail(signUpRequest.username())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Пользователь с таким email уже существует!"));
        }

        String strRoles = signUpRequest.role();

        switch (strRoles) {
            case "admin":
                roles = ERole.admin();
                break;
            case "user":
                roles = ERole.user();
                break;
            default:
                roles = null;
        }

        User user = User.builder()
                        .userName(signUpRequest.username())
                        .email(signUpRequest.email())
                        .password(encoder.encode(signUpRequest.password()))
                        .role(roles)
                        .build();
        userRepository.save(user);
        log.info("New user: " + user.toString());
        return ResponseEntity.ok(new MessageResponse("Регистрация пользователя прошла успешно!"));
    }

    public ResponseEntity<?> logout() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle.toString() != "anonymousUser") {      
        Long userId = ((UserDetailsImpl) principle).getId();
        refreshTokenService.deleteByUserId(userId);
        }
        
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
            .body(new MessageResponse("Вы вышли из приложения!"));
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
        
        if ((refreshToken != null) && (refreshToken.length() > 0)) {
        return refreshTokenService.findByToken(refreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
                
                return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new MessageResponse("Token успешно обновлен!"));
            })
            .orElseThrow(() -> new TokenRefreshException(refreshToken,
                "Refresh token is not in database!"));
        }
        
        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }
}
