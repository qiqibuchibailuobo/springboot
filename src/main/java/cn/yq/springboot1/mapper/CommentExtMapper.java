package cn.yq.springboot1.mapper;

import cn.yq.springboot1.model.Comment;
import cn.yq.springboot1.model.Question;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}