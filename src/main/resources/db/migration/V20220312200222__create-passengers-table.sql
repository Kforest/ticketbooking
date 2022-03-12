create table passengers
(
    id        nvarchar(64) primary key not null,
    order_id nvarchar(64) not null,
    name    nvarchar(32) not null,
    id_card_number nvarchar (32) NULL,
    create_at datetime2 (7) NULL
)
