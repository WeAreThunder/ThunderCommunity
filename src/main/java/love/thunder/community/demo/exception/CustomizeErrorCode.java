package love.thunder.community.demo.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"U•ェ•*U问题不存在了，要不要换个试试"),
    TARGET_PARAM_NOT_FOUND(2002,"U•ェ•*U未选中任何问题和回复进行回复"),
    NO_LOGIN(2003,"U•ェ•*U没有登录哦"),
    SYSTEM_ERROR(2004,"U•ェ•*服务器坏掉了"),
    TYPE_PARAM_WRONG(2005,"U•ェ•*评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"U•ェ•*回复的评论不在了"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空"),
    ;

    private String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
