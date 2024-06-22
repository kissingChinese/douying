package com.example.service.audit;

import com.example.entity.response.AuditResponse;
import com.example.entity.task.VideoTask;
import com.example.entity.video.Video;
import com.example.mapper.video.VideoMapper;
import com.example.config.QiNiuConfig;
import com.example.constant.AuditStatus;
import com.example.service.FeedService;
import com.example.service.FileService;
import com.example.service.QiNiuFileService;
import com.example.service.InterestPushService;
import com.example.service.user.FollowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  视频发布审核
 */
@Service
public class VideoPublishAuditServiceImpl implements AuditService<VideoTask,VideoTask> , InitializingBean,BeanPostProcessor {

    @Autowired
    private FeedService feedService;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private InterestPushService interestPushService;

    @Autowired
    private QiNiuFileService qiNiuFileService;

    @Autowired
    private TextAuditService textAuditService;

    @Autowired
    private ImageAuditService imageAuditService;

    @Autowired
    private VideoAuditService videoAuditService;

    @Autowired
    private FollowService followService;

    @Autowired
    private FileService fileService;

    private int maximumPoolSize = 8;

    protected ThreadPoolExecutor executor;

    /**
     *
     * @param videoTask
     * @param auditQueueState 申请快/慢审核
     * @return
     */
    public VideoTask audit(VideoTask videoTask,Boolean auditQueueState){

        if (auditQueueState){
            new Thread(()->{
                audit(videoTask);
            }).start();
        }else {
            audit(videoTask);
        }
        return null;
    }

    // 进行任务编排
    @Override
    public VideoTask audit(VideoTask videoTask) {
        executor.submit(()->{
            final Video video = videoTask.getVideo();
            final Video video1 = new Video();
            BeanUtils.copyProperties(video,video1);
            // 只有视频在新增或者公开时候才需要调用审核视频/封面
            // 新增 ： 必须审核
            // 修改: 新老状态不一致
            // 需要审核视频/封面
            boolean needAuditVideo = false;
            if (videoTask.getIsAdd()  && videoTask.getOldState() == videoTask.getNewState()){
                needAuditVideo = true;
            }else if (!videoTask.getIsAdd() && videoTask.getOldState() != videoTask.getNewState()){
                // 修改的情况下新老状态不一致,说明需要更新
                if (!videoTask.getNewState()){
                   needAuditVideo = true;
                }
            }
            AuditResponse videoAuditResponse = new AuditResponse(AuditStatus.SUCCESS,"正常");
            AuditResponse coverAuditResponse = new AuditResponse(AuditStatus.SUCCESS,"正常");
            AuditResponse titleAuditResponse = new AuditResponse(AuditStatus.SUCCESS,"正常");
            AuditResponse descAuditResponse = new AuditResponse(AuditStatus.SUCCESS,"正常");

            if (needAuditVideo){

                  videoAuditResponse = videoAuditService.audit(QiNiuConfig.CNAME+"/"+fileService.getById(video.getUrl()).getFileKey());
                  coverAuditResponse = imageAuditService.audit(QiNiuConfig.CNAME+"/"+fileService.getById(video.getCover()).getFileKey());
                interestPushService.pushSystemTypeStockIn(video1);
                interestPushService.pushSystemStockIn(video1);

                // 推入发件箱
                feedService.pusOutBoxFeed(video.getUserId(),video.getId(),video1.getGmtCreated().getTime());
            }else if (videoTask.getNewState()){
                interestPushService.deleteSystemStockIn(video1);
                interestPushService.deleteSystemTypeStockIn(video1);
                // 删除发件箱以及收件箱
                final Collection<Long> fans = followService.getFans(video.getUserId(), null);
                feedService.deleteOutBoxFeed(video.getUserId(),fans,video.getId());
            }

            // 新老视频标题简介一致
            final Video oldVideo = videoTask.getOldVideo();
            if (!video.getTitle().equals(oldVideo.getTitle())) {
                titleAuditResponse = textAuditService.audit(video.getTitle());
            }
            if (!video.getDescription().equals(oldVideo.getDescription()) && !ObjectUtils.isEmpty(video.getDescription())){
                descAuditResponse = textAuditService.audit(video.getDescription());
            }

            final Integer videoAuditStatus = videoAuditResponse.getAuditStatus();
            final Integer coverAuditStatus = coverAuditResponse.getAuditStatus();
            final Integer titleAuditStatus = titleAuditResponse.getAuditStatus();
            final Integer descAuditStatus = descAuditResponse.getAuditStatus();
            boolean f1 = videoAuditStatus == AuditStatus.SUCCESS;
            boolean f2 = coverAuditStatus == AuditStatus.SUCCESS;
            boolean f3 = titleAuditStatus == AuditStatus.SUCCESS;
            boolean f4 = descAuditStatus == AuditStatus.SUCCESS;

            if (f1 && f2 && f3 && f4) {
                video1.setMsg("通过");
                video1.setAuditStatus(AuditStatus.SUCCESS);
                // 填充视频时长
            }else {
                video1.setAuditStatus(AuditStatus.PASS);
                // 避免干扰
                video1.setMsg("");
                if (!f1){
                    video1.setMsg("视频有违规行为: "+videoAuditResponse.getMsg());
                }
                if (!f2){
                    video1.setMsg(video1.getMsg()+"\n封面有违规行为: " + coverAuditResponse.getMsg());
                }
                if (!f3){
                    video1.setMsg(video1.getMsg()+"\n标题有违规行为: " + titleAuditResponse.getMsg());
                }
                if (!f4){
                    video1.setMsg(video1.getMsg()+"\n简介有违规行为: " + descAuditResponse.getMsg());
                }
            }

            videoMapper.updateById(video1);
        });

        return null;
    }
    public boolean getAuditQueueState(){
        return executor.getTaskCount() < maximumPoolSize;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executor  = new ThreadPoolExecutor(5, maximumPoolSize, 60, TimeUnit.SECONDS, new ArrayBlockingQueue(1000));
    }
}
