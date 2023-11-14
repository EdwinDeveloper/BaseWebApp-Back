package ed.service.messaging.services;

import ed.service.messaging.entity.jpa.User;
import ed.service.messaging.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JWTService {

    private UserRepository userRepository;

    public JWTService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        return doGenerateToken(claims, userDetails.getEmail());
    }

    private String secret = "aJvLxugakTclhgvggvP5ogAciCZTUsNbHJokt84msNEhLkTBjJM+V4dq1gyxJGA3YrcoIJYAsZVvi0k515t8CA==";

    private Long expiration = 3600000L;

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        // Assuming authorities are stored as a list of strings in the "authorities" claim
        List<String> authorities = (List<String>) claims.get("authorities");

        // Convert the list of strings to a collection of GrantedAuthority objects
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        final String email = getEmailFromToken(token);

        Optional<User> userExist = userRepository.findByEmail(email);

        return (email.equals(userExist.get().getEmail()) && !isTokenExpired(token));
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    private Date getExpirationDateFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getExpiration();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

}
