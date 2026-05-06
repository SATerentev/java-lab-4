package com.simonheiss.back.infrastructure;

import com.simonheiss.back.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
    @Query(value = "SELECT * FROM question WHERE grade = :grade ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Question getRandomQuestion(@Param("grade") int grade);
}
