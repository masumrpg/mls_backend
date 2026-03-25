-- V1__create_users_and_profiles.sql

-- Tabel Users
CREATE TABLE mls.m_users
(
    id           BIGSERIAL PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    role         VARCHAR(20)  NOT NULL DEFAULT 'USER',
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP
);

-- Tabel Profile
CREATE TABLE mls.m_profiles
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT    NOT NULL UNIQUE,
    full_name   VARCHAR(100),
    avatar_url  VARCHAR(255),
    address     VARCHAR(500),
    city        VARCHAR(50),
    postal_code VARCHAR(20),
    bio         VARCHAR(255),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES mls.m_users (id) ON DELETE CASCADE
);

-- Index untuk performa pencarian
CREATE INDEX idx_users_email ON mls.m_users (email);
CREATE INDEX idx_users_username ON mls.m_users (username);