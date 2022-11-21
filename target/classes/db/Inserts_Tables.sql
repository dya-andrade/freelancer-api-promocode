INSERT INTO public.app
(uid, dt_alteracao, dt_criacao, dt_exclusao, nome, "token")
VALUES('26ed47e2-ff0c-4581-97c7-281eeab5e652', null, '2022-11-19 18:34', null, 'Tua Agenda', 'q4YZ$ys5etJMrmnV');


INSERT INTO public.produto
(id, dt_alteracao, dt_criacao, dt_exclusao, limite_aplicacao_bonus_padrinho, limite_aplicacoes_afiliados, moeda_afiliado, moeda_padrinho, nome, post_webhock_padrinho, app_uid)
VALUES('1', null, '2022-11-19 18:36', null, 3, '-1'::integer, 0, 0, 'Black Friday', 'Promoção', '26ed47e2-ff0c-4581-97c7-281eeab5e652');

