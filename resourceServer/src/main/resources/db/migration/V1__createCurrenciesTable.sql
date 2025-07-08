CREATE TABLE currencies (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    sizePrecision INT NOT NULL DEFAULT 2
);

INSERT INTO currencies (code, name, sizePrecision) VALUES
('USD', 'US Dollar', 2),
('RUB', 'Ruble', 2),
('BTC', 'Bitcoin', 10);