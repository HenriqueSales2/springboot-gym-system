CREATE TABLE IF NOT EXISTS gym_workouts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    exercise_name VARCHAR(100) NOT NULL,
    muscle_group VARCHAR(50) NOT NULL,
    equipment VARCHAR(50) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);