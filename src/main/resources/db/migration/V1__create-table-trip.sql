CREATE TABLE trips (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    owner_name VARCHAR(255) NOT NULL,
    owner_email VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    is_confirmed BOOLEAN NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL
);
