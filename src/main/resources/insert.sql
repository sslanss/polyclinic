INSERT INTO specialities (speciality_name) VALUES
                                               ('Врач общей практики'),
                                               ('Педиатр'),
                                               ('Офтальмолог'),
                                               ('Отоларинголог'),
                                               ('Стоматолог'),
                                               ('Аллерголог-иммунолог'),
                                               ('Травматолог-ортопед'),
                                               ('Невролог'),
                                               ('Дерматолог'),
                                               ('Онколог');

INSERT INTO doctors (speciality_id, full_name, gender, phone_number)
VALUES
    ((SELECT speciality_id FROM specialities WHERE speciality_name = 'Врач общей практики'), 'Иванов Иван Иванович', 'male', '1234567890'),
    ((SELECT speciality_id FROM specialities WHERE speciality_name = 'Педиатр'), 'Петров Петр Петрович', 'male', '2345678901'),
    ((SELECT speciality_id FROM specialities WHERE speciality_name = 'Офтальмолог'), 'Сидорова Анна Александровна', 'female', '3456789012'),
    ((SELECT speciality_id FROM specialities WHERE speciality_name = 'Отоларинголог'), 'Козлова Елена Владимировна', 'female', '4567890123'),
    ((SELECT speciality_id FROM specialities WHERE speciality_name = 'Стоматолог'), 'Смирнов Александр Игоревич', 'male', '5678901234');
