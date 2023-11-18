package nur.sak.operation.repository;

import nur.sak.operation.entities.Card;
import nur.sak.operation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByCardNum(Long cardNum);
    Boolean existsUserByCardNum(Long cardNum);
}
