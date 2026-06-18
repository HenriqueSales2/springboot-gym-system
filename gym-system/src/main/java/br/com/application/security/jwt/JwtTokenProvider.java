package br.com.application.security.jwt;

import br.com.application.data.dto.security.TokenDTO;
import br.com.application.exception.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @Autowired
    private UserDetailsService service; // trocar aqui depois (possivel erro)

    Algorithm algorithm = null;

    /*
     Usar essa annotation para inicializar qualquer coisa que queira após a aplicação ter sido inicializada.
     Porém, isso ocorrerá antes do sistema executar qualquer ação do cliente
    */

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); // pegar a secretKey em Bytes e setar na variável
        algorithm = Algorithm.HMAC256(secretKey.getBytes()); // decodificando o token
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds); // criando a variável responsável por determinar o prazo de validade do token que é de 1h
        String accessToken = getAccessToken(username, roles, now, validity);
        String refreshToken = getRefreshToken(username, roles, now);

        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        var token = "";
        if(tokenContainsBearer(refreshToken))
            token = refreshToken.substring((SecurityConstants.BEARER_PREFIX).length());

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        String username = decoded.getSubject();
        List<String> roles = decoded.getClaim("roles").asList(String.class);

        return createAccessToken(username, roles);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        String issueUrl = ServletUriComponentsBuilder // determinando a URL para criar o Token
                .fromCurrentContextPath()
                    .build()
                        .toUriString();

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issueUrl)
                .sign(algorithm);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        /*
        Acrescentar 3 horas a partir da validade do token a fim de evitar trafegar essas informações pela rede.
        Além de que dará mais uma chance do cliente renovar o token expirado
        */
        Date refreshTokenValidity = new Date(now.getTime() + (validityInMilliseconds * 3));

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenValidity)
                .withSubject(username)
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decoded = decodedToken(token);
        UserDetails userDetails = service.loadUserByUsername(decoded.getSubject()); // obtém a autenticação

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public DecodedJWT decodedToken(String token) {
        Algorithm algorithmDecodedToken = Algorithm.HMAC256(secretKey.getBytes()); // decodificando o token
        JWTVerifier verifier = JWT.require(algorithmDecodedToken).build(); // verificando se a assinatura é válida
        DecodedJWT decoded = verifier.verify(token);
        return decoded;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        // verificando se o bearerToken está vazio e começa com esse prefixo "Bearer "
        if (tokenContainsBearer(bearerToken)) return bearerToken.substring((SecurityConstants.BEARER_PREFIX).length()); // removendo o prefixo "Bearer " e só vai retornar o token

        return null;
    }

    public boolean validateToken(String token) {
        DecodedJWT decoded = decodedToken(token);

        try {
            if (decoded.getExpiresAt().before(new Date())) return false; // se a data já tiver sido expirada irá retornar false
            return true; // caso não não passe pelo if irá retornar true
        }
        catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token!");
        }
    }

    private static boolean tokenContainsBearer(String refreshToken) {
        return StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith(SecurityConstants.BEARER_PREFIX);
    }
}