package love.thunder.community.demo.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import love.thunder.community.demo.model.Notification;

public interface NotificationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notification record);

    int insertSelective(Notification record);

    Notification selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKey(Notification record);

    List<Notification> selectByReceiverOrderByGmtCreateDesc(@Param("receiver")Integer receiver);

    List<Notification> selectByReceiverAndStatus(@Param("receiver")Integer receiver,@Param("status")Integer status);

    int updateStatusById(@Param("updatedStatus")Integer updatedStatus,@Param("id")Integer id);
}