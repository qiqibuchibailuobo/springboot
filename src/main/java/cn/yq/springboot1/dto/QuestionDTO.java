package cn.yq.springboot1.dto;

import cn.yq.springboot1.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;
    private User user;
}
