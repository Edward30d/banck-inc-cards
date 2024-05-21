
CREATE TABLE IF NOT EXISTS card (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(16),
    product_id VARCHAR(6) UNIQUE NOT NULL,
    card_holder_name VARCHAR(100) NOT NULL,
    expiration_date DATE NOT NULL,
    creation_date DATE NOT NULL,
    state VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS recharge (
    id INT AUTO_INCREMENT PRIMARY KEY,
    previous_balance DECIMAL(10, 2),
    new_balance DECIMAL(10, 2),
    recharge_date DATE NOT NULL,
    card_id INT,
    FOREIGN KEY (card_id) REFERENCES card(id)
);

CREATE TABLE transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    card_id INT,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL,
    state VARCHAR(10) NOT NULL,
    FOREIGN KEY (card_id) REFERENCES card(id)
);

