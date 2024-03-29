package cn.yq.springboot1.enums;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2)
    ;
    private Integer type;
    CommentTypeEnum(Integer type){
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if(commentTypeEnum.type == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
}
