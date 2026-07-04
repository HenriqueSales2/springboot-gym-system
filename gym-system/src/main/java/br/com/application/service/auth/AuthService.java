package br.com.application.service.auth;

import br.com.application.data.dto.security.AccountCredentialsDTO;
import br.com.application.data.dto.security.TokenDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.model.User;
import br.com.application.repository.UserRepository;
import br.com.application.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static br.com.application.mapper.ObjectMapper.parseObject;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtTokenProvider provider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenDTO> sigIn(AccountCredentialsDTO credentials) {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername());
        if (user == null) throw new UsernameNotFoundException("Username: " + credentials.getUsername() + " not found!");

        var token = provider.createAccessToken(
                credentials.getUsername(),
                user.getRoles()
        );
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        var user = repository.findByUsername(username);

        TokenDTO token;
        if (user != null) token = provider.refreshToken(refreshToken);
         else throw new UsernameNotFoundException("Username: " + username + " not found!");

        return ResponseEntity.ok(token);
    }

    public AccountCredentialsDTO create(AccountCredentialsDTO user) {
        if (user == null) throw new RequiredObjectIsNullException();

        if (repository.findByUsername(user.getUsername()) != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");

        logger.info("Creating one new User!");

        var entity = new User();
        entity.setFullname(user.getFullname());
        entity.setUsername(user.getUsername());
        entity.setPassword(generateHashPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        return parseObject(repository.save(entity), AccountCredentialsDTO.class);
    }

    private String generateHashPassword(String password) {
        PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder(
                "",
                8,
                185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Enconder);
        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        encoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder);
        return encoder.encode(password);
    }
}