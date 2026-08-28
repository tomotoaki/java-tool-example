package com.example.tool.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.tool.entity.AccessToken;
import com.example.tool.mapper.AccessTokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link AccessTokenService} の実装。
 * トークン文字列の生成や有効期限計算などの業務ロジックをここに集約し、
 * DB アクセスは {@link AccessTokenMapper}（MyBatis）に委譲する。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_REVOKED = "REVOKED";

    private final AccessTokenMapper accessTokenMapper;

    @Override
    public AccessToken issue(String ownerName, Long validDays) {
        LocalDateTime now = LocalDateTime.now();

        AccessToken accessToken = AccessToken.builder()
                .tokenValue(generateTokenValue())
                .ownerName(ownerName)
                .status(STATUS_ACTIVE)
                .createdAt(now)
                .expiresAt(validDays != null ? now.plusDays(validDays) : null)
                .build();

        accessTokenMapper.insert(accessToken);
        log.info("アクセストークンを発行しました: id={}, owner={}", accessToken.getId(), ownerName);
        return accessToken;
    }

    @Override
    public List<AccessToken> findAll() {
        return accessTokenMapper.findAll();
    }

    @Override
    public Optional<AccessToken> findById(Long id) {
        return accessTokenMapper.findById(id);
    }

    @Override
    public AccessToken update(Long id, String ownerName, Long validDays) {
        AccessToken accessToken = requireById(id);

        if (ownerName != null) {
            accessToken.setOwnerName(ownerName);
        }
        if (validDays != null) {
            accessToken.setExpiresAt(LocalDateTime.now().plusDays(validDays));
        }

        accessTokenMapper.update(accessToken);
        log.info("アクセストークンを更新しました: id={}", id);
        return accessToken;
    }

    @Override
    public AccessToken revoke(Long id) {
        AccessToken accessToken = requireById(id);
        accessToken.setStatus(STATUS_REVOKED);
        accessTokenMapper.update(accessToken);
        log.info("アクセストークンを無効化しました: id={}", id);
        return accessToken;
    }

    @Override
    public boolean delete(Long id) {
        boolean deleted = accessTokenMapper.deleteById(id) > 0;
        if (deleted) {
            log.info("アクセストークンを削除しました: id={}", id);
        }
        return deleted;
    }

    private AccessToken requireById(Long id) {
        return accessTokenMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("指定された id のアクセストークンが見つかりません: " + id));
    }

    private String generateTokenValue() {
        return "tok_" + UUID.randomUUID().toString().replace("-", "");
    }
}
