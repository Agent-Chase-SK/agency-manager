CREATE TABLE "Agent" (
    "id" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS INDETINTY,
    "codeName" VARCHAR(255),
    "status" VARCHAR(31)
);

CREATE TABLE "Mission" (
    "id" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS INDETINTY,
    "codeName" VARCHAR(255),
    "status" VARCHAR(31),
    "date" DATE,
    "location" VARCHAR(255),
    "agentId" BIGINT
);
