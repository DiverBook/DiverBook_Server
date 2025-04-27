CREATE TABLE IF NOT EXISTS app_user(
    id UUID PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    divisions VARCHAR(255),
    phone_number VARCHAR(20),
    interests VARCHAR(255),
    places VARCHAR(255),
    about VARCHAR(255),
    isActivated BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE IF NOT EXISTS password(
    user_id UUID PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS collection (
    id SERIAL PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    found_user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    found_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    memo TEXT
);

CREATE TABLE IF NOT EXISTS badge(
    code VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_badge(
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    badge_code VARCHAR(50) NOT NULL REFERENCES badge(code) ON DELETE CASCADE,
    acquired_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, badge_code)
);

CREATE TABLE IF NOT EXISTS token_black_list(
    id SERIAL PRIMARY KEY,
    invalid_refresh_token VARCHAR(255),
    invalid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS profile_image(
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    user_image TEXT
)