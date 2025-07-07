CREATE TABLE transfers (
    id BIGINT PRIMARY KEY,
    id_from BIGINT NOT NULL,
    id_to BIGINT NOT NULL,
       currency_code VARCHAR(10) NOT NULL,
    amount_integer BIGINT NOT NULL CHECK (amount_integer >= 0),
    amount_fraction BIGINT NOT NULL CHECK (amount_fraction >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_from) REFERENCES checks(id),
    FOREIGN KEY (id_to) REFERENCES checks(id),
        FOREIGN KEY (currency_code) REFERENCES currencies(code)

);