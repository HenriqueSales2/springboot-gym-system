INSERT INTO `users` (`user_name`, `full_name`, `password`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`) VALUES
	('john', 'John Doe', '{pbkdf2}1e6193222a462872a4aa029ac46c2d7fc32a767c6aa70536fa7a865b2bcea8da4c46e63972134157', true, true, true, true),
	('mary', 'Mary Doe', '{pbkdf2}b2e6a90478d44483d3dcdb5a277212bcd46b633eb6d3d977aec76675b284faa7902428908e6a59ea', true, true, true, true);

-- admin123: {pbkdf2}1e6193222a462872a4aa029ac46c2d7fc32a767c6aa70536fa7a865b2bcea8da4c46e63972134157
-- user123:  {pbkdf2}b2e6a90478d44483d3dcdb5a277212bcd46b633eb6d3d977aec76675b284faa7902428908e6a59ea