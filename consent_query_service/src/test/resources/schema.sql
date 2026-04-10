DROP TABLE IF EXISTS consent_projection CASCADE;
DROP TABLE IF EXISTS event CASCADE;

CREATE TABLE event (
       id            BIGSERIAL       PRIMARY KEY,
       stream_id     VARCHAR(255)    NOT NULL,
       version       BIGINT          NOT NULL,
       user_id       BIGINT          NOT NULL,
       event_type    VARCHAR(100)    NOT NULL,
       data_category VARCHAR(100)    NOT NULL,
       finality      VARCHAR(100)    NOT NULL,
       payload      VARCHAR(1000)   NOT NULL,
       issued_by    VARCHAR(500)    NOT NULL,
       occurred_at  TIMESTAMPTZ     NOT NULL,
       created_at   TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
       CONSTRAINT uq_event_stream_version UNIQUE (stream_id, version)
);

CREATE TABLE consent_projection (
        id              BIGSERIAL       PRIMARY KEY,
        user_id         BIGINT          NOT NULL,
        data_category  VARCHAR(100)   NOT NULL,
        finality       VARCHAR(100)   NOT NULL,
        status         VARCHAR(50)    NOT NULL,
        legal_basis   VARCHAR(100)   NOT NULL,
        granted_at    TIMESTAMPTZ,
        revoked_at    TIMESTAMPTZ,
        last_event_id BIGINT,
        version       BIGINT         NOT NULL,
        updated_at    TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
        CONSTRAINT uq_consent_projection UNIQUE (user_id, data_category, finality)
);

CREATE INDEX idx_projection_user_id    ON consent_projection (user_id);
CREATE INDEX idx_projection_status  ON consent_projection (status);
CREATE INDEX idx_projection_user_status ON consent_projection (user_id, status);