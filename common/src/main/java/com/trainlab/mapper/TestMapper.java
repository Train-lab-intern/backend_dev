package com.trainlab.mapper;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.Enum.eUserLevel;
import com.trainlab.dto.*;
import com.trainlab.model.User;
import com.trainlab.model.testapi.Answer;
import com.trainlab.model.testapi.Question;
import com.trainlab.model.testapi.Test;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestMapper {
    @Mapping(target = "id", ignore = true)
    Test toEntity(TestDTO testDTO);
    Test toEntity(TestCreateDTO testCreateDTO);
    @Mapping(target = "id", ignore = true)
    Answer toEntity (AnswerDTO answerDTO);

    Answer toEntity (AnswerCreateDTO answerCreateDTO);
    @Mapping(target = "id", ignore = true)
    Question toEntity(QuestionDTO questionDTO);

    Question toEntity(QuestionCreateDTO questionCreateDTO);

    TestDTO toDTO(Test test);
    AnswerDTO toDTO(Answer answer);
    QuestionDTO toDTO(Question question);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Test partialUpdateToEntity(TestCreateDTO testCreateDTO, @MappingTarget Test test);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Question partialUpdateToEntity(QuestionCreateDTO questionCreateDTO, @MappingTarget Question question);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Answer partialUpdateToEntity(AnswerCreateDTO answerCreateDTO, @MappingTarget Answer answer);
}