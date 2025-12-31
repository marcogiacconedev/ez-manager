-- USER
CREATE TABLE public.users (
	id uuid NOT NULL,
	user_email varchar NOT NULL,
	user_name varchar NOT NULL,
	pass_hash varchar NOT NULL,
	user_role varchar DEFAULT USER NOT NULL
);
