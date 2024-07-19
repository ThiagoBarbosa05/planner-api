CREATE TABLE participants(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    is_confirmed BOOLEAN NOT NULL,
    trip_id uuid,
    FOREIGN KEY (trip_id) REFERENCES trips(id)
);