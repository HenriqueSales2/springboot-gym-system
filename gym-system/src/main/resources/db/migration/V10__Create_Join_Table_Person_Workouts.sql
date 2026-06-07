CREATE TABLE IF NOT EXISTS `person_workouts` (
  `person_id` int(10) NOT NULL,
  `workout_id` int(10) NOT NULL,
  PRIMARY KEY (`person_id`, `workout_id`),
  FOREIGN KEY (`person_id`) REFERENCES `person`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`workout_id`) REFERENCES `workouts`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;