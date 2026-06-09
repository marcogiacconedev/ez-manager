// package com.ezmanager.backend.util;

// import java.nio.charset.StandardCharsets;
// import java.security.Key;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.UUID;
// import java.util.function.Function;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// import com.ezmanager.backend.model.User;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;

// @Component
// public class JwtUtil {
//     @Value("${jwt.secret}") //va a cercare quella string in aaplication.properties
//     private String secret;

//     @Value("${jwt.expiration}")
//     private Long expiration;

//     //converte la stringa in un array di bytes
//     //gli algoritmi crittografici lavorano con 
//     // i byte e non con le stringhe. UTF-8 garantisce 
//     //una conversione consistente e prevedibile 
//     private Key getSigningKey() {
//         byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
//         //hmacSha. -> metodo della libreria jjwt
//         //prende array di bytes -> crea una chiave per l' algoritmo HMAC-SHA
//         //verifica che abbia una lunghezza adeguata (almeno 256 bit)

//         //in pratica: sta preparando la chiave segreta per firmare
//         //e verificare il JWT
//         return Keys.hmacShaKeyFor(keyBytes);
//     }

//     //genera un token prendendo in unput l' username e la 
//     public String generateToken(UUID userId, List<String> roles) {
//         Map<String, Object> claims = new HashMap<>();
//         claims.put("roles", roles);

//         return Jwts.builder()
//             .setClaims(claims)
//             .setSubject(userId.toString())
//             .setIssuedAt(new Date())
//             .setExpiration(new Date(System.currentTimeMillis() + expiration))
//             .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//             .compact();
//     }

//     //estrae tutti i claim da un token
//     public Claims extractAllClaims(String token) {
//         return Jwts.parserBuilder()
//             .setSigningKey(getSigningKey())
//             .build()
//             .parseClaimsJws(token)
//             .getBody();
//     }

//     // Estrae un claim specifico
//     public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//         final Claims claims = extractAllClaims(token);
//         return claimsResolver.apply(claims);
//     }

//     //estrare l' id dell user dal claim
//     public String extractUserId(String token) {
//         return extractClaim(token, Claims::getSubject);
//     }

//     //estrae la data di scadenza dal claim
//     public Date extractExpiration(String token) {
//         return extractClaim(token, Claims::getExpiration);
//     }

//     //controlla se il token è scaduto
//     private Boolean isTokenExpired(String token) {
//         return extractExpiration(token).before(new Date());
//     }

//     //valida il token
//     public Boolean validateToken(String token, User userDetails) {
//         final String id = extractUserId(token);
//         return (id.equals(userDetails.getId().toString()) && !isTokenExpired(token));
//     }

// }
