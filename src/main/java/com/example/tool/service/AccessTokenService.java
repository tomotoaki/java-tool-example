package com.example.tool.service;

import java.util.List;
import java.util.Optional;

import com.example.tool.entity.AccessToken;

/**
 * アクセストークンに関する業務ロジックを定義するインタフェース。
 * Application（呼び出し側）はこのインタフェースのみに依存する。
 */
public interface AccessTokenService {

    /**
     * 新しいアクセストークンを発行する。
     *
     * @param ownerName  トークンの所有者名
     * @param validDays  有効期限（日数）。null の場合は無期限とする。
     * @return 発行されたアクセストークン（採番された id を含む）
     */
    AccessToken issue(String ownerName, Long validDays);

    /**
     * 全アクセストークンを取得する。
     */
    List<AccessToken> findAll();

    /**
     * id を指定してアクセストークンを取得する。
     */
    Optional<AccessToken> findById(Long id);

    /**
     * 所有者名・有効期限を更新する。
     *
     * @return 更新後のアクセストークン
     */
    AccessToken update(Long id, String ownerName, Long validDays);

    /**
     * トークンを無効化（REVOKED）する。
     */
    AccessToken revoke(Long id);

    /**
     * トークンを削除する。
     *
     * @return 削除できた場合 true
     */
    boolean delete(Long id);
}
