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
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (matchPath(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            if (isHeaderValid(request)) {
                String token = getToken(request);
                try {
                    String email = jwtUtil.getEmail(token);
                    String[] roles = jwtUtil.getRoles(token);
                    setAuthentication(email, roles);
                    filterChain.doFilter(request, response);
                } catch (TokenExpiredException | JWTDecodeException | IllegalArgumentException e) {
                    log.error(e.getMessage());
                    response.setStatus(SC_UNAUTHORIZED);
                    response.setHeader("Error", e.getMessage());
                }
            } else {
                String errorMessage = "Invalid token or missing authorization header";
                log.error(errorMessage);
                response.setHeader("Error", errorMessage);
                filterChain.doFilter(request, response);
            }
        }
    }

    private void setAuthentication(String email, String[] roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean matchPath(String path) {
        final String AUTH_PATH = "/api/v2/auth/";
        final String MATCH_PATH = "/api/v2/users/match";
        final String REFRESH_TOKEN_PATH = "/api/v2/users/token/refresh";
        return path.startsWith(AUTH_PATH) || path.equals(MATCH_PATH) || path.equals(REFRESH_TOKEN_PATH);
    }

    private boolean isHeaderValid(HttpServletRequest request) {
        final String START_HEADER = "Bearer ";
        String header = request.getHeader(AUTHORIZATION);
        return header != null && header.startsWith(START_HEADER);
    }

    private String getToken(HttpServletRequest request) {
        final String START_HEADER = "Bearer ";
        String header = request.getHeader(AUTHORIZATION);
        return header.substring(START_HEADER.length());
    }
}
