package ru.project.jbd.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.MacAlgorithm;
import ru.project.jbd.domain.model.User;
import ru.project.jbd.security.services.UserDetailsImpl;
import ru.project.jbd.domain.service.UserService;

@Log4j2
@Component
public class JwtUtils {
    
    @Autowired
    private UserService userService;

    @Value("${app.path.token.sign}")
    private String PATH_SIGN_TOKEN;

    @Value("${app.path.token.refresh}")
    private String PATH_REFRESH_TOKEN;

    @Value("${app.jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${app.jwt.jwtCookieName}")
    private String jwtCookieName;
    
    @Value("${app.jwt.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    private MacAlgorithm alg = Jwts.SIG.HS512;

    private SecretKey key = alg.key().build();

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateToken(userPrincipal.getUsername(), userPrincipal.getAuthorities().toString());   
        return generateCookie(jwtCookieName, jwt, PATH_SIGN_TOKEN);
    }
      
    public ResponseCookie generateJwtCookie(User user) {
        String jwt = generateToken(user.getEmail(), user.getRoles().toString());   
        return generateCookie(jwtCookieName, jwt, PATH_SIGN_TOKEN);
    }
    
    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(jwtRefreshCookie, refreshToken, PATH_REFRESH_TOKEN);
    }
    
    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookieName);
    }
    
    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, null).path(PATH_SIGN_TOKEN).build();
        return cookie;
    }
    
    public ResponseCookie getCleanJwtRefreshCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtRefreshCookie, null).path(PATH_REFRESH_TOKEN).build();
        return cookie;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public User getUserFromJwtToken(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);
        if (jwt.equals(null) || !validateJwtToken(jwt)) return null;
        String email = getUserNameFromJwtToken(jwt);
        return userService.getByEmail(email);
    }
    
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
    
    public String generateToken(String email, String roles) {   
        return Jwts.builder()
            .claims()
            .add("roles", roles)
            .and()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key, alg)
            .compact();
    }
        
    private ResponseCookie generateCookie(String name, String value, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value).path(path).maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }
    
    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
}
