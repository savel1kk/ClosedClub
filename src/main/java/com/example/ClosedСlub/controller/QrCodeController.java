package com.example.ClosedСlub.controller;

import com.example.ClosedСlub.entity.QrCode;
import com.example.ClosedСlub.entity.User;
import com.example.ClosedСlub.service.QrCodeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/users/{userId}")
    public QrCode createForUser (@PathVariable Long userId) {
        return qrCodeService.createForUser(userId);
    }

    @PostMapping("/access/{uuid}")
    public String accesByQr (@PathVariable UUID uuid) {
        return qrCodeService.accessByQr(uuid);
    }

}
