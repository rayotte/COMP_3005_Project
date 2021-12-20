-- Ryan Ayotte
-- 101073548
-- 3005 Final Project
-- Sun Dec 19, 2021
create table client
(
    ID              varchar(5),
    name            varchar(20),
    username        varchar(20),
    password        varchar(20),
    ship_address    varchar(50),
    bill_address    varchar(50),
    primary key (ID)
);

create table owner
(
    ID          varchar(5),
    username    varchar(20),
    password    varchar(20),
    salary      numeric(8,2) check (salary > 30000),
    primary key (ID)
);

create table publisher
(
    ID          varchar(5),
    name        varchar(30) unique,
    email       varchar(30),
    phone       varchar(10),
    address     varchar(50),
    bank_info   varchar(30),
    primary key (ID, name)
);

create table book
(
    ISBN            varchar(13),
    title           varchar(20),
    author          varchar(20),
    genre           varchar(10),
    publisher       varchar(30),
    price           numeric(4,0),
    page_count      varchar(4),
    pub_percent     numeric(2,0),
    primary key (ISBN),
    foreign key (publisher) references publisher(name)
);

create table stock
(
    ISBN        varchar(13),
    quantity    numeric(5,0) check (quantity > -1),
    primary key (ISBN),
    foreign key (ISBN) references book
        on delete cascade
);

create table publishes
(
    ISBN            varchar(13),
    pub_name        varchar(30),
    primary key (ISBN),
    foreign key (ISBN) references book,
    foreign key (pub_name) references publisher(name)
);

create table orders
(
    ID              varchar(5),
    total_cost      numeric(6,0),
    ship_address    varchar(50),
    bill_address    varchar(50),
    book_list       varchar(80),
    client_id       varchar(5),
    primary key (ID),
    foreign key (client_id) references client (ID)
);

create table place_order
(
    client_id       varchar(5),
    order_id        varchar(5),
    primary key (client_id, order_id),
    foreign key (client_id) references client (ID),
    foreign key (order_id) references orders (ID)
        on delete cascade
);

create table order_book
(
    ISBN        varchar(13),
    order_id    varchar(5),
    primary key (ISBN, order_id),
    foreign key (ISBN) references book
        on delete cascade,
    foreign key (order_id) references orders(ID)
        on delete cascade
);

create table store
(
    store_id            varchar(5),
    primary key (store_id)
);

create table manages
(
    store_id    varchar(5),
    owner_id    varchar(5),
    primary key (store_id, owner_id),
    foreign key (store_id) references store
        on delete set null,
    foreign key (owner_id) references owner (ID)
        on delete set null
);

create table report
(
    report_id       varchar(5),
    store_id        varchar(5),
    report_month    varchar(14),
    report_sales    numeric(5,0),
    primary key (report_id),
    foreign key (store_id) references store
        on delete set null
);

create table generate
(
    store_id    varchar(5),
    report_id   varchar(5),
    primary key (store_id, report_id),
    foreign key (store_id) references store
        on delete set null,
    foreign key (report_id) references report
        on delete set null
);

create table logs
(
    report_id       varchar(5),
    order_id        varchar(5),
    primary key (order_id, report_id),
    foreign key (order_id) references orders
        on delete set null,
    foreign key (report_id) references report
        on delete set null
);


-- Insert some values into the database
delete from store;
delete from owner;
delete from book;
delete from publisher;
delete from orders;
delete from client;
delete from stock;
delete from manages;
delete from publishes;
delete from order_book;
delete from place_order;
delete from report;
delete from generate;
delete from logs;

insert into client values('10001', 'Harry Potter', 'HPotter', 'quidditch', 'Godrics Hollow', 'Gringotts');
insert into client values('10002', 'Hermione Granger', 'Bookworm', 'library', 'Hampstead, London', 'Hampstead, London');

insert into owner values('20001', 'APWBD', 'elder', 95000);
insert into owner values('20002', 'ProfCat', 'transfiguration', 86000);

insert into publisher values('30001', 'HarperCollins Publishers', 'spsales@harpercollins.com', '8002427737','195 Broadway New York, NY 10007', 'Bank of America: 8721694');
insert into publisher values('30002', 'Bantam Books', 'sales@bantambooks.com', '8008927823','408 New York, NY 10877', 'TD Bank: 7815479');

insert into book values ('9780007458424', 'The Hobbit', 'J. R. R. Tolkien', 'Fantasy', 'HarperCollins Publishers', '11.0', '368', '3.0');
insert into book values ('9780553593716', 'Game of Thrones', 'George R. R. MArtin', 'Fantasy', 'Bantam Books', '12.0', '864', '3.0');

insert into stock values('9780007458424',20);
insert into stock values('9780553593716',40);

insert into publishes values('9780007458424','HarperCollins Publishers');
insert into publishes values('9780553593716','Bantam Books');

insert into orders values('40001', 12, 'Godrics Hollow', 'gringotts', 'Game of Thrones,', '10001');
insert into orders values('40002', 23, 'Hampstead, London', 'Hampstead, London', 'Game of Thrones, The Hobbit,', '10002');

insert into place_order values('10001','40001');
insert into place_order values('10002','40002');

insert into order_book values('9780553593716', '40001');
insert into order_book values('9780553593716', '40002');
insert into order_book values('9780007458424', '40002');

insert into store values ('50001');

insert into manages values ('50001','20001');
insert into manages values ('50001','20002');

insert into report values('60001','50001','August',428);
insert into generate values('50001','60001');
