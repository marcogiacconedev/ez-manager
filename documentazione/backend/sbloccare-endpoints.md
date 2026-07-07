## Come sbloccare momentaneamente gli endpoints per lo sviluppo

Spring Security blocca di base tutti gli endpoints.
Per eliminare momentaneamente questo layer di sicurezza, 
occorre creare ``` /src/main/java/..../config/SecurityConfig.java ``` ed incollare
```
package com.ezmanager.backend.config;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
```


