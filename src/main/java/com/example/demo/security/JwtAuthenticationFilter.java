package com.example.demo.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
@Component public class JwtAuthenticationFilter extends OncePerRequestFilter  {
    private final JwtTokenService tokens;
    public JwtAuthenticationFilter(JwtTokenService tokens) {
        this.tokens=tokens;
    }
    @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException {
        String h=req.getHeader("Authorization");
        if(StringUtils.hasText(h)&&h.startsWith("Bearer ")&&SecurityContextHolder.getContext().getAuthentication()==null) {
            try {
                UserPrincipal p=tokens.parse(h.substring(7));
                var a=new UsernamePasswordAuthenticationToken(p,null,List.of(new SimpleGrantedAuthority("ROLE_"+p.role())));
                a.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(a);
            }
            catch(RuntimeException ex) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(req,res);
    }
}
