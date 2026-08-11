package com.example.demo.security;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration public class SecurityConfig  {
    @Bean SecurityFilterChain chain(HttpSecurity http,JwtAuthenticationFilter jwt,AuthenticationProvider provider,RestAuthenticationEntryPoint ep,RestAccessDeniedHandler dh)throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(e->e.authenticationEntryPoint(ep).accessDeniedHandler(dh)).authorizeHttpRequests(a->a.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll().requestMatchers(HttpMethod.POST,"/api/auth/register","/api/auth/login").permitAll().requestMatchers("/internal/**","/actuator/health","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll().requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated()).authenticationProvider(provider).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean AuthenticationProvider provider(CustomUserDetailsService uds,PasswordEncoder pe) {
        DaoAuthenticationProvider p=new DaoAuthenticationProvider(uds);
        p.setPasswordEncoder(pe);
        return p;
    }
    @Bean PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration c)throws Exception {
        return c.getAuthenticationManager();
    }
}
