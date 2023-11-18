package nur.sak.operation.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nur.sak.operation.dto.UserCardDTO;
import nur.sak.operation.entities.User;
import nur.sak.operation.service.implementation.CardServiceImpl;
import nur.sak.operation.service.implementation.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Getter
@Setter
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final CardServiceImpl cardService;


    @GetMapping("/search/{username}")
    public Optional<User> findByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/findall")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/search{id}")
    public Optional<User> findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/connect")
    public void UserWithCard(@RequestBody UserCardDTO dto) {
        cardService.addCardToUser(dto);
    }

    @PutMapping("/update{userId}")
    public void update(@PathVariable("userId") Long userId,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String surname,
                       @RequestParam(required = false) String username,
                       @RequestParam(required = false) Long phoneName) {
        userService.updateUser(userId, name, surname, username, phoneName);
    }

    @DeleteMapping("/delete{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }
}
