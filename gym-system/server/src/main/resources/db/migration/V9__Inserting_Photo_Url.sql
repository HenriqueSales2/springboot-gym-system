-- Atualizar os campos de photo_url para as 12 primeiras pessoas famosas
UPDATE person
SET photo_url = CASE
    WHEN id = 1 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Arnold_Schwarzenegger.jpg'
    WHEN id = 2 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Ronnie_Coleman.jpg'
    WHEN id = 3 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Jay_Cutler.jpg'
    WHEN id = 4 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Phil_Heath.jpg'
    WHEN id = 5 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Dorian_Yates.png'
    WHEN id = 6 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Lee_Haney.jpg'
    WHEN id = 7 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Franco_Columbu.jpg'
    WHEN id = 8 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Derek_Lunsford.jpg'
    WHEN id = 9 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Chris_Bumstead.jpg'
    WHEN id = 10 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Ramon_Dino.jpg'
    WHEN id = 11 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Flex_Wheeler.jpg'
    WHEN id = 12 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Iris_Kyle.jpg'

    ELSE photo_url
END
WHERE id <= 12;