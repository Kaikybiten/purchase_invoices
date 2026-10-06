CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.invoice (
    id bigserial NOT NULL,
    access_token varchar(44) NOT NULL UNIQUE,
    invoice_entry_date date NOT NULL,
    CONSTRAINT invoice_pkey PRIMARY KEY (id)
);

CREATE TABLE public.product (
    id bigserial NOT NULL,
    name varchar(30) NOT NULL,
    code varchar(30) NOT NULL,
    measure varchar(10),
    unit_price numeric(10, 2) NOT NULL,
    purchase_date date DEFAULT CURRENT_DATE,
    CONSTRAINT product_pkey PRIMARY KEY (id)
);

CREATE TABLE public.recorded_purchases (
    id bigserial NOT NULL,
    id_product bigint NOT NULL,
    id_invoice bigint NOT NULL,
    purchase_date date NOT NULL,
    quantity numeric(38, 2) NOT NULL,
    total_price numeric(38, 2) NOT NULL,
    CONSTRAINT recorded_purchases_pkey PRIMARY KEY (id),
    CONSTRAINT recorded_purchases_id_product_fkey
       FOREIGN KEY (id_product) REFERENCES public.product(id),
    CONSTRAINT recorded_purchases_id_invoice_fkey
       FOREIGN KEY (id_invoice) REFERENCES public.invoice(id)
);