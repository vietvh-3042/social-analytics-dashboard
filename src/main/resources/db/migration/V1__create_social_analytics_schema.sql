CREATE TABLE admin_users (
    id UUID PRIMARY KEY,
    facebook_subject VARCHAR(128) NOT NULL UNIQUE,
    display_name VARCHAR(200) NOT NULL,
    email VARCHAR(320),
    picture_url VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE posts (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES admin_users(id) ON DELETE CASCADE,
    post_url VARCHAR(2048) NOT NULL,
    title VARCHAR(240),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    imported_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uq_posts_owner_url UNIQUE (owner_id, post_url)
);

CREATE INDEX idx_posts_owner_imported ON posts(owner_id, imported_at DESC);

CREATE TABLE crawl_settings (
    admin_id UUID PRIMARY KEY REFERENCES admin_users(id) ON DELETE CASCADE,
    interval_minutes INTEGER NOT NULL DEFAULT 60 CHECK (interval_minutes BETWEEN 1 AND 1440),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    last_run_at TIMESTAMPTZ,
    next_run_at TIMESTAMPTZ
);

CREATE INDEX idx_crawl_settings_due ON crawl_settings(next_run_at) WHERE enabled = TRUE;

CREATE TABLE sync_runs (
    id UUID PRIMARY KEY,
    admin_id UUID NOT NULL REFERENCES admin_users(id) ON DELETE CASCADE,
    source VARCHAR(16) NOT NULL CHECK (source IN ('MANUAL', 'SCHEDULED')),
    status VARCHAR(16) NOT NULL CHECK (status IN ('QUEUED', 'COMPLETED', 'FAILED')),
    started_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    snapshots_created INTEGER NOT NULL DEFAULT 0,
    error_summary VARCHAR(500)
);

CREATE INDEX idx_sync_runs_admin_started ON sync_runs(admin_id, started_at DESC);

CREATE TABLE metric_snapshots (
    id UUID PRIMARY KEY,
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    likes BIGINT NOT NULL CHECK (likes >= 0),
    comments BIGINT NOT NULL CHECK (comments >= 0),
    shares BIGINT NOT NULL CHECK (shares >= 0),
    followers BIGINT NOT NULL CHECK (followers >= 0),
    captured_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_metric_snapshots_post_captured ON metric_snapshots(post_id, captured_at DESC);
