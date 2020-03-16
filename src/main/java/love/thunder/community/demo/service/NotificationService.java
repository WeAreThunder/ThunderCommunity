package love.thunder.community.demo.service;

import love.thunder.community.demo.dto.NotificationDTO;
import love.thunder.community.demo.model.User;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import love.thunder.community.demo.model.Notification;
import love.thunder.community.demo.mapper.NotificationMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Resource
    private NotificationMapper notificationMapper;


    public int deleteByPrimaryKey(Integer id) {
        return notificationMapper.deleteByPrimaryKey(id);
    }


    public int insert(Notification record) {
        return notificationMapper.insert(record);
    }


    public int insertSelective(Notification record) {
        return notificationMapper.insertSelective(record);
    }


    public Notification selectByPrimaryKey(Integer id) {
        return notificationMapper.selectByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(Notification record) {
        return notificationMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(Notification record) {
        return notificationMapper.updateByPrimaryKey(record);
    }

    public List<Notification> selectByReceiverOrderByGmtCreateDesc(Integer receiver) {
        return notificationMapper.selectByReceiverOrderByGmtCreateDesc(receiver);
    }

    public List<Notification> selectByReceiverAndStatus(Integer receiver, Integer status) {
        return notificationMapper.selectByReceiverAndStatus(receiver, status);
    }

    public int updateStatusById(Integer updatedStatus, Integer id) {
        return notificationMapper.updateStatusById(updatedStatus, id);
    }

}

