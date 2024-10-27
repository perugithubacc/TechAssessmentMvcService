CREATE SEQUENCE customer_id_seq
    START WITH 1000000000
    INCREMENT BY 1;

CREATE TABLE Customer
(
    customer_id    BIGINT PRIMARY KEY DEFAULT nextval('customer_id_seq'),
    document_id    VARCHAR(255) NOT NULL UNIQUE,
    first_name     VARCHAR(255) NOT NULL,
    middle_name    VARCHAR(255),
    last_name      VARCHAR(255) NOT NULL,
    gender         VARCHAR(255) NOT NULL,
    birthday       DATE         NOT NULL,
    mobile_number  VARCHAR(255) NOT NULL,
    office_number  VARCHAR(255),
    personal_email VARCHAR(255) NOT NULL UNIQUE,
    office_email   VARCHAR(255),
    family_members TEXT,
    address        TEXT,
    customer_type  VARCHAR(255) NOT NULL,
    age            INTEGER,
    CONSTRAINT customer_doc_unique UNIQUE (document_id),
    CONSTRAINT customer_email_unique UNIQUE (personal_email)
);

CREATE INDEX customer_idx_customer_id ON Customer (customer_id);
