package love.thunder.community.demo.controller;

import com.github.pagehelper.PageHelper;
import love.thunder.community.demo.dto.NotificationDTO;
import love.thunder.community.demo.dto.QuestionDTO;
import love.thunder.community.demo.mapper.UserMapper;
import love.thunder.community.demo.model.Notification;
import love.thunder.community.demo.model.User;
import love.thunder.community.demo.service.CommentService;
import love.thunder.community.demo.service.NotificationService;
import love.thunder.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentService commentService;

    @GetMapping("/profile/{action}")
    public String Profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page",defaultValue="1")Integer page,
                          @RequestParam(name = "size",defaultValue="5")Integer size){
        User user = (User) request.getSession().getAttribute("user");
//        当用户未登录时，跳转到登录页面
        if (user == null){
            return "redirect:/index";
        }
        //将当前页的具体信息传回前端
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            questionPageSet(model, page, size, user);
        }else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            NoticePageSet(model, page, size, user);
        }


        return "profile";
    }

    private void NoticePageSet(Model model, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size, User user) {
        size=10;
        List<NotificationDTO> notificationDTOS = getNotificationDTOList(user);
        int noticeListSize = notificationDTOS.size();
        int pageCount;
        //当总个数可以被五整除的时候，则页数为个数除5.当不能整除时，则页数为个数除5后加1
        if ((noticeListSize % size)==0){
            pageCount = noticeListSize / size;
        }else {
            pageCount = noticeListSize /size + 1;
        }
        PageHelper.startPage(page,size);
        notificationDTOS = getNotificationDTOList(user);
        //向前端传输总页数
        model.addAttribute("pageCount",pageCount);
        //向前端传输当前页数
        model.addAttribute("pageForNow",page);
        //向前端传递通知列表
        model.addAttribute("notifications",notificationDTOS);
    }

    private List<NotificationDTO> getNotificationDTOList(User user) {
        //传递通知数据
        List<Notification> notifications = notificationService.selectByReceiverOrderByGmtCreateDesc(user.getId());
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setGmtCreate(notification.getGmtCreate());
            notificationDTO.setNotifier(userMapper.selectByPrimaryKey(notification.getNotifier()));
            notificationDTO.setStatus(notification.getStatus());
            notificationDTO.setId(notification.getId());
            if (notification.getType() == 1){
                notificationDTO.setType("回复了你的问题：");
                notificationDTO.setOuterId(notification.getOuterId());
                notificationDTO.setOuterTile(questionService.selectByPrimaryKey(notification.getOuterId()).getTitle());
            }else if (notification.getType() == 2){
                notificationDTO.setType("回复了你的评论：");
                notificationDTO.setOuterId(questionService.selectByPrimaryKey(commentService.selectByPrimaryKey(notification.getOuterId()).getParentId()).getId());
                notificationDTO.setOuterTile(commentService.selectByPrimaryKey(notification.getOuterId()).getContent());
            }
            notificationDTOS.add(notificationDTO);
        }
        return notificationDTOS;
    }


    private void questionPageSet(Model model, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "5") Integer size, User user) {
        //使用按创建者id的方式查找发布的问题
        List<QuestionDTO> questionList = questionService.listByCreator(user.getId());

        //pagehelper的用法，现在不太会，以后好好学
        //得到总问题数，并计算页数
        int questionListSize = questionList.size() ;
        int pageCount;
        //当总个数可以被五整除的时候，则页数为个数除5.当不能整除时，则页数为个数除5后加1
        if ((questionListSize % size)==0){
            pageCount = questionListSize / size;
        }else {
            pageCount = questionListSize /size + 1;
        }


        PageHelper.startPage(page,size);
        //紧跟着pagehelper的第一个方法会被分页
        List<QuestionDTO> questionListByCreator = questionService.listByCreator(user.getId());
        //向前端传输总页数
        model.addAttribute("pageCount",pageCount);
        //向前端传输当前页数
        model.addAttribute("pageForNow",page);
        //向前端传递问题列表
        model.addAttribute("questions",questionListByCreator);
    }
}
