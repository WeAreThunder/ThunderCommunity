package love.thunder.community.demo.schedule;

import com.mysql.cj.log.Log;
import lombok.extern.slf4j.Slf4j;
import love.thunder.community.demo.cache.HotTagCache;
import love.thunder.community.demo.mapper.QuestionMapper;
import love.thunder.community.demo.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.*;

//创建定时任务
@Component
@Slf4j
public class HotTagTasks {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache HotTagCache;

    //每10秒执行一次
    @Scheduled(fixedRate = 10000)
    //每天凌晨一点执行
//    @Scheduled(cron = "0 0 1 * * *")
    public void hotTagScheduled(){
        log.info("The hotTagScheduled is start: {}",new Date());
        List<Question> questions = questionMapper.selectAll();
        Map<String, Integer> priorities = new HashMap<>();
        for (Question question : questions) {
            //使用中文或者英文逗号进行分隔
            String[] tags = StringUtils.split(question.getTag(), ",|，");
            for (String tag : tags) {
                Integer priority = priorities.get(tag);
                if (priority != null){
                    priorities.put(tag,priority + 5 + question.getCommentCount());
                }else {
                    priorities.put(tag,5 + question.getCommentCount());
                }
            }
        }
        HotTagCache.updateTags(priorities);
        log.info("The hotTagScheduled is stop: {}",new Date());
    }
}

//根据需要用在@Schedul（cron = ""）里
//        0 0 10,14,16 * * ? 每天上午10点，下午2点，4点
//        0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时
//        0 0 12 ? * WED 表示每个星期三中午12点
//        "0 0 12 * * ?" 每天中午12点触发
//        "0 15 10 ? * *" 每天上午10:15触发
//        "0 15 10 * * ?" 每天上午10:15触发
//        "0 15 10 * * ? *" 每天上午10:15触发
//        "0 15 10 * * ? 2005" 2005年的每天上午10:15触发
//        "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
//        "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
//        "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
//        "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
//        "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
//        "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
//        "0 15 10 15 * ?" 每月15日上午10:15触发
//        "0 15 10 L * ?" 每月最后一日的上午10:15触发
//        "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
//        "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
//        "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发