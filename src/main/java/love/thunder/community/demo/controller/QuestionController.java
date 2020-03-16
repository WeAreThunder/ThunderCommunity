package love.thunder.community.demo.controller;

import love.thunder.community.demo.dto.CommentDTO;
import love.thunder.community.demo.dto.QuestionDTO;
import love.thunder.community.demo.model.Comment;
import love.thunder.community.demo.model.Question;
import love.thunder.community.demo.service.CommentService;
import love.thunder.community.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model){
        //获取问题详情
        QuestionDTO questionDTO = questionService.getById(id);
        //获取回复列表
        List<CommentDTO> comments = commentService.selectByParentIdAndType(id,1);
        //获取相关问题
        List<Question> questionsByTag = questionService.selectByTagLike(questionDTO.getId(), questionDTO.getTag());

        //每次访问成功后增加浏览数数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("questionsByTag",questionsByTag);
        return "question";
    }
    @Transactional
    @GetMapping("/likeComment/{id}")
    public String incCommentLikeCount(@PathVariable("id")Integer id){
        Comment comment = commentService.selectByPrimaryKey(id);
        commentService.updateLikeCountById(id);
        questionService.updateOneLikeCountById(comment.getParentId());
        return "redirect:/question/"+comment.getParentId();
    }

}
