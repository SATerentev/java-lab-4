package com.simonheiss.back.infrastructure;

import com.simonheiss.back.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Integer> {
    @Query(value = "SELECT * FROM game_result WHERE score != 0 ORDER BY score DESC LIMIT 10;", nativeQuery = true)
    public List<GameResult> getTopResults();
}
