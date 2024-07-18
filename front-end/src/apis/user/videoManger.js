import request from '../request';

/**
 * 发布视频/修改视频
 * @param {object} videoInfo 视频信息
 * @returns 成功
 */
export const apiVideoPush = (videoInfo)=>{
    if(!videoInfo) return;
    videoInfo = Object.assign({},videoInfo)
    videoInfo.labelNames= videoInfo.labelNames.join(",")
    videoInfo.auditStatus = 0
    return request.post("/video", videoInfo);
}

/**
 * 删除视频
 * @param {int} id 视频id
 * @returns 
 */
export const apiRemoveVideo = (id)=>{
    return request.delete(`/video/${id}`)
}