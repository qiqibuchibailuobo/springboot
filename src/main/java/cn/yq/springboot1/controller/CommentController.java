package cn.yq.springboot1.controller;

import cn.yq.springboot1.dto.CommentCreateDTO;
import cn.yq.springboot1.dto.CommentDTO;
import cn.yq.springboot1.dto.ResultDTO;
import cn.yq.springboot1.enums.CommentTypeEnum;
import cn.yq.springboot1.exception.CustomizeErrorCode;
import cn.yq.springboot1.mapper.CommentMapper;
import cn.yq.springboot1.model.Comment;
import cn.yq.springboot1.model.User;
import cn.yq.springboot1.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller

public class CommentController {
@Autowired(required = false)
private CommentMapper commentMapper;
    @Autowired(required = false)
    private CommentService commentService;

    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    @ResponseBody
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request,
                       Model model,
                       HttpSession session){
        //requestbody接收前端传来的json数据
        //responsebody将后台数据序列化成json传到前端
        //通过session获取当前用户

        User user = (User) request.getSession().getAttribute("user");

        if(user == null){
           // return ResultDTO.errorOf(2002,"请先登录");
            return  ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return  ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
       // comment.setCommentator(261);
        comment.setLikeCount(0L);

        commentService.insert(comment,user);
        //commentMapper.insert(comment);
//        Map<Object,Object> objectObjectMap = new HashMap<>();//直接用map转化成json格式
//        objectObjectMap.put("message","成功");
        return ResultDTO.okOf();
    }
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
