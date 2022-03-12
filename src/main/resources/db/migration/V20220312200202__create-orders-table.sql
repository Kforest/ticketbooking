create table orders
(
    id        nvarchar(64) primary key not null,
    flight_id nvarchar(64) not null,
    status    nvarchar(16) not null,
    create_at datetime2 (7) NULL,
    update_at datetime2 (7) NULL,
)
