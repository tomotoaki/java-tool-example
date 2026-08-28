package com.example.tool.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * access_token テーブルに対応するエンティティ。
 * Lombok の @Data / @Builder でゲッター・セッター・コンストラクタを自動生成する。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {

    private Long id;

    private String tokenValue;

    private String ownerName;

    /** ACTIVE / REVOKED */
    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
