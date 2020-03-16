package love.thunder.community.demo.service;

import love.thunder.community.demo.dto.QuestionDTO;
import love.thunder.community.demo.exception.CustomizeErrorCode;
import love.thunder.community.demo.exception.CustomizeException;
import love.thunder.community.demo.mapper.QuestionMapper;
import love.thunder.community.demo.mapper.UserMapper;
import love.thunder.community.demo.model.Question;
import love.thunder.community.demo.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;


    public List<QuestionDTO> list(String search) {
        List<Question> questions;
        if (search.equals("*")){
            questions = questionMapper.selectAll();
        } else {
            questions = questionMapper.selectByTitleLike(search);
        }
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question中的值，快速的拷贝到questionDTO中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        return questionDTOList;
    }
    //根据创建者id获得问题列表
    public List<QuestionDTO> listByCreator(Integer creator){
        List<Question> questions = questionMapper.selectAllByCreator(creator);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question中的值，快速的拷贝到questionDTO中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        //如果问题不存在，则进入自制的错误处理方法中
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }
    //增加阅读数
    public void incView(Integer id) {
        questionMapper.updateOneViewCountById(id);
    }
    //增加回复数
    public int updateOneCommentCountById(Integer id) {
        return questionMapper.updateOneCommentCountById(id);
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //如果没有得到id则创建新问题
            questionMapper.insertSelective(question);
        }else{
            //如果得到id,则根据id，进行更新
            Question dbQuestion = questionMapper.selectByPrimaryKey(question.getId());
            dbQuestion.setTitle(question.getTitle());
            dbQuestion.setDescription(question.getDescription());
            dbQuestion.setTag(question.getTag());
            int updated = questionMapper.updateByPrimaryKey(dbQuestion);
            //如果在更新时找不到问题，则跳转去错误界面
            if (updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public Question selectByPrimaryKey(Integer id) {
        return questionMapper.selectByPrimaryKey(id);
    }


    public List<Question> selectByTagLike(Integer id, String likeTag) {
        String[] splits = likeTag.split(",|，");
        List<Question> allQuestion = new ArrayList<>();
        //按照tag查找相关问题，并且去重
        for (String split : splits) {
            List<Question> questions = questionMapper.selectByTagLike(id,split);
            for (Question question : questions) {
                if (!allQuestion.contains(question))
                allQuestion.add(question);
            }
        }

        return allQuestion;
    }

    public int updateOneLikeCountById(Integer id) {
        return questionMapper.updateOneLikeCountById(id);
    }
}
