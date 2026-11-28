package com.example.ClosedСlub.service;

import com.example.ClosedСlub.dto.UserCreateRequest;
import com.example.ClosedСlub.dto.UserResponse;
import com.example.ClosedСlub.dto.UserUpdateRequest;
import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.exception.UserNotFoundException;
import com.example.ClosedСlub.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getName()))
                .toList();
    }

    public UserResponse getById (Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponse(user.getId(), user.getName()))
                .orElseThrow(() -> new UserNotFoundException("Участник с id " + id + " не найден"));
    }

    public UserResponse create(UserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());

        User savedUser =userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getName());
    }

    public UserResponse update(Long id, UserUpdateRequest request) {
      User user = userRepository.findById(id)
              .orElseThrow(() ->new UserNotFoundException("Участник с id " + id + " не найден"));

      user.setName(request.getName());
      User updateUser = userRepository.save(user);

      return new UserResponse(updateUser.getId(), updateUser.getName());

    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
    }


}
