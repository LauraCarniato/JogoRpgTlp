CREATE TABLE personagem (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    classe VARCHAR(50) NOT NULL,
    aparencia VARCHAR(50) NOT NULL,
    pontos_disponiveis INT DEFAULT 0,
    vida INT DEFAULT 0,
    espada INT DEFAULT 0,
    cura INT DEFAULT 0,
    escudo INT DEFAULT 0,
    dano INT DEFAULT 0,
    congelamento INT DEFAULT 0
);

CREATE TABLE progresso (
    id SERIAL PRIMARY KEY,
    personagem_id INT REFERENCES personagem(id),
    mapa_atual VARCHAR(100) DEFAULT 'nivel1_sala1.png',
    inimigo_atual VARCHAR(50) DEFAULT 'goblin'
);

ALTER TABLE progresso
ADD CONSTRAINT progresso_personagem_unico UNIQUE (personagem_id);

ALTER TABLE progresso
ADD COLUMN posicao_x DOUBLE PRECISION DEFAULT 0;

ALTER TABLE progresso
ADD COLUMN posicao_y DOUBLE PRECISION DEFAULT 0;

ALTER TABLE progresso
DROP CONSTRAINT progresso_personagem_id_fkey;

ALTER TABLE progresso
ADD CONSTRAINT progresso_personagem_id_fkey
FOREIGN KEY (personagem_id)
REFERENCES personagem(id)
ON DELETE CASCADE;