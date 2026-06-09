package br.com.application.repository;

import br.com.application.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*não é necessário colocar essa annotation.
Porém, é importante salientar que em sistemas legados
se remover essa annotation é perigoso quebrar o projeto inteiro.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id") Long id);

    // and
    // Leandro, Andressa, Amanda, Andre. Independente da posição das letras "and", o método irá listar
    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(concat('%', :firstName, '%'))") // implementando comando SQL para listar todos os usuários pelo nome, ou parte dele
    Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable); // isso é Camel Case
}