CREATE TABLE specialities (
                              speciality_id SERIAL PRIMARY KEY,
                              speciality_name TEXT UNIQUE NOT NULL
);

CREATE TYPE gender AS ENUM ('male', 'female');

CREATE TABLE doctors (
                         doctor_id SERIAL PRIMARY KEY,
                         speciality_id INT NOT NULL,
                         full_name TEXT NOT NULL,
                         gender gender,
                         phone_number VARCHAR(10),

                         FOREIGN KEY (speciality_id) REFERENCES specialities (speciality_id) ON DELETE CASCADE
);

CREATE TABLE patients (
                          insurance_policy_number VARCHAR(16) PRIMARY KEY,
                          full_name TEXT NOT NULL,
                          date_of_birth DATE NOT NULL,
                          gender gender,
                          phone_number VARCHAR(10),
                          address TEXT,
                          password TEXT
);

CREATE TYPE appointment_type AS ENUM ('scheduled', 'emergency');

CREATE TABLE appointments (
                              appointment_id SERIAL PRIMARY KEY,
                              doctor_id INT,
                              patient_id VARCHAR(16),
                              date_time TIMESTAMP WITH TIME ZONE NOT NULL,
                              recommendations TEXT NOT NULL,
                              appointment_type appointment_type,

                              FOREIGN KEY (doctor_id) REFERENCES doctors (doctor_id) ON DELETE CASCADE,
                              FOREIGN KEY (patient_id) REFERENCES patients (insurance_policy_number) ON DELETE CASCADE
);

CREATE TABLE diagnoses (
                           diagnosis_id SERIAL PRIMARY KEY,
                           diagnosis_name TEXT UNIQUE NOT NULL
);

CREATE TABLE diagnosing (
                            appointment_id INT,
                            diagnosis_id INT,

                            FOREIGN KEY (appointment_id) REFERENCES appointments (appointment_id) ON DELETE CASCADE,
                            FOREIGN KEY (diagnosis_id) REFERENCES diagnoses (diagnosis_id) ON DELETE CASCADE,
                            PRIMARY KEY(appointment_id, diagnosis_id)
)