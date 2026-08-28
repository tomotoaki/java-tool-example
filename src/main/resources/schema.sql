CREATE TABLE IF NOT EXISTS access_token (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    token_value   VARCHAR(255) NOT NULL UNIQUE,
    owner_name    VARCHAR(100) NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMP    NOT NULL,
    expires_at    TIMESTAMP
);
