create SCHEMA IF NOT EXISTS db
    AUTHORIZATION postgres;

create TABLE IF NOT EXISTS db.invoice
(
    id     character varying(255) COLLATE pg_catalog."default" NOT NULL,
    sum    double precision,
    "time" timestamp without time zone,
    CONSTRAINT invoice_key PRIMARY KEY (id)
)
    TABLESPACE pg_default;

alter table IF EXISTS db.invoice
    OWNER to postgres;

create TABLE IF NOT EXISTS db.pen
(
    id         character varying(256) COLLATE pg_catalog."default" NOT NULL,
    count      integer,
    price      double precision,
    title      character varying(50) COLLATE pg_catalog."default",
    brand      character varying(50) COLLATE pg_catalog."default",
    type       character varying(50) COLLATE pg_catalog."default",
    color      character varying(50) COLLATE pg_catalog."default",
    invoice_id character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT pen_key PRIMARY KEY (id),
    CONSTRAINT invoice_id FOREIGN KEY (invoice_id)
        REFERENCES db.invoice (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete SET NULL
        NOT DEFERRABLE
)
    TABLESPACE pg_default;

alter table IF EXISTS db.pen
    OWNER to postgres;


create TABLE IF NOT EXISTS db.tea
(
    id         character varying(256) COLLATE pg_catalog."default" NOT NULL,
    count      integer,
    price      double precision,
    title      character varying(50) COLLATE pg_catalog."default",
    brand      character varying(50) COLLATE pg_catalog."default",
    type       character varying(50) COLLATE pg_catalog."default",
    invoice_id character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT tea_key PRIMARY KEY (id),
    CONSTRAINT invoice_id FOREIGN KEY (invoice_id)
        REFERENCES db.invoice (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete SET NULL
        NOT DEFERRABLE
)
    TABLESPACE pg_default;

alter table IF EXISTS db.tea
    OWNER to postgres;


create TABLE IF NOT EXISTS db.phone
(
    id           character varying(100) COLLATE pg_catalog."default" NOT NULL,
    count        integer,
    price        double precision,
    manufacturer character varying(50) COLLATE pg_catalog."default",
    title        character varying(50) COLLATE pg_catalog."default",
    model        character varying(50) COLLATE pg_catalog."default",
    invoice_id   character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT phone_key PRIMARY KEY (id),
    CONSTRAINT invoice_id FOREIGN KEY (invoice_id)
        REFERENCES db.invoice (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete SET NULL
        NOT DEFERRABLE
)
    TABLESPACE pg_default;

alter table IF EXISTS db.phone
    OWNER to postgres;