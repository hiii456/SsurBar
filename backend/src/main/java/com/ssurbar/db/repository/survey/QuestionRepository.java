package com.ssurbar.db.repository.survey;

import com.ssurbar.db.entity.survey.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    Optional<Question> findQuestionByQuestionId(String questionId);

}

