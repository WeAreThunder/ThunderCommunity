package love.thunder.community.demo.model;

import lombok.Data;

@Data
public class Comment {
    private Integer id;

    /**
     * 父类id
     */
    private Integer parentId;

    /**
     * 父类类型
     * 1:问题的回复
     * 2:回复的回复
     */
    private Integer type;

    /**
     * 评论人id
     */
    private Integer commentator;

    /**
     * 创建时间
     */
    private Long gmtCreate;

    /**
     * 更新时间
     */
    private Long gmtModified;

    private Long likeCount;

    /**
     * 内容
     */
    private String content;

    private Integer commentCount;
}