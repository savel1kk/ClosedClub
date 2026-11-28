package com.example.ClosedСlub.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "qr_codes")
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean active = true;

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public QrCode() {
    }

    public QrCode(User user, UUID uuid) {
        this.user = user;
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
