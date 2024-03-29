package sit.int221.oasipservice.configurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sit.int221.oasipservice.filters.JwtAuthEntryPoint;
import sit.int221.oasipservice.filters.JwtRequestFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static sit.int221.oasipservice.entities.ERole.*;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.exceptionHandling()
                .authenticationEntryPoint(authEntryPoint);
        http.sessionManagement()
                .sessionCreationPolicy(STATELESS);
        http.authorizeRequests()
                .antMatchers("/api/v2/auth/**").permitAll()
                .antMatchers(POST, "/api/v2/events").permitAll()
                .antMatchers(GET, "/api/v2/categories", "/api/v2/categories/{id}", "/api/v2/categories/{id}/events").permitAll()
                .antMatchers("/api/v2/users/**", "/api/v2/categories/**")
                .hasAuthority(ROLE_ADMIN.getRole())
                .antMatchers(GET, "/api/v2/events", "/api/v2/events/{id}")
                .hasAnyAuthority(ROLE_ADMIN.getRole(), ROLE_LECTURER.getRole(), ROLE_STUDENT.getRole())
                .antMatchers("/api/v2/events/**")
                .hasAnyAuthority(ROLE_ADMIN.getRole(), ROLE_STUDENT.getRole())
                .anyRequest().authenticated();
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
