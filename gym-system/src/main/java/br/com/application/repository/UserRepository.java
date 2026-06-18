package br.com.application.repository;

import br.com.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
    /*
    como eu disse em outra classe com essa mesma annotation "Repository", essa annotation não é mais necessária,
    pois quando a classe "JpaRepository" é extendida o Spring Boot já reconhece que essa classe é uma classe de "Repository"
    */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
    nesse caso não seria uma consulta nativa do banco de dados (JPQL - JPA QUERY LANGUAGE)
    é como se fosse um SQL só que para objetos Java, ou seja, vou acessar diretamente o objeto User,
    NÃO É QUERY NATIVA
    */
    @Query("SELECT u FROM User u WHERE u.username =:username")
    User findByUsername(@Param("username") String username);
}