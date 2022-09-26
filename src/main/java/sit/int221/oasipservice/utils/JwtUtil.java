package sit.int221.oasipservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sit.int221.oasipservice.payload.response.JwtResponse;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final String ROLES = "roles";
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
        return createRefreshToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    private String createAccessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .withClaim(ROLES, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(secretKey));
    }

    private String createRefreshToken(String subject, Collection<? extends GrantedAuthority> authorities) {
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
}
