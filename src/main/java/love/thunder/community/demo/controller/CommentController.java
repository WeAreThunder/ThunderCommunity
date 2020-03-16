package love.thunder.community.demo.controller;

import love.thunder.community.demo.dto.CommentCreateDTO;
import love.thunder.community.demo.dto.CommentDTO;
import love.thunder.community.demo.dto.ResultDTO;
import love.thunder.community.demo.exception.CustomizeErrorCode;
import love.thunder.community.demo.model.Comment;
import love.thunder.community.demo.model.User;
import love.thunder.community.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    //使用@RequestBody注解，将json数据放入自己创建的类中
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if(commentCreateDTO == null || StringUtils.isEmpty(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(((User) request.getSession().getAttribute("user")).getId());
        commentService.insertSelective(comment);
        //如果评论成功，则返回成功消息
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List> comments(@PathVariable(name = "id")Integer id){
        List<CommentDTO> commentDTOS = commentService.selectByParentIdAndType(id, 2);
        return ResultDTO.okOf(commentDTOS);
    }
}
