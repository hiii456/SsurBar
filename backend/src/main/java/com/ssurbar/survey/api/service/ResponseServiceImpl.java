package com.ssurbar.survey.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssurbar.survey.api.request.ResponsePostReq;
import com.ssurbar.survey.api.response.SurveyAnswer;
import com.ssurbar.survey.common.util.RandomIdUtil;
import com.ssurbar.survey.db.entity.answer.FilterData;
import com.ssurbar.survey.db.entity.answer.QuestionAnswer;
import com.ssurbar.survey.db.repository.answer.FilterDataRepository;
import com.ssurbar.survey.db.repository.answer.QuestionAnswerRepository;
import com.ssurbar.survey.db.repository.survey.QuestionRepository;
import com.ssurbar.survey.db.repository.survey.SurveyRepository;

@Service("responseService")
public class ResponseServiceImpl implements ResponseService {

    @Autowired
    FilterDataRepository filterDataRepository;

    @Autowired
    QuestionAnswerRepository questionAnswerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    RandomIdUtil randomIdUtil;

    /* 사용자의 응답을 수집하여 DB에 저장하기 */
    @Override
    public List<SurveyAnswer> saveAnswer(ResponsePostReq responsePostReq) {
        String surveyId = responsePostReq.getSurveyId();
        String filterAnswer = responsePostReq.getFilterAnswer();
        List<String> answerList = responsePostReq.getAnswerList();
        JsonParser jsonParser = new JsonParser();
        List<SurveyAnswer> list = new ArrayList<>();

        FilterData filterData = FilterData.builder()
                .filterDataId(randomIdUtil.makeRandomId(13))
                .response(filterAnswer)
                .build();

        filterDataRepository.save(filterData);

        for ( String content : answerList ) {
            
            /**
             * answer의 형태
             * { "questionId": String,
             *  "answer": JSON }
             */

            JsonElement element = jsonParser.parse(content);
            String questionId = element.getAsJsonObject().get("questionId").getAsString();
            String answer = element.getAsJsonObject().get("answer").getAsString();

            QuestionAnswer questionAnswer = QuestionAnswer.builder()
                    .questionAnswerId(randomIdUtil.makeRandomId(13))
                    .question(questionRepository.findQuestionByQuestionId(questionId).get())
                    .response(answer)
                    .filterData(filterDataRepository.findFilterDataByFilterDataId(filterData.getFilterDataId()).get())
                    .survey(surveyRepository.findBySurveyId(surveyId).get())
                    .build();

            questionAnswerRepository.save(questionAnswer);
            SurveyAnswer surveyAnswer = SurveyAnswer.builder()
                    .questionId(questionAnswer.getQuestion().getQuestionId())
                    .filterResponse(questionAnswer.getFilterData().getResponse())
                    .response(questionAnswer.getResponse())
                    .build();

            list.add(surveyAnswer);

        }

        return list;

    }

}

