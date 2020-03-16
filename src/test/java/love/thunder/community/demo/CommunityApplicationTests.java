package love.thunder.community.demo;

import love.thunder.community.demo.mapper.UserMapper;
import love.thunder.community.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        List<User> users = userMapper.selectByIds(ids);
        System.out.println(users.toString());
    }

}
