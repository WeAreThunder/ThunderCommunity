package love.thunder.community.demo.mapper;

import love.thunder.community.demo.model.Question;import org.apache.ibatis.annotations.Insert;import org.apache.ibatis.annotations.Param;import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);

    List<Question> selectAll();

	List<Question> selectAllByCreator(@Param("creator")Integer creator);

	int updateOneViewCountById(@Param("id")Integer id);
    //回复数+1
	int updateOneCommentCountById(@Param("id")Integer id);

    List<Question> selectByTagLike(@Param("id") Integer id, @Param("likeTag")String likeTag);

    List<Question> selectByTitleLike(@Param("likeTitle")String likeTitle);

    int updateOneLikeCountById(@Param("id")Integer id);



}