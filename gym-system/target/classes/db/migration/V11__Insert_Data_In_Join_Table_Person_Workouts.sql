INSERT INTO person_workouts (person_id, workout_id)
SELECT
    p.id AS person_id,
    w.id AS workout_id
FROM
    (SELECT id FROM person WHERE id <= 12) p
CROSS JOIN
    (SELECT id FROM workouts ORDER BY RAND() LIMIT 20) w;

-- Associar 3 livros às demais pessoas
INSERT INTO person_workouts (person_id, workout_id)
SELECT
    p.id AS person_id,
    w.id AS workout_id
FROM
    (SELECT id FROM person WHERE id > 12) p
CROSS JOIN
    (SELECT id FROM workouts ORDER BY RAND() LIMIT 3) w;