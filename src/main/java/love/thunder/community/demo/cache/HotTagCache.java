package love.thunder.community.demo.cache;

import lombok.Data;
import love.thunder.community.demo.dto.HotTagDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotTagCache {
    private List<String> hots = new ArrayList<>();

    //优先队列，记得自己去查资料了解啊
    public void updateTags(Map<String,Integer> tags){
        int max = 5;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        tags.forEach((name,priority)->{
            HotTagDTO hotTagDTo = new HotTagDTO();
            hotTagDTo.setName(name);
            hotTagDTo.setPriority(priority);
            if (priorityQueue.size() < max){
                priorityQueue.add(hotTagDTo);
            }else {
                //当前标签的热度大于最小热度标签的热度，则插入
                HotTagDTO minHot = priorityQueue.peek();
                if(hotTagDTo.compareTo(minHot) > 0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTo);
                }
            }
        });

        List<String> sortedTags = new ArrayList<>();

        HotTagDTO poll = priorityQueue.poll();
        hots.add(poll.getName());
        while (poll != null){
            sortedTags.add(0,poll.getName());
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
    }
}
