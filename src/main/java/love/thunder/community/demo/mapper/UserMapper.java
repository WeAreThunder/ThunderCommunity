package love.thunder.community.demo.mapper;

import love.thunder.community.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectAll();

    List<User> selectAllByToken(@Param("token") String token);

    User selectAllByAccountId(@Param("accountId") String accountId);

    int updateNameAndAvatarUrlAndTokenById(@Param("updatedName") String updatedName, @Param("updatedAvatarUrl") String updatedAvatarUrl, @Param("updatedToken") String updatedToken, @Param("id") Integer id);

    List<User> selectByIds(@Param("ids")List<Integer> ids);


}