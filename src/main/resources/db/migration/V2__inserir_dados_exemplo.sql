INSERT INTO usuarios (nome, email, senha_hash, perfil, ativo)
SELECT 'Administrador', 'admin@autobox.com', '123456', 'ADMIN', TRUE
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@autobox.com');

INSERT INTO clientes (nome, telefone, email, cpf_cnpj)
SELECT 'Carlos Silva', '(11) 98888-1001', 'carlos@email.com', '11111111111'
WHERE NOT EXISTS (SELECT 1 FROM clientes WHERE cpf_cnpj = '11111111111');

INSERT INTO clientes (nome, telefone, email, cpf_cnpj)
SELECT 'Marina Souza', '(11) 97777-2002', 'marina@email.com', '22222222222'
WHERE NOT EXISTS (SELECT 1 FROM clientes WHERE cpf_cnpj = '22222222222');

INSERT INTO clientes (nome, telefone, email, cpf_cnpj)
SELECT 'Oficina Central LTDA', '(11) 96666-3003', 'contato@oficinacentral.com', '33333333000133'
WHERE NOT EXISTS (SELECT 1 FROM clientes WHERE cpf_cnpj = '33333333000133');

INSERT INTO veiculos (placa, marca, modelo, ano, quilometragem, cliente_id)
SELECT 'ABC1D23', 'Toyota', 'Corolla', 2022, 28500, id FROM clientes WHERE cpf_cnpj = '11111111111'
AND NOT EXISTS (SELECT 1 FROM veiculos WHERE placa = 'ABC1D23');

INSERT INTO veiculos (placa, marca, modelo, ano, quilometragem, cliente_id)
SELECT 'EFG2H34', 'Honda', 'Civic', 2021, 42000, id FROM clientes WHERE cpf_cnpj = '22222222222'
AND NOT EXISTS (SELECT 1 FROM veiculos WHERE placa = 'EFG2H34');

INSERT INTO veiculos (placa, marca, modelo, ano, quilometragem, cliente_id)
SELECT 'XYZ9K88', 'Volkswagen', 'Gol', 2019, 78000, id FROM clientes WHERE cpf_cnpj = '33333333000133'
AND NOT EXISTS (SELECT 1 FROM veiculos WHERE placa = 'XYZ9K88');

INSERT INTO pecas (sku, nome, fabricante, aplicacao_veiculo, preco_unitario, quantidade_estoque, estoque_minimo, versao)
SELECT 'SKU-OLEO001', 'Óleo Motor 5W30', 'Castrol', 'Motor a gasolina', 59.90, 25, 5, 0
WHERE NOT EXISTS (SELECT 1 FROM pecas WHERE sku = 'SKU-OLEO001');

INSERT INTO pecas (sku, nome, fabricante, aplicacao_veiculo, preco_unitario, quantidade_estoque, estoque_minimo, versao)
SELECT 'SKU-FILT001', 'Filtro de Óleo', 'Mann', 'Aplicação universal', 34.50, 18, 5, 0
WHERE NOT EXISTS (SELECT 1 FROM pecas WHERE sku = 'SKU-FILT001');

INSERT INTO pecas (sku, nome, fabricante, aplicacao_veiculo, preco_unitario, quantidade_estoque, estoque_minimo, versao)
SELECT 'SKU-FREI001', 'Pastilha de Freio', 'Bosch', 'Palio/Civic/Corolla', 189.90, 12, 3, 0
WHERE NOT EXISTS (SELECT 1 FROM pecas WHERE sku = 'SKU-FREI001');

INSERT INTO pecas (sku, nome, fabricante, aplicacao_veiculo, preco_unitario, quantidade_estoque, estoque_minimo, versao)
SELECT 'SKU-BATE001', 'Bateria 60Ah', 'Moura', 'Veículos nacionais', 499.00, 8, 2, 0
WHERE NOT EXISTS (SELECT 1 FROM pecas WHERE sku = 'SKU-BATE001');

INSERT INTO pecas (sku, nome, fabricante, aplicacao_veiculo, preco_unitario, quantidade_estoque, estoque_minimo, versao)
SELECT 'SKU-VELA001', 'Jogo de Velas', 'NGK', 'Motores 1.0 a 2.0', 96.50, 30, 8, 0
WHERE NOT EXISTS (SELECT 1 FROM pecas WHERE sku = 'SKU-VELA001');

INSERT INTO ordens_servico (numero_os, veiculo_id, status, descricao_problema, valor_mao_de_obra, valor_total, estoque_baixado, data_abertura)
SELECT 'OS-DEMO-001', id, 'EM_ANDAMENTO', 'Troca de óleo e filtro', 180.00, 274.40, FALSE, NOW()
FROM veiculos WHERE placa = 'ABC1D23'
AND NOT EXISTS (SELECT 1 FROM ordens_servico WHERE numero_os = 'OS-DEMO-001');

INSERT INTO ordens_servico (numero_os, veiculo_id, status, descricao_problema, valor_mao_de_obra, valor_total, estoque_baixado, data_abertura)
SELECT 'OS-DEMO-002', id, 'AGUARDANDO_PECA', 'Substituição das pastilhas de freio', 220.00, 409.90, FALSE, NOW()
FROM veiculos WHERE placa = 'EFG2H34'
AND NOT EXISTS (SELECT 1 FROM ordens_servico WHERE numero_os = 'OS-DEMO-002');

INSERT INTO ordens_servico (numero_os, veiculo_id, status, descricao_problema, valor_mao_de_obra, valor_total, estoque_baixado, data_abertura)
SELECT 'OS-DEMO-003', id, 'CONCLUIDA', 'Troca de bateria e revisão elétrica', 150.00, 649.00, TRUE, NOW()
FROM veiculos WHERE placa = 'XYZ9K88'
AND NOT EXISTS (SELECT 1 FROM ordens_servico WHERE numero_os = 'OS-DEMO-003');
