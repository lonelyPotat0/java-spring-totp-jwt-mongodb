package com.example.demo.security.config;

import java.io.Serializable;
// import java.security.Key;
// import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.impl.crypto.MacProvider;

@Component
public class JwtTokenUtil implements Serializable {
    static Variable variable = new Variable();
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // Test
    private static final String secret = variable.getJwtSecret();//"SuperSecret";
    
    //Production # also read line 73
    // private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    // private static final byte[] secretBytes = secret.getEncoded();
    // private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}


    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(id)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, secret )// base64SecretBytes)
                .compact();
    }

    // validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        System.out.println("Bearer true" + (username.equals(userDetails.getUsername()) && !isTokenExpired(token)));
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
