-- create business tables
create table categories
(
    id bigserial not null primary key,
    name  varchar(50) not null constraint uk_t8o6pivur7nn124jehx7cygw5 unique,
    slug  varchar(50) not null,
    image text
);

alter table categories owner to postgres;

create table difficulties
(
    id bigserial not null primary key,
    level varchar(255) not null,
    point integer not null
);

alter table difficulties owner to postgres;

create table roles
(
    id   bigserial not null primary key,
    name varchar(255) not null
);

alter table roles  owner to postgres;

create table tags
(
    id   bigserial not null primary key,
    name varchar(50) not null constraint uk_t48xdq560gs3gap9g7jg36kgc unique
);

alter table tags owner to postgres;

create table users
(
    id           bigserial not null primary key,
    username     varchar(50) not null constraint uk_r43af9ap4edm43mmtq01oddj6 unique,
    password     varchar(80)  not null,
    full_name    varchar(100) not null,
    email        varchar(255) not null constraint uk_6dotkott2kjsp8vw4d0m25fb7 unique,
    phone_number varchar(12),
    image        text,
    active       boolean,
    role_id      bigint constraint fkp56c1712k691lhsyewcssf40f references roles
);

alter table users owner to postgres;

create table quizzes
(
    id            bigserial not null primary key,
    title         varchar(255) not null,
    description   text,
    status        varchar(255),
    image         text,
    category_id   bigint constraint fkpo9fnqd9hnnmg8qxiyue40cot references categories,
    instructor_id bigint  constraint fktj231ev59xspjboiqfpha7qmc references users
);

alter table quizzes owner to postgres;

create table questions
(
    id            bigserial not null primary key,
    title         varchar(255) not null,
    is_multiple   boolean,
    difficulty_id bigint constraint fkqhglpf6seou0cwtxnpv8aks5b references difficulties,
    quiz_id       bigint constraint fkn3gvco4b0kewxc0bywf1igfms references quizzes,
    tag_id        bigint constraint fkbfo6d7hrwmi0pu9ii54du351o references tags
);

alter table questions owner to postgres;

create table answers
(
    id          bigserial not null primary key,
    text        text   not null,
    is_correct  boolean,
    question_id bigint constraint fk3erw1a3t0r78st8ty27x6v3g1 references questions
);

create table users_quizzes
(
    quiz_id            bigint not null constraint fk2pe7exldag7w0strpafrshmhh references quizzes,
    user_id            bigint not null constraint fkrvmgvwqnme3q2omcqtlo3a43v references users,
    max_score          integer,
    remaining_time     integer,
    recent_active_date timestamp,
    primary key (quiz_id, user_id)
);

alter table users_quizzes owner to postgres;

alter table answers owner to postgres;

-- create audit tables
create table revinfo
(
    rev      integer not null primary key,
    revtstmp bigint
);

alter table revinfo owner to postgres;

create table answers_aud
(
    id          bigint  not null,
    rev         integer not null constraint fkini8u1yepd95v0xbkyh8cljqq references revinfo,
    revtype     smallint,
    is_correct  boolean,
    text        text,
    question_id bigint,
    primary key (id, rev)
);

alter table answers_aud owner to postgres;

create table categories_aud
(
    id      bigint  not null,
    rev     integer not null constraint fk6ti58h8w0q47oacscu06hcite references revinfo,
    revtype smallint,
    image   text,
    name    varchar(50),
    slug    varchar(50),
    primary key (id, rev)
);

alter table categories_aud owner to postgres;

create table difficulties_aud
(
    id      bigint  not null,
    rev     integer not null constraint fk7b7mkqjtkhvk29lcem0vjfn24 references revinfo,
    revtype smallint,
    level   varchar(255),
    point   integer,
    primary key (id, rev)
);

alter table difficulties_aud owner to postgres;

create table questions_aud
(
    id            bigint  not null,
    rev           integer not null constraint fk17yard257f6u58jc6oxmbsofj references revinfo,
    revtype       smallint,
    is_multiple   boolean,
    title         varchar(255),
    difficulty_id bigint,
    quiz_id       bigint,
    tag_id        bigint,
    primary key (id, rev)
);

alter table questions_aud owner to postgres;

create table quizzes_aud
(
    id            bigint  not null,
    rev           integer not null constraint fk2vgrsvfrjdxyr831iaum96m8v references revinfo,
    revtype       smallint,
    description   text,
    image         text,
    status        varchar(255),
    title         varchar(255),
    category_id   bigint,
    instructor_id bigint,
    primary key (id, rev)
);

alter table quizzes_aud owner to postgres;

create table roles_aud
(
    id      bigint  not null,
    rev     integer not null constraint fkt0mnl3rej2p0h9gxnbalf2kdd references revinfo,
    revtype smallint,
    name    varchar(255),
    primary key (id, rev)
);

alter table roles_aud owner to postgres;

create table tags_aud
(
    id      bigint  not null,
    rev     integer not null constraint fk1jtbkw86fnuap1gio8ucmbj86 references revinfo,
    revtype smallint,
    name    varchar(50),
    primary key (id, rev)
);

alter table tags_aud owner to postgres;

create table users_aud
(
    id           bigint  not null,
    rev          integer not null constraint fkc4vk4tui2la36415jpgm9leoq references revinfo,
    revtype      smallint,
    active       boolean,
    email        varchar(255),
    full_name    varchar(100),
    image        text,
    password     varchar(80),
    phone_number varchar(12),
    username     varchar(50),
    role_id      bigint,
    primary key (id, rev)
);

alter table users_aud owner to postgres;

create table users_quizzes_aud
(
    quiz_id            bigint  not null,
    user_id            bigint  not null,
    rev                integer not null constraint fkq64p76159rrlinpx1b44cmwo6 references revinfo,
    revtype            smallint,
    max_score          integer,
    recent_active_date timestamp,
    remaining_time     integer,
    primary key (quiz_id, user_id, rev)
);

alter table users_quizzes_aud owner to postgres;

