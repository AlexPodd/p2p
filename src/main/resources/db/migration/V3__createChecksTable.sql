CREATE TABLE checks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    currency_code VARCHAR(10) NOT NULL,
    amount_integer BIGINT NOT NULL CHECK (amount_integer >= 0),
    amount_fraction INTEGER NOT NULL CHECK (amount_fraction >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (currency_code) REFERENCES currencies(code)
);