CREATE TABLE public.app (
	uid varchar(255) NOT NULL,
	dt_alteracao timestamp NULL,
	dt_criacao timestamp NULL,
	dt_exclusao timestamp NULL,
	nome varchar(255) NULL,
	"token" varchar(255) NULL,
	CONSTRAINT app_pkey PRIMARY KEY (uid)
);

INSERT INTO public.app
(uid, dt_alteracao, dt_criacao, dt_exclusao, nome, "token")
VALUES('26ed47e2-281eeab5e652', null, '2022-11-19 18:34', null, 'Tua Agenda', 'q4YZ$ys5etJMrmnV');


CREATE TABLE public.produto (
	id varchar(255) NOT NULL,
	dt_alteracao timestamp NULL,
	dt_criacao timestamp NULL,
	dt_exclusao timestamp NULL,
	limite_aplicacao_bonus_padrinho int4 NULL DEFAULT 3,
	limite_aplicacoes_afiliados int4 NULL DEFAULT '-1'::integer,
	moeda_afiliado int4 NULL,
	moeda_padrinho int4 NULL,
	nome varchar(255) NULL,
	post_webhock_padrinho varchar(255) NULL,
	app_uid varchar(255) NOT NULL,
	CONSTRAINT produto_pkey PRIMARY KEY (app_uid, id),
	CONSTRAINT fkmq9lra6mv4o4q9xoxvjv70pta FOREIGN KEY (app_uid) REFERENCES public.app(uid)
);

INSERT INTO public.produto
(id, dt_alteracao, dt_criacao, dt_exclusao, limite_aplicacao_bonus_padrinho, limite_aplicacoes_afiliados, moeda_afiliado, moeda_padrinho, nome, post_webhock_padrinho, app_uid)
VALUES('ff0c-4581-97c7', null, '2022-11-19 18:36', null, 3, 1, 20, 2, 'Black Friday', 'Promoção', '26ed47e2-281eeab5e652');

CREATE TABLE public.cliente (
	id varchar(255) NOT NULL,
	dt_alteracao timestamp NULL,
	dt_criacao timestamp NULL,
	dt_exclusao timestamp NULL,
	email varchar(255) NULL,
	nome varchar(255) NULL,
	app_uid varchar(255) NOT NULL,
	CONSTRAINT cliente_pkey PRIMARY KEY (app_uid, id),
	CONSTRAINT fk37lup41rd0w7s6h3koqlfb35v FOREIGN KEY (app_uid) REFERENCES public.app(uid)
);

INSERT INTO public.cliente
(id, dt_alteracao, dt_criacao, dt_exclusao, email, nome, app_uid)
VALUES('4581-97c7', NULL, '2022-11-19 23:08:16.884', NULL, 'maria@gmail.com', 'Maria', '26ed47e2-281eeab5e652');

CREATE TABLE public.promo_code (
	dt_alteracao timestamp NULL,
	dt_criacao timestamp NULL,
	dt_exclusao timestamp NULL,
	limite_aplicacoes_afiliados int4 NULL DEFAULT '-1'::integer,
	promo_code varchar(255) NULL,
	produto_app_uid varchar(255) NOT NULL,
	produto_id varchar(255) NOT NULL,
	cliente_padrinho_app_uid varchar(255) NOT NULL,
	cliente_padrinho_id varchar(255) NOT NULL,
	CONSTRAINT promo_code_pkey PRIMARY KEY (cliente_padrinho_app_uid, cliente_padrinho_id, produto_app_uid, produto_id),
	CONSTRAINT uk_6kjt8oolu1ahxyi9pr3qligtl UNIQUE (promo_code),
	CONSTRAINT fkjkt9bpqggt79nk7wb8v4lq469 FOREIGN KEY (produto_app_uid,produto_id) REFERENCES public.produto(app_uid,id),
	CONSTRAINT fkmtlbn1odvae76uwuhdcmusoxa FOREIGN KEY (cliente_padrinho_app_uid,cliente_padrinho_id) REFERENCES public.cliente(app_uid,id)
);

INSERT INTO public.promo_code
(dt_alteracao, dt_criacao, dt_exclusao, limite_aplicacoes_afiliados, promo_code, produto_app_uid, produto_id, cliente_padrinho_app_uid, cliente_padrinho_id)
VALUES(NULL, '2022-11-20 09:21:00.355', NULL, 1, 'AQDC3BO', '26ed47e2-281eeab5e652', 'ff0c-4581-97c7', '26ed47e2-281eeab5e652', '4581-97c7');

