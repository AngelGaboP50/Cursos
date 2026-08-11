package com.example.demo.security;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration public class SecurityConfig  {
    @Bean SecurityFilterChain chain(HttpSecurity http,JwtAuthenticationFilter jwt,RestAuthenticationEntryPoint ep,RestAccessDeniedHandler dh)throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(e->e.authenticationEntryPoint(ep).accessDeniedHandler(dh)).authorizeHttpRequests(a->a.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll().requestMatchers("/internal/**","/actuator/health","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll().requestMatchers(HttpMethod.GET,"/api/courses/*/reviews").permitAll().requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated()).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
