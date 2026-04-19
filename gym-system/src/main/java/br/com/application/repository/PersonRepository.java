package br.com.application.repository;

import br.com.application.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*não é necessário colocar essa annotation,
porém é importante salientar que em sistemas legados
se remover essa annotation é perigoso quebrar o projeto inteiro.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id") Long id);

}
