package com.example.ClosedСlub.service;

import com.example.ClosedСlub.entity.QrCode;
import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.repository.QrCodeRepository;
import com.example.ClosedСlub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QrCodeServiceTest {

    @Mock
    private QrCodeRepository qrCodeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QrCodeService qrCodeService;

    @Test
    void createForUser_CreatesActiveQrCodeWhenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setName("Иван");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(qrCodeRepository.save(any(QrCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QrCode created = qrCodeService.createForUser(1L);

        assertNotNull(created);
        assertNotNull(created.getUuid());
        assertTrue(created.isActive());
        assertEquals(user, created.getUser());
        verify(qrCodeRepository).save(any(QrCode.class));
    }

    @Test
    void createForUser_ThrowsWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> qrCodeService.createForUser(99L));
        verify(qrCodeRepository, never()).save(any());
    }

    @Test
    void accessByQr_DeactivatesOldQrAndIssuesNewOneWhenActive() {
        User user = new User();
        user.setId(1L);

        UUID oldUuid = UUID.randomUUID();
        QrCode activeCode = new QrCode();
        activeCode.setUuid(oldUuid);
        activeCode.setActive(true);
        activeCode.setUser(user);

        when(qrCodeRepository.findByUuid(oldUuid)).thenReturn(Optional.of(activeCode));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(qrCodeRepository.save(any(QrCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String allowedUser = qrCodeService.accessByQr(oldUuid);

        assertEquals(user.getName(), allowedUser);
        assertFalse(activeCode.isActive());
        verify(qrCodeRepository, times(2)).save(any(QrCode.class));
    }

    @Test
    void accessByQr_ThrowsWhenQrCodeNotFound() {
        UUID unknownUuid = UUID.randomUUID();
        when(qrCodeRepository.findByUuid(unknownUuid)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> qrCodeService.accessByQr(unknownUuid));
        verify(qrCodeRepository, never()).save(any());
    }

    @Test
    void accessByQr_ThrowsWhenQrCodeIsAlreadyUsed() {
        User user = new User();
        user.setId(1L);

        UUID usedUuid = UUID.randomUUID();
        QrCode usedCode = new QrCode();
        usedCode.setUuid(usedUuid);
        usedCode.setActive(false);
        usedCode.setUser(user);

        when(qrCodeRepository.findByUuid(usedUuid)).thenReturn(Optional.of(usedCode));

        assertThrows(IllegalStateException.class, () -> qrCodeService.accessByQr(usedUuid));
        verify(qrCodeRepository, never()).save(any());
    }
}
