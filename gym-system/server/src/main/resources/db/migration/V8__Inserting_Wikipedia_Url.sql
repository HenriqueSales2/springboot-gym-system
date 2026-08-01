UPDATE person
SET wikipedia_profile_url = CASE
    WHEN id = 1 THEN 'https://en.wikipedia.org/wiki/Arnold_Schwarzenegger'
    WHEN id = 2 THEN 'https://en.wikipedia.org/wiki/Ronnie_Coleman'
    WHEN id = 3 THEN 'https://en.wikipedia.org/wiki/Jay_Cutler_(bodybuilder)'
    WHEN id = 4 THEN 'https://en.wikipedia.org/wiki/Phil_Heath'
    WHEN id = 5 THEN 'https://en.wikipedia.org/wiki/Dorian_Yates'
    WHEN id = 6 THEN 'https://en.wikipedia.org/wiki/Lee_Haney'
    WHEN id = 7 THEN 'https://en.wikipedia.org/wiki/Franco_Columbu'
    WHEN id = 8 THEN 'https://en.wikipedia.org/wiki/Derek_Lunsford'
    WHEN id = 9 THEN 'https://en.wikipedia.org/wiki/Chris_Bumstead'
    WHEN id = 10 THEN 'https://en.wikipedia.org/wiki/Ramon_Dino'
    WHEN id = 11 THEN 'https://en.wikipedia.org/wiki/Flex_Wheeler'
    WHEN id = 12 THEN 'https://en.wikipedia.org/wiki/Iris_Kyle'
    ELSE wikipedia_profile_url
END
WHERE id <= 12;