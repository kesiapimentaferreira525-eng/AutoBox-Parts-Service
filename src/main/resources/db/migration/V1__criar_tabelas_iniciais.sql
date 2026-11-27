CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(30) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    cpf_cnpj VARCHAR(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS veiculos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    placa VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(60) NOT NULL,
    modelo VARCHAR(80) NOT NULL,
    ano INT NOT NULL,
    quilometragem INT NOT NULL DEFAULT 0,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_veiculos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS pecas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sku VARCHAR(40) NOT NULL UNIQUE,
    nome VARCHAR(120) NOT NULL,
    fabricante VARCHAR(80),
    aplicacao_veiculo VARCHAR(150),
    preco_unitario DECIMAL(10, 2) NOT NULL CHECK (preco_unitario > 0),
    quantidade_estoque INT NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0),
    estoque_minimo INT NOT NULL DEFAULT 2 CHECK (estoque_minimo >= 0),
    versao BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS ordens_servico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    numero_os VARCHAR(20) NOT NULL UNIQUE,
    veiculo_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    descricao_problema TEXT NOT NULL,
    valor_mao_de_obra DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    valor_total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    estoque_baixado BOOLEAN NOT NULL DEFAULT FALSE,
    data_abertura TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_conclusao TIMESTAMP NULL,
    CONSTRAINT fk_ordens_servico_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id)
);

CREATE TABLE IF NOT EXISTS itens_ordem_servico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ordem_servico_id BIGINT NOT NULL,
    peca_id BIGINT NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade > 0),
    preco_unitario_snapshot DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_itens_os_ordem FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_itens_os_peca FOREIGN KEY (peca_id) REFERENCES pecas(id)
);

CREATE TABLE IF NOT EXISTS vendas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cliente_id BIGINT NULL,
    data_venda TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_vendas_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS itens_venda (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    venda_id BIGINT NOT NULL,
    peca_id BIGINT NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade > 0),
    preco_unitario_snapshot DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_itens_venda_venda FOREIGN KEY (venda_id) REFERENCES vendas(id) ON DELETE CASCADE,
    CONSTRAINT fk_itens_venda_peca FOREIGN KEY (peca_id) REFERENCES pecas(id)
);
