create table metric_aggregate (
    bucket timestamptz not null,
    source varchar(30) not null,
    likes bigint not null check (likes >= 0),
    shares bigint not null check (shares >= 0),
    followers bigint not null check (followers >= 0),
    primary key (bucket, source)
);
