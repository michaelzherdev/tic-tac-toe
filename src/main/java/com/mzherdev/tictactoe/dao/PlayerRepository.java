package com.mzherdev.tictactoe.dao;

import com.mzherdev.tictactoe.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
}
