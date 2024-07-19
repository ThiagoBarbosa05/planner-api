CREATE TABLE activities(
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    occurs_at TIMESTAMP,
    trip_id uuid,
    FOREIGN KEY (trip_id) REFERENCES trips(id)
);