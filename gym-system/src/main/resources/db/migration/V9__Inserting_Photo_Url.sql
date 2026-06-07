-- Atualizar os campos de photo_url para as 12 primeiras pessoas famosas
UPDATE person
SET photo_url = CASE
    WHEN id = 1 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Ayrton_Senna.jpg'
    WHEN id = 2 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Leonardo_da_Vinci.png'
    WHEN id = 3 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Indira_Gandhi.jpg'
    WHEN id = 4 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Mahatma_Gandhi.jpg'
    WHEN id = 5 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Muhammad_Ali.jpg'
    WHEN id = 6 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Nelson_Mandela.jpg'
    WHEN id = 7 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Nikola_Tesla.jpeg'
    WHEN id = 8 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Yuri_Gagarin.jpg'
    WHEN id = 9 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Neil_Armstrong.jpg'
    WHEN id = 10 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Valentina_Tereshkova.jpg'
    WHEN id = 11 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Marie_Curie.jpg'
    WHEN id = 12 THEN 'https://henriquesales2.github.io/springboot-gym-system/photos/Ada_Lovelace.png'

    ELSE photo_url
END
WHERE id <= 12;