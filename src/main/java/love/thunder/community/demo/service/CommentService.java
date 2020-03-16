package love.thunder.community.demo.service;

import love.thunder.community.demo.dto.CommentDTO;
import love.thunder.community.demo.exception.CustomizeErrorCode;
import love.thunder.community.demo.exception.CustomizeException;
import love.thunder.community.demo.mapper.QuestionMapper;
import love.thunder.community.demo.mapper.UserMapper;
import love.thunder.community.demo.model.Notification;
import love.thunder.community.demo.model.Question;
import love.thunder.community.demo.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import love.thunder.community.demo.model.Comment;
import love.thunder.community.demo.mapper.CommentMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    public int deleteByPrimaryKey(Integer id) {
        return commentMapper.deleteByPrimaryKey(id);
    }


    public int insert(Comment comment) {
        return commentMapper.insert(comment);
    }

    //事务注解，使用该注解后，方法里对数据库的操作要么全都成功，要么全都失败
    @Transactional
    public void insertSelective(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        //type为1时，为问题的回复
        //type为2时，为回复的回复
        if (comment.getType() == 1) {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            //将前端获取的评论数据写入数据库
            commentMapper.insertSelective(comment);
            //增加一个问题评论数
            questionMapper.updateOneCommentCountById(question.getId());
            //创建通知,当通知者和被通知者不是一人是，才创建通知
            if (question.getCreator() != comment.getCommentator()){
                createNotify(comment, question);
            }

        } else if (comment.getType() == 2) {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            //将前端获取的评论数据写入数据库
            commentMapper.insertSelective(comment);
            //增加一个回复评论数
            commentMapper.updateOneCommentCountById(comment.getParentId());
            if (commentMapper.selectByPrimaryKey(comment.getParentId()).getCommentator() != comment.getCommentator()){
                createNotify(comment, question);
            }
        }
    }

    private void createNotify(Comment comment, Question question) {
        question = questionMapper.selectByPrimaryKey(comment.getParentId());
        //添加一个回复通知
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        if (comment.getType() == 1){
            notification.setType(1);
            //收到通知的为问题的发起人
            notification.setReceiver(question.getCreator());
        }else if (comment.getType() == 2){
            notification.setType(2);
            //收到通知的为层主
            notification.setReceiver(commentMapper.selectByPrimaryKey(comment.getParentId()).getCommentator());
        }
        notification.setOuterId(comment.getParentId());
        //发出通知的为评论者
        notification.setNotifier(comment.getCommentator());
        notificationService.insertSelective(notification);
    }


    public Comment selectByPrimaryKey(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(Comment record) {
        return commentMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(Comment record) {
        return commentMapper.updateByPrimaryKey(record);
    }

    public List<CommentDTO> selectByParentIdAndType(Integer id, Integer type) {
        List<Comment> comments = commentMapper.selectByParentIdAndTypeOrderByGmtCreateDesc(id, type);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转换为 Map
        List<User> users = userMapper.selectByIds(userIds);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMapper.selectByPrimaryKey(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        Collectors.toList();

        return commentDTOS;
    }

    public int updateLikeCountById(Integer id) {
        return commentMapper.updateOneLikeCountById(id);
    }
}




