package com.example.ClosedСlub.service;

import com.example.ClosedСlub.dto.*;
import com.example.ClosedСlub.entity.QrCode;
import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.exception.QrCodeNotFoundException;
import com.example.ClosedСlub.exception.UserNotFoundException;
import com.example.ClosedСlub.repository.QrCodeRepository;
import com.example.ClosedСlub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    void createQrCode_shouldDeactivateOldAndCreateNew() {
        User user = new User("Иванов Иван Иванович");
        user.setId(1L);

        QrCodeRequest request = new QrCodeRequest();
        request.setUserId(1L);

        QrCode oldQr = new QrCode();
        oldQr.setId(10L);
        oldQr.setActive(true);
        oldQr.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(qrCodeRepository.findByUserIdAndActiveTrue(1L)).thenReturn(Optional.of(oldQr));

        when(qrCodeRepository.save(any(QrCode.class))).thenAnswer(invocation -> {
            QrCode saved = invocation.getArgument(0);
            saved.setId(20L);
            return saved;
        });

        QrCodeResponse response = qrCodeService.createQrCode(request);

        assertNotNull(response);
        assertEquals(20L, response.getId());
        assertTrue(response.isActive());
        assertFalse(oldQr.isActive());
        verify(qrCodeRepository, times(2)).save(any(QrCode.class));
    }

    @Test
    void accessByQr_shouldReturnUserAndChangeUuid() {
        UUID oldUuid = UUID.randomUUID();
        User user = new User("Иванов Иван Иванович");
        user.setId(1L);

        QrCode qrCode = new QrCode();
        qrCode.setUuid(oldUuid);
        qrCode.setUser(user);

        when(qrCodeRepository.findByUuid(oldUuid)).thenReturn(Optional.of(qrCode));

        AccessByUUIDResponse response = qrCodeService.accessByUUID(oldUuid);

        assertEquals("Иванов Иван Иванович", response.getUserName());
        assertNotEquals(oldUuid, qrCode.getUuid());
        verify(qrCodeRepository, times(1)).save(qrCode);
    }

    @Test
    void accessByQr_shouldThrowException_whenUuidNotFound() {
        UUID randomUuid = UUID.randomUUID();
        when(qrCodeRepository.findByUuid(randomUuid)).thenReturn(Optional.empty());

        assertThrows(QrCodeNotFoundException.class, () -> qrCodeService.accessByUUID(randomUuid));
    }
}
