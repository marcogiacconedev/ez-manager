-- USER
CREATE TABLE public."User" (
	id uuid NOT NULL,
	email varchar NOT NULL,
	name varchar NOT NULL,
	"password" varchar NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id)
);
