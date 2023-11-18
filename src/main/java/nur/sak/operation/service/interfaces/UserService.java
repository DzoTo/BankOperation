package nur.sak.operation.service.interfaces;

import nur.sak.operation.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User addUser(User user);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    }



