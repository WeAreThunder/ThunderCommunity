package love.thunder.community.demo.dto;

import lombok.Data;
import love.thunder.community.demo.model.User;

@Data
public class NotificationDTO {
    private Integer id;
    private Integer outerId;
    private Long gmtCreate;
    private Integer status;
    private User notifier;
    private String outerTile;
    private String type;
}
