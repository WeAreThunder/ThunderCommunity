package love.thunder.community.demo.model;

import lombok.Data;

@Data
public class Notification {
    private Integer id;

    private Integer notifier;

    private Integer receiver;

    private Integer outerId;

    private Integer type;

    private Long gmtCreate;

    private Integer status;
}