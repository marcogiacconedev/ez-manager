# Flusso autenticazione

### stack
1. spring Boot 4.x
2. Spring Security
3. JPA + PostgreSQL
4. JWT (jjwt)
5. no sessioni, solo REST


### Obiettivo 
1. ``` POST /auth/login ```
2. username + password
3. Sppring valida le credenziali contro la tabella Users
4. Se OK -> genera JWT, ritorna JWT
5. le altre API accettano solo richieste con Authorization: Bearer <token>


## 1. Modello Dati
```
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // getter/setter
}
```

## 2. Repository
```
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
}
```

## 3. Metodo in UserService
```
@Override
public UserDetails getUserByUsername(String username) {
    UserEntity user = repo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return User
        .withUsername(user.getUsername())
        .password(user.getPassword()) // BCrypt
        .authorities("USER")
        .build();
}
```

## 4. SecurityConfig
```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

## 5. LoginRequest
```
public record LoginRequest(
    String username,
    String password
) {}
```

## 6. AuthController
```
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(
        AuthenticationManager authManager,
        JwtService jwtService
    ) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest req
    ) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                req.username(),
                req.password()
            )
        );

        String token = jwtService.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
```

Se:
- user non esiste -> 401
- password errata -> 401
- OK -> 200 + JWT

## 7. JwtService
```
@Service
public class JwtService {

    private final String secret = "super-secret-key";
    private final long expirationMs = 86400000; // 1 giorno

    public String generateToken(Authentication auth) {
        return Jwts.builder()
            .setSubject(auth.getName())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secret.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
```

## 8. JWT Filter
Rende 'loggati' gli itenti
```
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService uds;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService uds
    ) {
        this.jwtService = jwtService;
        this.uds = uds;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String username = jwtService.extractUsername(token);

        UserDetails user = uds.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
            );

        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }
}
```

## 10. Login
Login: ``` POST /auth/login ```
Response: ``` { "token": "eyJhbGciOiJIUzI1NiIsIn..." } ```

Request API protetta
```
GET /users
Authorization: Bearer <token>
```