CREATE TABLE IF NOT EXISTS DM_OPERATION_ARCH (
    ID INTEGER NOT NULL,
    TYPE VARCHAR(20) NOT NULL,
    CREATED_TIMESTAMP TIMESTAMP(0) NOT NULL,
    RECEIVED_TIMESTAMP TIMESTAMP(0) NULL,
    OPERATION_CODE VARCHAR(50) NOT NULL,
    INITIATED_BY VARCHAR(100) NULL,
    OPERATION_DETAILS BYTEA DEFAULT NULL,
    OPERATION_PROPERTIES BYTEA DEFAULT NULL,
    ENABLED BOOLEAN NOT NULL DEFAULT FALSE,
    ARCHIVED_AT TIMESTAMP(0) DEFAULT NOW()
);

CREATE INDEX IDX_OPR_ARC ON DM_OPERATION_ARCH(ARCHIVED_AT);

CREATE TABLE IF NOT EXISTS DM_ENROLMENT_OP_MAPPING_ARCH (
    ID INTEGER NOT NULL,
    ENROLMENT_ID INTEGER NOT NULL,
    OPERATION_ID INTEGER NOT NULL,
    STATUS VARCHAR(50) NULL,
    CREATED_TIMESTAMP INTEGER NOT NULL,
    UPDATED_TIMESTAMP INTEGER NOT NULL,
    ARCHIVED_AT TIMESTAMP(0) DEFAULT NOW()
);

CREATE INDEX IDX_EN_OP_MAP_ARCH ON DM_ENROLMENT_OP_MAPPING_ARCH(ARCHIVED_AT);

CREATE TABLE IF NOT EXISTS DM_DEVICE_OPERATION_RESPONSE_ARCH  (
    ID  INT NOT NULL,
    ENROLMENT_ID  INTEGER NOT NULL,
    OPERATION_ID  INTEGER NOT NULL,
    OPERATION_RESPONSE VARCHAR(4096) DEFAULT NULL,
    RECEIVED_TIMESTAMP  TIMESTAMP(0) NULL,
    ARCHIVED_AT TIMESTAMP(0) DEFAULT NOW(),
    IS_LARGE_RESPONSE BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX IDX_OPR_RES_ARCH ON DM_DEVICE_OPERATION_RESPONSE_ARCH(ARCHIVED_AT);

CREATE TABLE DM_DEVICE_OPERATION_RESPONSE_LARGE_ARCH (
    ID INT NOT NULL,
    OPERATION_RESPONSE BYTEA DEFAULT NULL,
    ARCHIVED_AT TIMESTAMP(0) DEFAULT NOW()
);

CREATE INDEX IDX_OPR_RES_LRG_ARCH ON DM_DEVICE_OPERATION_RESPONSE_LARGE_ARCH(ARCHIVED_AT);

CREATE TABLE IF NOT EXISTS DM_NOTIFICATION_ARCH (
    NOTIFICATION_ID INTEGER NOT NULL,
    DEVICE_ID INTEGER NOT NULL,
    OPERATION_ID INTEGER NOT NULL,
    TENANT_ID INTEGER NOT NULL,
    STATUS VARCHAR(10) NULL,
    DESCRIPTION VARCHAR(1000) NULL,
    ARCHIVED_AT TIMESTAMP(0) DEFAULT NOW()
);

CREATE INDEX IDX_NOT_ARCH ON DM_NOTIFICATION_ARCH(ARCHIVED_AT);