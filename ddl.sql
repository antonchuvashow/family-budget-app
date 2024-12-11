CREATE TABLE users
(
    username   VARCHAR(50) PRIMARY KEY NOT NULL,
    password   VARCHAR(255)            NOT NULL,
    full_name  VARCHAR(100)            NOT NULL,
    birth_date DATE                    NOT NULL CHECK (birth_date < CURRENT_DATE),
    sum        DECIMAL(10, 2)          NOT NULL DEFAULT 0.00
);

CREATE TABLE incomeCategory
(
    category_id SERIAL PRIMARY KEY,
    name        VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE expenseCategory
(
    entry_id SERIAL PRIMARY KEY,
    name     VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE income
(
    income_id      SERIAL PRIMARY KEY,
    user_id        VARCHAR(50)                       NOT NULL,
    amount         DECIMAL(10, 2) CHECK (amount > 0) NOT NULL,
    operation_date DATE                              NOT NULL DEFAULT CURRENT_DATE,
    category_id    INT                               NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (username),
    FOREIGN KEY (category_id) REFERENCES incomeCategory (category_id)
);

CREATE TABLE expense
(
    expense_id     SERIAL PRIMARY KEY,
    user_id        VARCHAR(50)                       NOT NULL,
    amount         DECIMAL(10, 2) CHECK (amount > 0) NOT NULL,
    operation_date DATE                              NOT NULL DEFAULT CURRENT_DATE,
    entry_id       INT                               NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (username),
    FOREIGN KEY (entry_id) REFERENCES expenseCategory (entry_id)
);




