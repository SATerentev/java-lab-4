package com.simonheiss.back.infrastructure;

import com.simonheiss.back.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Integer> {
    @Query(value = "SELECT * FROM gameResult ORDER BY score DESC", nativeQuery = true)
    public List<GameResult> getTopResults();
}
