package ed.service.messaging.Filters;

import ed.service.messaging.services.JWTService;
import ed.service.messaging.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    public JwtRequestFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Extract the JWT from the Authorization header
        String header = request.getHeader("Authorization");
        String token = TokenUtil.extractToken(header);

        try{
            if (token != null && jwtService.validateToken(token)) {
                // Extract claims from the token
                Claims claims = jwtService.extractClaims(token);

                // Create an Authentication object
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        jwtService.getAuthorities(claims)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        }catch (SignatureException e){
            e.printStackTrace();

            // Token validation failed, send a 401 Unauthorized response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }
    }

}
