package com.example.ClosedСlub.service;

import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void create_SavesTheNewParticipantIfTheNameIsNotInTheDatabase() {
        User user = new User();
        user.setName("Иванов Иван Иванович");

        when(userRepository.findByName("Иванов Иван Иванович")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertEquals("Иванов Иван Иванович", result.getName());
        verify(userRepository).save(user);
    }

    @Test
    void create_ThrowsAnExceptionWhenTheNameAlreadyExists() {
        User existingUser = new User();
        existingUser.setName("Иванов Иван Иванович");

        when(userRepository.findByName("Иванов Иван Иванович")).thenReturn(Optional.of(existingUser));

        User newUser = new User();
        newUser.setName("Иванов Иван Иванович");

        assertThrows(IllegalStateException.class, () -> userService.create(newUser));

        verify(userRepository, never()).save(any());
    }

    @Test
    void findById_SuccessfullySearchesById() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void findById_ThrowsAnExceptionIfTheIdIsNotInTheDatabase() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> userService.findById(99L));
    }

    @Test
    void findAll_SuccessfullySeeksAllParticipants() {
        User user1 = new User();
        User user2 = new User();

        user1.setName("Иванов Иван Иванович");
        user2.setName("Федоров Федор Федорович");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void delete_DeletesUserWhenIdExists() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_ThrowsAnExceptionWhenIdNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> userService.delete(99L));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void update_UpdatesUserNameWhenIdExists() {
        User user = new User();
        user.setId(1L);
        user.setName("Старое имя");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.update(1L, "Новое имя");

        assertEquals("Новое имя", user.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void update_ThrowsAnExceptionWhenIdNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> userService.update(99L, "Новое имя"));
    }
}
