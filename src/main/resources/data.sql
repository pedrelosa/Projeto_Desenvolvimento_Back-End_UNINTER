-- =========================================================
-- LIMPEZA (caso rode múltiplas vezes em create/update)
-- =========================================================
-- A ordem evita quebra de FK
DELETE FROM consultas_online;
DELETE FROM consultas_presenciais;
DELETE FROM consultas;
DELETE FROM prontuarios;
DELETE FROM home_care;
DELETE FROM recepcionistas;
DELETE FROM enfermeiros;
DELETE FROM medicos;
DELETE FROM pacientes;
DELETE FROM clinicas;
DELETE FROM hospitais;

-- =========================================================
-- DADOS BÁSICOS
-- =========================================================

-- CLÍNICA(S)
INSERT INTO clinicas (id, nome, endereco)
VALUES (1, 'Clínica VidaPlus', 'Rua das Flores, 200');

-- HOSPITAL(ES)
INSERT INTO hospitais (id, nome, endereco)
VALUES (1, 'Hospital VidaPlus', 'Av. Central, 1000');

-- PROFISSIONAIS
INSERT INTO medicos (id, nome, email, senha, telefone, perfil, crm, especialidade)
VALUES
  (1, 'Dra. Ana',   'ana@vp.com',   '123', '9999-0001', 'MEDICO', 'CRM123', 'Clínico Geral'),
  (2, 'Dr. Bruno',  'bruno@vp.com', '123', '9999-0002', 'MEDICO', 'CRM456', 'Cardiologista');

INSERT INTO enfermeiros (id, nome, email, senha, telefone, perfil, coren)
VALUES
  (1, 'Enf. Carla', 'carla@vp.com', '123', '9999-0003', 'ENFERMEIRO', 'COREN001');

INSERT INTO recepcionistas (id, nome, email, senha, telefone, perfil, matricula)
VALUES
  (1, 'Paula', 'paula@vp.com', '123', '9999-0004', 'RECEPCIONISTA', 'REC123');

-- PACIENTES
INSERT INTO pacientes (id, nome, email, senha, telefone, perfil, cpf, data_nascimento)
VALUES
  (1, 'João Silva', 'joao@vp.com', '123', '8888-1111', 'PACIENTE', '11122233344', DATE '1990-05-10'),
  (2, 'Maria Souza','maria@vp.com','123', '8888-2222', 'PACIENTE', '55566677788', DATE '1985-08-22');

-- PRONTUÁRIOS
INSERT INTO prontuarios (id, paciente_id, medico_id, diagnostico, prescricoes, data_criacao)
VALUES
  (1, 1, 1, 'Cefaleia leve', 'Hidratação e repouso', TIMESTAMP '2030-01-05 14:00:00'),
  (2, 2, 2, 'Acompanhamento cardiológico', NULL, TIMESTAMP '2030-01-06 09:00:00');

-- HOME CARE
INSERT INTO home_care (id, paciente_id, enfermeiro_id, data_hora, procedimentos)
VALUES
  (1, 2, 1, TIMESTAMP '2030-01-09 15:00:00', 'Curativo e avaliação de sinais vitais');

-- =========================================================
-- CONSULTAS (JOINED)
--   id 1 -> presencial (Hospital)
--   id 2 -> online (Clínica)
-- =========================================================

-- BASE: CONSULTA 1 (PRESENCIAL)
INSERT INTO consultas (id, paciente_id, medico_id, data_hora, status)
VALUES (1, 1, 1, TIMESTAMP '2030-01-10 09:30:00', 'Agendada');

-- DERIVADA: CONSULTA PRESENCIAL (usa MESMO ID)
INSERT INTO consultas_presenciais (id, sala)
VALUES (1, '101');

-- BASE: CONSULTA 2 (ONLINE)
INSERT INTO consultas (id, paciente_id, medico_id, data_hora, status)
VALUES (2, 1, 2, TIMESTAMP '2030-01-11 10:00:00', 'Agendada');

-- DERIVADA: CONSULTA ONLINE (usa MESMO ID)
INSERT INTO consultas_online (id, link_video_chamada)
VALUES (2, 'https://telemed.vidaplus/consulta/2?tkn=seed-uuid-0002');

-- =========================================================
-- AJUSTE DE AUTO-INCREMENT (evitar colisões ao salvar novos)
--   OBS: Em JOINED, as tabelas derivadas normalmente NÃO têm identity.
-- =========================================================
ALTER TABLE clinicas     ALTER COLUMN id RESTART WITH  2;
ALTER TABLE hospitais    ALTER COLUMN id RESTART WITH  2;
ALTER TABLE medicos      ALTER COLUMN id RESTART WITH  3;
ALTER TABLE enfermeiros  ALTER COLUMN id RESTART WITH  2;
ALTER TABLE recepcionistas ALTER COLUMN id RESTART WITH 2;
ALTER TABLE pacientes    ALTER COLUMN id RESTART WITH  3;
ALTER TABLE prontuarios  ALTER COLUMN id RESTART WITH  3;
ALTER TABLE home_care    ALTER COLUMN id RESTART WITH  2;
ALTER TABLE consultas    ALTER COLUMN id RESTART WITH  3;

-- =========================================================
-- OBS SOBRE ASSOCIAÇÃO CLÍNICA/HOSPITAL x CONSULTAS
-- Os @OneToMany em Clinica/Hospital são unidirecionais (sem mappedBy),
-- então o Hibernate cria TABELA DE JUNÇÃO com nome gerado.
-- Para evitar acoplar ao nome exato da join table (dependente do dialeto/estratégia),
-- NÃO populamos aqui. A associação será feita em runtime pelos Controllers/Services.
-- =========================================================
