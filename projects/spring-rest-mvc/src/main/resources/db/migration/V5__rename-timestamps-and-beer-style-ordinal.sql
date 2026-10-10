-- Match renamed entity timestamp fields
ALTER TABLE beer RENAME COLUMN created_at TO created_date;
ALTER TABLE beer RENAME COLUMN updated_at TO update_date;
ALTER TABLE customer RENAME COLUMN last_modified_date TO update_date;

-- beer_style is now stored as the BeerStyle enum ordinal (SMALLINT)
UPDATE beer SET beer_style = CASE beer_style
    WHEN 'LAGER'    THEN '0'
    WHEN 'STOUT'    THEN '1'
    WHEN 'MALT'     THEN '2'
    WHEN 'IPA'      THEN '3'
    WHEN 'WHEAT'    THEN '4'
    WHEN 'PILSNER'  THEN '5'
    WHEN 'PORTER'   THEN '6'
    WHEN 'ALE'      THEN '7'
    WHEN 'SAISON'   THEN '8'
    WHEN 'PALE_ALE' THEN '9'
END;
ALTER TABLE beer MODIFY beer_style smallint NOT NULL;
