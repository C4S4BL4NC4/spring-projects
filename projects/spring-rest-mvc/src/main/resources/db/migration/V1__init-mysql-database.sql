drop table if exists beer;
drop table if exists customer;

create table beer (
    id                 varchar(36)    not null,
    version            integer,
    beer_name          varchar(50)    not null,
    beer_style         varchar(255)   not null,
    upc                varchar(255)   not null,
    quantity_on_hand   integer,
    price              decimal(38,2)  not null,
    created_at         datetime(6),
    updated_at         datetime(6),
    primary key (id)
) engine = InnoDB;

create table customer (
    id                  varchar(36)  not null,
    version             integer,
    name                varchar(255),
    created_date        datetime(6),
    last_modified_date  datetime(6),
    primary key (id)
) engine = InnoDB;
