package sit.int221.oasipservice.filters;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sit.int221.oasipservice.utils.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION);
        final String START_HEADER = "Bearer ";
        if (header != null && header.startsWith(START_HEADER)) {
            String token = header.substring(START_HEADER.length());
            try {
                String email = jwtUtil.getEmail(token);
                String[] roles = jwtUtil.getRoles(token);
                setAuthentication(email, roles);
            } catch (TokenExpiredException | JWTDecodeException | IllegalArgumentException e) {
                log.error(e.getMessage());
                response.setHeader("Error", e.getMessage());
            }
        } else {
            String errorMessage = "Invalid token or missing authorization header";
            log.error(errorMessage);
            response.setHeader("Error", errorMessage);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String email, String[] roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
