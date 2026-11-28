package com.example.ClosedСlub.service;

import com.example.ClosedСlub.dto.AccessByUUIDResponse;
import com.example.ClosedСlub.dto.QrCodeRequest;
import com.example.ClosedСlub.dto.QrCodeResponse;
import com.example.ClosedСlub.entity.QrCode;
import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.exception.QrCodeNotFoundException;
import com.example.ClosedСlub.exception.UserNotFoundException;
import com.example.ClosedСlub.repository.QrCodeRepository;
import com.example.ClosedСlub.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final UserRepository userRepository;

    public QrCodeService(QrCodeRepository qrCodeRepository, UserRepository userRepository) {
        this.qrCodeRepository = qrCodeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public QrCodeResponse createQrCode(QrCodeRequest request) {
       User user = userRepository.findById(request.getUserId())
               .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

       Optional<QrCode> activeQrCode = qrCodeRepository.findByUserIdAndActiveTrue(user.getId());

       if (activeQrCode.isPresent()) {
           QrCode oldCode = activeQrCode.get();
           oldCode.setActive(false);
           qrCodeRepository.save(oldCode);
       }

       QrCode newQrCode = new QrCode();
       newQrCode.setUser(user);
       newQrCode.setUuid(UUID.randomUUID());
       newQrCode.setActive(true);

       QrCode savedQrCode = qrCodeRepository.save(newQrCode);

       return new QrCodeResponse(savedQrCode.getId(), savedQrCode.getUuid(), user.getId(), savedQrCode.isActive());
    }

    @Transactional
    public AccessByUUIDResponse accessByUUID(UUID uuid) {
       QrCode qrCode = qrCodeRepository.findByUuid(uuid)
               .orElseThrow(() -> new QrCodeNotFoundException("Неверный или устаревший QR-код"));

       User user = qrCode.getUser();

       qrCode.setUuid(UUID.randomUUID());

       qrCodeRepository.save(qrCode);

       return new AccessByUUIDResponse(user.getName(), "Доступ разрешен!");
    }

    @Transactional(readOnly = true)
    public QrCodeResponse getById(Long id) {
        QrCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new QrCodeNotFoundException("QR-код не найден"));

        return new QrCodeResponse(qrCode.getId(), qrCode.getUuid(), qrCode.getUser().getId(), qrCode.isActive());
    }

    @Transactional(readOnly = true)
    public List<QrCodeResponse> getAll() {
        return qrCodeRepository.findAll().stream()
                .map(qrCode -> new QrCodeResponse(qrCode.getId(), qrCode.getUuid(), qrCode.getUser().getId(), qrCode.isActive()))
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!qrCodeRepository.existsById(id)) {
            throw new QrCodeNotFoundException("QR-код не найден");
        }
        qrCodeRepository.deleteById(id);
    }
    
}
