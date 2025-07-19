CREATE TABLE equipment (
    id BIGSERIAL PRIMARY KEY,
    serial VARCHAR(50) UNIQUE,
    model VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION CHECK (latitude >= -90 AND latitude <= 90),
    longitude DOUBLE PRECISION CHECK (longitude >= -180 AND longitude <= 180),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE violation (
    id BIGSERIAL PRIMARY KEY,
    equipment_serial VARCHAR(50) NOT NULL REFERENCES equipment(serial),
    occurrence_date_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    measured_speed DOUBLE PRECISION,
    considered_speed DOUBLE PRECISION,
    regulated_speed DOUBLE PRECISION,
    picture TEXT NOT NULL,
    type VARCHAR(50) NOT NULL
);