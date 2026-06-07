CREATE TABLE IF NOT EXISTS workouts (
    `id` INT(10) AUTO_INCREMENT PRIMARY KEY,
    `exercise_name` VARCHAR(100) NOT NULL,
    `muscle_group` VARCHAR(50) NOT NULL,
    `equipment` VARCHAR(50) NOT NULL,
    `difficulty` VARCHAR(20) NOT NULL
);