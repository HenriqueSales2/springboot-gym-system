package br.com.application.service.auth;

import br.com.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    // maior flexibilidade na aplicação e não ficar com problemas de falhas na aplicação por dependências adicionais (algumas dependências serão injetadas)
    @Autowired
    UserRepository repository;

    // usar construtor caso queira garantir que nenhuma dependência crítica esteja ausente (todas as dependências serão injetadas)
    public UserService(UserRepository repository) {
        if (repository == null) throw new IllegalArgumentException("Repository cannot be null");
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByUsername(username);
        if (user != null) return user;
        else throw new UsernameNotFoundException("Username " + username + " not found!");
    }
}