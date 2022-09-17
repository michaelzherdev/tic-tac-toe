package com.mzherdev.tictactoe.dao;

import com.mzherdev.tictactoe.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Game> findById(Integer integer);
}
