-- Criar o banco de dados
CREATE DATABASE carwash_db;

-- Usar o banco de dados
USE carwash_db;

-- Criar tabela de Usuários
CREATE TABLE usuarios (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          senha VARCHAR(100) NOT NULL,
                          tipo_usuario ENUM('EMPRESARIAL', 'SINGULAR') NOT NULL
);

-- Criar tabela de Serviços
CREATE TABLE servicos (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          descricao VARCHAR(255) NOT NULL,
                          tipo_servico ENUM('LAVAGEM_SECO', 'LIMPEZA_COMPLETA', 'POLIMENTO', 'OUTRO') NOT NULL,
                          plano ENUM('LIGEIRO', 'PESADO') NOT NULL,
                          preco DECIMAL(10, 2) NOT NULL
);

-- Criar tabela de Agendamentos
CREATE TABLE agendamentos (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              usuario_id INT NOT NULL,
                              servico_id INT NOT NULL,
                              data DATE NOT NULL,
                              status ENUM('PENDENTE', 'CONFIRMADO', 'CANCELADO') NOT NULL,
                              FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                              FOREIGN KEY (servico_id) REFERENCES servicos(id)
);

-- Criar tabela de Histórico de Serviços
CREATE TABLE historicos_servicos (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     usuario_id INT NOT NULL,
                                     servico_id INT NOT NULL,
                                     data DATE NOT NULL,
                                     detalhes TEXT NOT NULL,
                                     FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                                     FOREIGN KEY (servico_id) REFERENCES servicos(id)
);

-- Adicionar um usuário empresarial com nome e senha simplificados
INSERT INTO usuarios (nome, email, senha, tipo_usuario)
VALUES ('E', 'e@exemplo.com', 'e', 'EMPRESARIAL');

-- Adicionar um usuário não empresarial com nome e senha simplificados
INSERT INTO usuarios (nome, email, senha, tipo_usuario)
VALUES ('I', 'i@exemplo.com', 'i', 'SINGULAR');
