package cn.yq.springboot1.controller;

import cn.yq.springboot1.dto.CommentDTO;
import cn.yq.springboot1.dto.QuestionDTO;
import cn.yq.springboot1.enums.CommentTypeEnum;
import cn.yq.springboot1.mapper.QuestionMapper;
import cn.yq.springboot1.service.CommentService;
import cn.yq.springboot1.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(
                            @PathVariable(name = "id") Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuertions = questionService.selectRelated(questionDTO);
        List<CommentDTO>comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        questionService.incView(id);//阅读数
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuertions",relatedQuertions);
        return "question";
    }

}
