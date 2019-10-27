package cn.yq.springboot1.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private String categoryName;//大类别名称

    private List<String> tags;
}
