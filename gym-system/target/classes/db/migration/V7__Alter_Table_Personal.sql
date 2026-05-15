ALTER TABLE `personal`
    ADD COLUMN `is_personal` BIT(1) NOT NULL DEFAULT b'1' AFTER `gender`;