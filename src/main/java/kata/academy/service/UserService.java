package kata.academy.service;


import kata.academy.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    List<User> findAll();

    User saveUser(User user);

    void deleteById(Long id);

}
