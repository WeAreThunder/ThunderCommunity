package love.thunder.community.demo.controller;

import love.thunder.community.demo.model.Notification;
import love.thunder.community.demo.service.CommentService;
import love.thunder.community.demo.service.NotificationService;
import love.thunder.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("/notification/{id}")
    public String NotificationRead(@PathVariable("id")Integer id){
        Notification notification = notificationService.selectByPrimaryKey(id);
        int questionId = 0 ;
        if (notification.getType() == 1){
            questionId = questionService.selectByPrimaryKey(notification.getOuterId()).getId();
        }else if (notification.getType() == 2){
            questionId = commentService.selectByPrimaryKey(notification.getOuterId()).getParentId();
        }
        notificationService.updateStatusById(1,id);
        return "redirect:/question/"+questionId;
    }
}
