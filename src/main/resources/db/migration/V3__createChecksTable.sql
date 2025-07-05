CREATE TABLE checks (
    id BIGINT PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    currency_code VARCHAR(10) NOT NULL,
    amount_integer BIGINT NOT NULL CHECK (amount_integer >= 0),
    amount_fraction BIGINT NOT NULL CHECK (amount_fraction >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (currency_code) REFERENCES currencies(code)
);