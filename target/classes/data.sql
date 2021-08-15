insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, IS_DISPONIVEL, CATEGORIA, PRECO, QTD_ESTOQUE)
values(10002,'Arroz Integral', '87654322-B', 'Tio Joao', TRUE, 'Perecíveis', 5.5, 10);

insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, IS_DISPONIVEL, CATEGORIA, PRECO, QTD_ESTOQUE)
values(10003,'Sabao em Po', '87654323-B', 'OMO', TRUE, 'Limpeza', 12, 10);

insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, IS_DISPONIVEL, CATEGORIA, PRECO, QTD_ESTOQUE)
values(10004,'Agua Sanitaria', '87654324-C', 'Dragao', TRUE, 'limpesa', 3, 10);

insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, IS_DISPONIVEL, CATEGORIA, PRECO, QTD_ESTOQUE)
values(10005,'Creme Dental', '87654325-C', 'Colgate', TRUE, 'HIGIENE', 2.5, 10);

insert into lote (ID, PRODUTO_ID, NUMERO_DE_ITENS)
values(1, 10005, 5);

update produto set IS_DISPONIVEL = TRUE where ID = 10005;

insert into cliente (ID, CPF, NOME, IDADE, ENDERECO,TIPO)
values(1, 11020030006, 'Leonardo Su', 21, 'Rua tal X','Normal');

insert into cliente (ID, CPF, NOME, IDADE, ENDERECO,TIPO)
values(2, 22020030006, 'Su', 21, 'Rua tal Y','Especial');

insert into cliente (ID, CPF, NOME, IDADE, ENDERECO,TIPO)
values(3, 33020030006, 'Su Su', 21, 'Rua tal Z','Premium');