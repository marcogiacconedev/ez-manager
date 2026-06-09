# Flusso di Autenticazione con JWT in Java

Ti spiego il flusso completo di autenticazione JWT usando le librerie JJWT che hai nel tuo progetto.

## Il Flusso Completo

**1. Login dell'utente** → L'utente invia username e password

**2. Validazione credenziali** → Il server verifica le credenziali nel database

**3. Generazione JWT** → Se le credenziali sono corrette, il server genera un token JWT

**4. Invio del token** → Il token viene inviato al client

**5. Richieste successive** → Il client include il token nell'header `Authorization: Bearer <token>`

**6. Validazione del token** → Il server valida il token per ogni richiesta protetta

**7. Accesso alle risorse** → Se il token è valido, l'utente accede alla risorsa

## Implementazione Pratica

### 1. Classe per gestire i JWT

```java
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration; // in millisecondi, es: 86400000 per 24h
    
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    // Genera il token JWT
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    // Estrae l'username dal token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    // Estrae la data di scadenza
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    // Estrae i ruoli
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("roles");
    }
    
    // Estrae un claim specifico
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    // Estrae tutti i claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    // Verifica se il token è scaduto
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    // Valida il token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```

### 2. Controller per il Login

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Autentica l'utente
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            // Carica i dettagli dell'utente
            UserDetails userDetails = userDetailsService.loadUserByUsername(
                loginRequest.getUsername()
            );
            
            // Estrae i ruoli
            List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
            
            // Genera il token
            String token = jwtUtil.generateToken(userDetails.getUsername(), roles);
            
            // Restituisce il token
            return ResponseEntity.ok(new JwtResponse(token));
            
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenziali non valide");
        }
    }
}
```

### 3. Filtro JWT per le richieste

```java
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain chain) 
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;
        
        // Estrae il token dall'header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("Errore nell'estrazione del token", e);
            }
        }
        
        // Valida il token e imposta l'autenticazione nel contesto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        chain.doFilter(request, response);
    }
}
```

### 4. Configurazione di Spring Security

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 5. Classi DTO

```java
// Request per il login
public class LoginRequest {
    private String username;
    private String password;
    
    // getters e setters
}

// Response con il token
public class JwtResponse {
    private String token;
    
    public JwtResponse(String token) {
        this.token = token;
    }
    
    // getter e setter
}
```

### 6. File application.properties

```properties
jwt.secret=ilTuoSecretMoltoLungoEComplesso123456789012345678901234567890
jwt.expiration=86400000
```

⚠️ **Importante**: La chiave segreta deve essere lunga almeno 256 bit (32 caratteri) per HS256.

## Come Funziona in Pratica

1. L'utente fa login inviando POST a `/api/auth/login` con username e password
2. Il server valida le credenziali e genera un JWT
3. Il client salva il token (es. localStorage o cookie)
4. Per ogni richiesta successiva, il client invia il token nell'header: `Authorization: Bearer eyJhbGc...`
5. Il filtro `JwtRequestFilter` intercetta ogni richiesta, estrae e valida il token
6. Se valido, imposta l'autenticazione nel contesto di Spring Security
7. L'utente può accedere alle risorse protette

## Struttura del JWT

Un token JWT è composto da tre parti separate da punti:

```
header.payload.signature
```

**Header**: contiene il tipo di token e l'algoritmo di firma
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

**Payload**: contiene i claims (informazioni sull'utente)
```json
{
  "sub": "username",
  "roles": ["ROLE_USER"],
  "iat": 1516239022,
  "exp": 1516325422
}
```

**Signature**: firma crittografica per verificare l'integrità del token

## Best Practices

- Usa HTTPS per tutte le comunicazioni
- Imposta una scadenza ragionevole per i token (es. 24 ore)
- Non memorizzare informazioni sensibili nel payload del JWT
- Usa una secret key robusta e mantienila segreta
- Implementa un meccanismo di refresh token per sessioni lunghe
- Valida sempre il token lato server
- Implementa un sistema di blacklist per token revocati
- Usa algoritmi di firma sicuri (HS256, RS256)