package love.thunder.community.demo.controller;

import com.github.pagehelper.PageHelper;
import love.thunder.community.demo.cache.HotTagCache;
import love.thunder.community.demo.dto.QuestionDTO;
import love.thunder.community.demo.mapper.QuestionMapper;
import love.thunder.community.demo.mapper.UserMapper;
import love.thunder.community.demo.model.Notification;
import love.thunder.community.demo.model.Question;
import love.thunder.community.demo.model.User;
import love.thunder.community.demo.service.NotificationService;
import love.thunder.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue="1")Integer page,
                        @RequestParam(name = "size",defaultValue="5")Integer size,
                        @RequestParam(name = "search",defaultValue = "*") String search
                        ){
        //热门标签
        List<String> hotTags = hotTagCache.getHots();

        //pagehelper的用法，现在不太会，以后好好学
        //得到总问题数，并计算页数
        int questionListSize = questionService.list(search).size();
        int pageCount;
        //当总个数可以被五整除的时候，则页数为个数除5.当不能整除时，则页数为个数除5后加1
        if ((questionListSize % size)==0){
            pageCount = questionListSize / size;
        }else {
            pageCount = questionListSize /size + 1;
        }

        PageHelper.startPage(page,size);
        //紧跟着pagehelper的第一个方法会被分页
        List<QuestionDTO> questionList = questionService.list(search);
        //向前端传输总页数
        model.addAttribute("pageCount",pageCount);
        //向前端传输当前页数
        model.addAttribute("pageForNow",page);
        //向前端传输问题列表
        model.addAttribute("questions",questionList);

        model.addAttribute("hotTags",hotTags);


        return "index";
    }

}
