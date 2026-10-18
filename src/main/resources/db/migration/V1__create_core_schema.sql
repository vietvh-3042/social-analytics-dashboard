create table app_user (
    id uuid primary key,
    username varchar(120) not null unique,
    display_name varchar(160) not null,
    role varchar(20) not null,
    created_at timestamptz not null
);

create table social_account (
    id uuid primary key,
    user_id uuid not null references app_user(id),
    provider varchar(30) not null,
    external_id varchar(180) not null,
    access_token_reference varchar(255),
    last_synced_at timestamptz,
    connected_at timestamptz not null default current_timestamp,
    constraint uq_social_account_provider_external unique (provider, external_id)
);

create table post (
    id uuid primary key,
    social_account_id uuid references social_account(id),
    source varchar(30) not null,
    external_id varchar(180),
    content varchar(5000) not null,
    published_at timestamptz not null,
    created_at timestamptz not null
);

create unique index uq_post_source_external on post(source, external_id) where external_id is not null;

create table social_metric (
    id uuid primary key,
    post_id uuid not null references post(id) on delete cascade,
    likes bigint not null check (likes >= 0),
    shares bigint not null check (shares >= 0),
    comments bigint not null check (comments >= 0),
    followers bigint not null check (followers >= 0),
    measured_at timestamptz not null,
    provider varchar(30) not null
);

create index ix_social_metric_post_measured on social_metric(post_id, measured_at desc);

create table import_batch (
    id uuid primary key,
    filename varchar(255) not null,
    status varchar(20) not null,
    imported_rows integer not null default 0,
    rejected_rows integer not null default 0,
    completed_at timestamptz
);
