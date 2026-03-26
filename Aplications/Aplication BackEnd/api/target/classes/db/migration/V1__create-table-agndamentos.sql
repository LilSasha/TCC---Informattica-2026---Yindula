create table Agendamento(
    id int auto_increment primary key not null,
    cidadao varchar(255) not null,
    servico varchar(255) not null,
    numeroBI varchar(14) unique,
    data_agendada varchar(20) not null,
    hora_agendada varchar(20) not null,
    posto varchar(20) not null
)