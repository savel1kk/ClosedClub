package com.example.ClosedСlub.service;

import com.example.ClosedСlub.dto.UserCreateRequest;
import com.example.ClosedСlub.dto.UserResponse;
import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.exception.UserNotFoundException;
import com.example.ClosedСlub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void create_shouldSaveUserAndReturnResponse() {
        UserCreateRequest request = new UserCreateRequest();
        request.setName("Иванов Иван Иванович");

        User savedUser = new User("Иванов Иван Иванович");
        savedUser.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Иванов Иван Иванович", response.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getById_shouldReturnUser_whenExists() {
        User user = new User("Петров Петр Петрович");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getById(1L);

        assertEquals("Петров Петр Петрович", response.getName());
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(99L));
    }
}

