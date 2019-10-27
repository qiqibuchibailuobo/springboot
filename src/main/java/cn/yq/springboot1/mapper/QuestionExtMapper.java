package cn.yq.springboot1.mapper;

import cn.yq.springboot1.dto.QuestionQueryDTO;
import cn.yq.springboot1.model.Question;


import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

}