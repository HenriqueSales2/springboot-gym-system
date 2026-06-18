package br.com.application.config;

import br.com.application.security.jwt.JwtTokenFilter;
import br.com.application.security.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
@SecurityScheme(name = SecurityConfig.SECURITY, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfig {

    public static final String SECURITY = "bearerAuth";

    @Autowired
    private JwtTokenProvider provider;

    public SecurityConfig(JwtTokenProvider provider) {
        this.provider = provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder(
                "", // salt vazio
                8, // comprimento da chave gerada (8 bits)
                185000, // número de vezes que o algoritmo será aplicado
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256 // usa HMAC-SHA256 para fazer o hash a senha (criptografar a senha)
        );

        Map<String, PasswordEncoder> enconders = new HashMap<>();
        enconders.put("pbkdf2", pbkdf2Enconder); // setando o nome da chave e o algoritmo de criptografia
        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("pbkdf2", enconders);

        encoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder);
        return encoder;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(provider);
        //@formatter:off
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // não vai guardar o estado da sessão
                )
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(
                                        "/auth/signin",
                                        "/auth/refresh/**",
                                        "/auth/createUser", // remover esse endpoint em produção, permitindo ele apenas para facilitar alguns testes
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/scalar/**"
                                ).permitAll() // permitindo acesso total para essas URLs
                                .requestMatchers("/api/**")
                                .authenticated() // acesso apenas com JWT válido
                                .requestMatchers("/users").denyAll() // evitando expor as entidades
                ).cors(cors -> {}) // subindo cors em modo default
                .build(); // construindo o filtro no padrão Builder (chamando métodos e setando os valores)
        //@formatter:on
    }
}