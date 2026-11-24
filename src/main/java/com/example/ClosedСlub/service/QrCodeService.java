package com.example.ClosedСlub.service;

import com.example.ClosedСlub.entity.QrCode;
import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.repository.QrCodeRepository;
import com.example.ClosedСlub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    public QrCode createForUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Участника " + userId + " не существует");
        }
        User user =  optionalUser.get();
        UUID uuid = UUID.randomUUID();
        QrCode qrCode = new QrCode();
        qrCode.setUser(user);
        qrCode.setUuid(uuid);
        qrCode.setActive(true);
        return qrCodeRepository.save(qrCode);
    }

    @Transactional
    public String accessByQr(UUID uuid) {
        Optional<QrCode> optionalQrCode = qrCodeRepository.findByUuid(uuid);
        if (optionalQrCode.isEmpty()){
            throw new IllegalStateException("Доступ закрыт");
        }
        QrCode qrCode = optionalQrCode.get();
        if (!qrCode.isActive()) {
            throw new IllegalStateException("Qr-Code не действителен");
        }
        qrCode.setActive(false);
        qrCodeRepository.save(qrCode);
        createForUser(qrCode.getUser().getId());
        return qrCode.getUser().getName();
    }


}
