package com.example.ClosedСlub.service;

import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        Optional<User> optionalUser = userRepository.findByName(user.getName());
        if (optionalUser.isPresent()) {
            throw new IllegalStateException("Данный участник уже есть в базе");
        }
        user.setId(null);
        return userRepository.save(user);
    }

    public User findById (Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Участника " + id + " не существует");
        }
        return optionalUser.get();
    }

    public void delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Участника " + id + " не существует");
        }
        userRepository.deleteById(id);
    }

    public void update(Long id, String name) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Участника " + id + " не существует");
        }
        User user = optionalUser.get();

        if (name != null && !name.equals(user.getName())) {
            Optional<User> foundByName = userRepository.findByName(name);
            if (foundByName.isPresent()) {
                throw new IllegalStateException("Данный участник уже есть в базе");
            }
            user.setName(name);
        }
        userRepository.save(user);
    }
}
