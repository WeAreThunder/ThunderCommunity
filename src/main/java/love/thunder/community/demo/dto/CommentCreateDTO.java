package love.thunder.community.demo.dto;

import lombok.Data;
import org.apache.ibatis.annotations.Delete;

@Data
public class CommentCreateDTO {
    private Integer parentId;
    private String content;
    private Integer type;
}
