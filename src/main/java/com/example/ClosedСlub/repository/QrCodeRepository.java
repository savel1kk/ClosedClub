package com.example.ClosedСlub.repository;


import com.example.ClosedСlub.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {

    Optional<QrCode> findByUuid(UUID uuid);

    Optional<QrCode> findByUuidAndActiveTrue(UUID uuid);
}
