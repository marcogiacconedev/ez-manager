  Cosa è stato fatto da Claude
  - pom.xml: abilitato spring-boot-starter-security (BCrypt + filter chain).
  - application.properties + .env.dev: ora le credenziali DB e JWT (JWT_SECRET, JWT_EXPIRATION) sono lette da env var — Docker le inietta automaticamente.
  - util/JwtUtil.java: scommentato e completato (genera token HS256 con subject=userId, claim username e role; estrae/valida).
  - model/User.java: tolto il final da userRole (Hibernate non poteva valorizzarlo via setter, lo lasciamo a default "USER") e aggiunto unique=true su userName/userEmail.
  - dto/SignupRequest.java: completato con username/email/password + getter/setter.
  - repository/UserRepository.java: aggiunti findByUserName, existsByUserName, existsByUserEmail.
  - service/AuthService.java: signup (BCrypt encode + check duplicati → 409) e login (match hash + JWT → 401 se invalido).
  - controller/AuthController.java: mappato su /auth, espone POST /auth/signup e POST /auth/login.
  - security/SecurityConfig.java: PasswordEncoder BCrypt, sessioni stateless, /auth/login e /auth/signup pubblici, tutto il resto autenticato, CSRF off.
  - security/JwtAuthenticationFilter.java: legge Authorization: Bearer …, valida e popola il SecurityContext.

  Da fare prima di mettere in produzione
  - Cambia JWT_SECRET in .env.dev con una stringa reale ≥ 32 char.
  - SPRING_JPA_HIBERNATE_DDL_AUTO=update va bene in dev; in prod meglio validate + migration tool.

  Per provarlo
  docker compose -f backend/docker-compose.yaml up --build
  # signup
  curl -X POST http://localhost:8080/auth/signup -H 'Content-Type: application/json' \
    -d '{"username":"marco","email":"marco@test.it","password":"segreta"}'
  # login
  curl -X POST http://localhost:8080/auth/login -H 'Content-Type: application/json' \
    -d '{"username":"marco","password":"segreta"}'
  # endpoint protetto
  curl http://localhost:8080/users/<uuid> -H 'Authorization: Bearer <token>'