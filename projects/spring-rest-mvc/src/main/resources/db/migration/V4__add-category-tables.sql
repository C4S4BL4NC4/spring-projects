drop table if exists beer_category;
drop table if exists category;

CREATE TABLE `category`
(
    id                 varchar(36) NOT NULL,
    description        varchar(50)  DEFAULT NULL,
    created_date       datetime(6)  DEFAULT NULL,
    last_modified_date datetime(6)  DEFAULT NULL,
    version            bigint       DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE `beer_category`
(
    beer_id     varchar(36) NOT NULL,
    category_id varchar(36) NOT NULL,
    PRIMARY KEY (beer_id, category_id),
    CONSTRAINT FOREIGN KEY (beer_id) REFERENCES beer (id),
    CONSTRAINT FOREIGN KEY (category_id) REFERENCES category (id)
) ENGINE = InnoDB;
