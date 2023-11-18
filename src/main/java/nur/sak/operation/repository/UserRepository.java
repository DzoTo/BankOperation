package nur.sak.operation.repository;

import nur.sak.operation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    User findByPhoneNum(Long num);

    Boolean existsUserByPhoneNum(Long phoneNum);

}
