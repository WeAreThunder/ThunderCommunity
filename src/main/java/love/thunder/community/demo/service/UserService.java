package love.thunder.community.demo.service;

import love.thunder.community.demo.mapper.UserMapper;
import love.thunder.community.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//当有用户登录时，如果时新用户，则创建，如果不是，则更新老用户
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
        User dbUser = userMapper.selectAllByAccountId(user.getAccountId());
        if (dbUser == null){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            //根据用户id更新姓名，头像，token
            userMapper.updateNameAndAvatarUrlAndTokenById(dbUser.getName(),dbUser.getAvatarUrl(),dbUser.getToken(),dbUser.getId());
        }
    }
}
