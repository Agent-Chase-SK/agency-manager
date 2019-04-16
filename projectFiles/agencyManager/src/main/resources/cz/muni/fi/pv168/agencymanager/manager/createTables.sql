create table Agent (
    id bigint not null primary key generated always as identity,
    codeName varchar(255),
    status varchar(31)
);

create table Mission (
    id bigint not null primary key generated always as identity,
    codeName varchar(255),
    status varchar(31),
    date date,
    location varchar(255),
	agentId bigint,
	
    CONSTRAINT AGENT_FK
	FOREIGN KEY (agentId)
	REFERENCES Agent (id)
);
