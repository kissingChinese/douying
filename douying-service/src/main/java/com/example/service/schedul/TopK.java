package com.example.service.schedul;
import com.example.entity.vo.HotVideo;
import java.util.*;

/**
 * 使用优先队列来存储热门视频
 */
public class TopK {
    private int k = 0; //排行前k的视频
    private Queue<HotVideo> queue;
    public TopK(int k,Queue<HotVideo> queue){
        this.k = k;
        this.queue = queue;
    }
    public void add(HotVideo hotVideo) {
        if (queue.size() < k) {
            queue.add(hotVideo);
        } else if (queue.peek().getHot() < hotVideo.getHot()){
            queue.poll();
            queue.add(hotVideo);
        }
        return;
    }
    public List<HotVideo> get(){
        final ArrayList<HotVideo> list = new ArrayList<>(queue.size());
        while (!queue.isEmpty()) {
            list.add(queue.poll());
        }
        Collections.reverse(list);
        return list;
    }


}
