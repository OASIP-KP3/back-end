package sit.int221.oasipservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.payload.response.JwtResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtUtils {
    private final String ROLES = "roles";
    private final String TOKEN_TYPE = "Bearer ";
    private String secretKey;
    private int jwtExpirationInMs;
    private int refreshExpirationInMs;

    @Value("${jwt.secret}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${jwt.expirationInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Value("${jwt.refreshExpirationInMs}")
    public void setRefreshExpirationInMs(int refreshExpirationInMs) {
        this.refreshExpirationInMs = refreshExpirationInMs;
    }

    private DecodedJWT decodedJWT(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        return verifier.verify(token);
    }

    public String getEmail(String token) {
        return decodedJWT(token).getSubject();
    }

    public String[] getRoles(String token) {
        return decodedJWT(token).getClaim(ROLES).asArray(String.class);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return createAccessToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return createRefreshToken(userDetails.getUsername());
    }

    private String createAccessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .withClaim(ROLES, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(secretKey));
    }

    private String createRefreshToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationInMs))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public JwtResponse generateToken(UserDetails userDetails) {
        String tokenType = "Bearer";
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        return new JwtResponse(accessToken, refreshToken, tokenType);
    }

    public boolean isHeaderValid(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        return header != null && header.startsWith(TOKEN_TYPE);
    }

    public boolean isTokenExpired(String token) {
        return decodedJWT(token).getExpiresAt().before(new Date());
    }

    public Instant getExpiresAt(String token) {
        return decodedJWT(token).getExpiresAt().toInstant();
    }

    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        return header.substring(TOKEN_TYPE.length());
    }

    public Set<String> getRoles() {
        return getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public String getEmail() {
        return getAuthentication().getName();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
