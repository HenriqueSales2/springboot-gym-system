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
                new UsernamePasswordAuthenticationToken( // passando as credenciais que o usuário colocou na requisição
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername()); // resgatando o username no banco de dados
        if (user == null) throw new UsernameNotFoundException("Username: " + credentials.getUsername() + " not found!"); // validando se o user está null ou não

        var token = provider.createAccessToken( // criando o acesso ao token usando as credenciais passadas pelo cliente
                credentials.getUsername(),
                user.getRoles() // passando a lista de permissões
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

        // validando se o username já existe, se existir não vai ser criado e vai cair em uma exceção de conflito
        if (repository.findByUsername(user.getUsername()) != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");

        logger.info("Creating one new User!");

        var entity = new User();
        entity.setFullname(user.getFullname());
        entity.setUsername(user.getUsername());
        entity.setPassword(generateHashPassword(user.getPassword())); // criptografando a senha
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        return parseObject(repository.save(entity), AccountCredentialsDTO.class); // convertendo em DTO, criando, e salvando a entidade
    }

    private String generateHashPassword(String password) {
        PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder(
                "", // salt vazio
                8, // comprimento da chave gerada (8 bits)
                185000, // número de vezes que o algoritmo será aplicado
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256 // usa HMAC-SHA256 para fazer o hash a senha (criptografar a senha)
        );

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Enconder); // setando o nome da chave e o algoritmo de criptografia
        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        encoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder); // setando o algoritmo padrão para comparar as senhas (comparar a senha digitada com a senha criptografada)
        return encoder.encode(password);
    }
}