CREATE TABLE player (
  id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   name VARCHAR(255)
);

CREATE TABLE game (
  id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   player1_id INT NOT NULL,
   player2_id INT NOT NULL,
   active_player_id INT NOT NULL,
   board VARCHAR(255),
   finished BOOLEAN NOT NULL,
   result VARCHAR(255)
);

ALTER TABLE game ADD CONSTRAINT FK_GAME_ON_PLAYER1 FOREIGN KEY (player1_id) REFERENCES player (id);
ALTER TABLE game ADD CONSTRAINT FK_GAME_ON_PLAYER2 FOREIGN KEY (player2_id) REFERENCES player (id);