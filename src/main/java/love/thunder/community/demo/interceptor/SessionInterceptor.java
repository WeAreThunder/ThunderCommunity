package love.thunder.community.demo.interceptor;

import love.thunder.community.demo.mapper.UserMapper;
import love.thunder.community.demo.model.Notification;
import love.thunder.community.demo.model.User;
import love.thunder.community.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    //ctrl+o可以创建接口的方法

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        //当cookie不为空时进行查找
        if (cookies !=null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    List<User> users = userMapper.selectAllByToken(token);
                    if(users.size() !=0){
                        //在cookie中发现已登录的账号，验证成功后，给session中加入user
                        request.getSession().setAttribute("user",users.get(0));
                    }
                    break;
                }
            }
        }
        //全局显示通知数量
        if (request.getSession().getAttribute("user") != null){
            User user = (User) request.getSession().getAttribute("user");
            List<Notification> notifications = notificationService.selectByReceiverAndStatus(user.getId(), 0);
            int notificationCount = notifications.size();
            request.getSession().setAttribute("notificationCount",notificationCount);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
