package sit.int221.oasipservice.filters;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sit.int221.oasipservice.utils.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (matchPath(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            if (jwtUtils.isHeaderValid(request)) {
                String token = jwtUtils.getToken(request);
                try {
                    String email = jwtUtils.getEmail(token);
                    String[] roles = jwtUtils.getRoles(token);
                    setAuthentication(email, roles);
                    filterChain.doFilter(request, response);
                } catch (TokenExpiredException | JWTDecodeException | IllegalArgumentException |
                         SignatureVerificationException e) {
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
        Collection<GrantedAuthority> authorities = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        SecurityContextHolder
                .getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(email, null, authorities));
    }

    private boolean matchPath(String path) {
        final String AUTH_PATH = "/api/v2/auth/";
        final String MATCH_PATH = "/api/v2/users/match";
        final String POST_EVENTS_PATH = "/api/v2/events";
        return path.startsWith(AUTH_PATH) || path.equals(MATCH_PATH) || path.equals(POST_EVENTS_PATH);
    }
}
