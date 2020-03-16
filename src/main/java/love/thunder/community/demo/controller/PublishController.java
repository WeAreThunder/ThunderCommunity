package love.thunder.community.demo.controller;

import love.thunder.community.demo.model.Question;
import love.thunder.community.demo.model.User;
import love.thunder.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    //如果得到的是get请求，则渲染publish.html
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    //如果得到的是post请求，则处理数据
    @PostMapping("/publish")
    public String doPublish(
            //得到前端的标题，内容，标签
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id")Integer id,
            HttpServletRequest request,
            Model model
    ){
        //将得到的标题，内容，标签，返回显示回去，这样即便提示错误，内容也会保留
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if (title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description==null||description==""){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if (tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            //向前端发布错误消息
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        //将得到的数据放入新建的question变量中
        Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionService.createOrUpdate(question);
        //如果发布成功，就返回首页
        if (id != null){
            //如果更新问题，则返回问题界面
            return "redirect:/question/"+id;
        }else {
            //如果创建问题，则返回主页
            return "redirect:/";
        }
    }
    //更新
    @GetMapping("/publish/{id}")
    public String updatePublish(@PathVariable(name = "id")Integer id,
                                Model model){
        Question question = questionService.selectByPrimaryKey(id);
        model.addAttribute("id",question.getId());
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        return "publish";
    }

}
