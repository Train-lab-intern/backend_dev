package com.trainlab.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "refresh_sessions", schema = "public")
public class RefreshSessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token", nullable = false)
    private UUID refreshToken;

    @Column(name = "fingerprint", nullable = false)
    private String fingerprint;

    @Column(name = "expired_at", nullable = false)
    private Instant expiredAt;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;
}
