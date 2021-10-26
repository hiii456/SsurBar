package com.ssurbar.db.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssurbar.db.entity.User;
import com.ssurbar.db.entity.survey.Survey;
import com.ssurbar.db.entity.survey.SurveyForm;
import com.ssurbar.db.entity.survey.SurveyTarget;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, String> {

	List<Survey> findAllBySurveyFormId(SurveyForm surveyForm);
	
	List<Survey> findAllByUserId(User user);

	SurveyTarget findBySurveyId(Survey survey);

}
