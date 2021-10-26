package com.ssurbar.db.repository.survey;

import com.ssurbar.db.entity.survey.Question;
import com.ssurbar.db.entity.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, String> {
    Optional<Question> findSurveyBySurveyId(String surveyId);
}
