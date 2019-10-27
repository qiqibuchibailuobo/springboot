package cn.yq.springboot1.controller;

import cn.yq.springboot1.dto.PaginationDTO;
import cn.yq.springboot1.model.User;
import cn.yq.springboot1.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@org.springframework.stereotype.Controller
public class IndexController {
    @Autowired(required = false)
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        HttpServletRequest request,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size ,
                        @RequestParam(name = "search",required = false) String  search ){
//        User user = (User) request.getSession().getAttribute("user");
//        System.out.println("ddd"+user.getId());
        PaginationDTO pagination = questionService.list(search,page,size);
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        return "index";
    }
}
