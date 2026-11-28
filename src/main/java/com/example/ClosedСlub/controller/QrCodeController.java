package com.example.ClosedСlub.controller;

import com.example.ClosedСlub.dto.AccessByUUIDResponse;
import com.example.ClosedСlub.dto.QrCodeRequest;
import com.example.ClosedСlub.dto.QrCodeResponse;
import com.example.ClosedСlub.entity.QrCode;
import com.example.ClosedСlub.service.QrCodeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping
    public QrCodeResponse create (@Valid @RequestBody QrCodeRequest request) {
        return qrCodeService.createQrCode(request);
    }

    @PostMapping("/access/{uuid}")
    public AccessByUUIDResponse accessByUUID (@PathVariable UUID uuid) {
        return qrCodeService.accessByUUID(uuid);
    }

    @GetMapping
    public List<QrCodeResponse> getAll() {
        return qrCodeService.getAll();
    }

    @GetMapping("/{id}")
    public QrCodeResponse getById(@PathVariable Long id) {
        return qrCodeService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        qrCodeService.delete(id);
    }
}
