package nur.sak.operation.service.implementation;

import lombok.RequiredArgsConstructor;
import nur.sak.operation.entities.User;
import nur.sak.operation.repository.UserRepository;
import nur.sak.operation.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findbyPhone(Long num) {
        return userRepository.findByPhoneNum(num);
    }

    public void deleteUser(Long Id) {
        if (!userRepository.existsById(Id)) {
            throw new IllegalStateException(("No such user is found"));
        }
        userRepository.deleteById(Id);
    }

    public void updateUser(Long userId,
                           String name,
                           String surname,
                           String username,
                           Long phoneNum) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalStateException("No such user is found"));
        if (name != null && name.length() > 0 && !Objects.equals(name, user.getName())) {
            user.setName(name);
        }

        if (surname != null && surname.length() > 0 && !Objects.equals(surname, user.getSurname())) {
            user.setSurname(surname);
        }

        if (username != null && username.length() > 0 && !Objects.equals(username, user.getUsername())) {
            user.setUsername(username);
        }

        if (phoneNum != null && !Objects.equals(phoneNum, user.getPhoneNum())) {
            user.setPhoneNum(phoneNum);
        }
    }

}
