package com.example.demo.ultils;

// import com.example.demo.security.config.JwtTokenUtil;
import com.example.demo.security.config.Variable;

// import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.function.Function;

public class TokenTool {
    
    // @Autowired
	// private JwtTokenUtil jwtTokenUtil;

    static Variable variable = new Variable();
    private String secret = variable.getJwtSecret();
    
    public String getUsernameFromToken(String bearer) {
        if (bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            return getClaimFromToken(token, Claims::getSubject);// jwtTokenUtil.getUsernameFromToken(token);
        }
        return null;
    }

    
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
