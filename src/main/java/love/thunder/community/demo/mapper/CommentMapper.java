package love.thunder.community.demo.mapper;

import love.thunder.community.demo.model.Comment;import org.apache.ibatis.annotations.Param;import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectByParentId(@Param("parentId") Integer parentId);

    //以从小到大的方式查找不同type的问题回复
    List<Comment> selectByParentIdAndTypeOrderByGmtCreateDesc(@Param("parentId") Integer parentId, @Param("type") Integer type);

    int updateOneCommentCountById(@Param("id")Integer id);

    int updateOneLikeCountById(@Param("id")Integer id);



}