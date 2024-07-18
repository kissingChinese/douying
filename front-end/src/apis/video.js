import * as qiniu from 'qiniu-js'
import request from './request'

const Config = {
    qiniuOSS: {}
}

export const apiVideoGetToken = async() => await request.get("/video/token")

export const apiVideoUpload = async (file, callBack={next:()=>{},error:()=>{}, complete:()=>{}})=> {
    const videoToken = (await apiVideoGetToken()).data.data
    const observable = qiniu.upload(file, null, videoToken, {}, Config.qiniuOSS)
    const subscription = observable.subscribe(callBack.next, callBack.error, callBack.complete) // 这样传参形式也可以
    // subscription.unsubscribe() // 上传取消
    return subscription;
}

/**
 * 根据分类获取视频
 * @param {int} classfiyId 分类id
 */
export const apiVideoByClassfiy = (classfiyId, page=1, limit=15)=>{
    if(classfiyId>0)
    return request.get(`/index/video/type/${classfiyId}`, {
        params: {
            page,
            limit
        }
    })
    return apiVideoByHot()
}
/**
 * 获取视频排行榜
 * @returns 
 */
export const apiVideoHotRank = () =>{
    console.log("???/")
    return request.get(`/index/video/hot/rank`)
}
/**
 * 获取热门视频
 * @returns 
 */
export const apiVideoByHot = ()=>{
    return request.get(`/index/video/hot`)
}

/**
 * 获取主页推送视频
 */
export const apiVideoByPush = ()=>{
    return request.get("/index/pushVideos")
}

/**
 * 获取指定收藏夹的视频
 * @param {int} favoriteId 收藏夹id
 * @returns 成功
 */
export const apiGetVideoByFavoriteId = (favoriteId=0)=>{
    return request.get(`/video/favorites/${favoriteId}`)
}

/**
 * 获取用户自己的视频
 * @param {int} page 当前页 
 * @param {int} limit 条数
 * @returns 成功
 */
export const apiGetVideoByUser = (page=1, limit=5)=>{
    return request.get(`/video`, {
        params: {
            page,
            limit
        }
    })
}

/**
 * 根据视频标签推送相似视频
 * @param {Array<String>} labels 视频标签列表
 * @returns 
 */
export const apiGetVideoBySimilar = (labelNames,id)=>{
    return request.get(`/index/video/similar`, {
        params: {
            labelNames,
            id
        }
    })
}

/**
 * 点赞视频
 * @param {int} videoId 视频id
 * @returns 
 */
export const apiStarVideo = (videoId)=>{
    return request.post(`/video/star/${videoId}`)
}

/**
 * 获取浏览记录
 * @returns 
 */
export const apiGetHistoryVideo = ()=>{
    return request.get(`/video/history`)
}

/**
 * 搜索视频
 * @param {string} searchName 搜索参数,可能是标题,用户,YV
 * @param {int} page 当前页
 * @param {int} limit 条数
 * @returns 
 */
export const apiSearchVideo = (searchName, page=1, limit=10) =>{
    return request.get(`/index/search`, {
        params: {
            searchName,
            page,
            limit
        }
    })
}

/**
 * 获取审核速度
 * @returns 
 */
export const apiGetAuditQueueState = ()=>{
    return request.get(`/video/audit/queue/state`)
}

/**
 * 添加视频到浏览记录
 * @param {int} id 视频id
 * @returns 
 */
export const apiAddHistory = (id)=>{
    return request.post(`/video/history/${id}`)
}

/**
 * 根据视频id获取视频详情
 * @param {int} id 
 * @returns 
 */
export const apiGetVideoById = (id)=>{
    return request.get(`/index/video/${id}`)
}

/**
 * 修改用户模型,视频推荐(重点)
 * @param {int} id 视频id 
 * @param {string} labels 视频标签
 * @param {number} score 分数 -0.5, 1
 * @returns 
 */
export const apiSetUserVideoModel = (id, labels, score)=>{
    return request.post(`/customer/updateUserModel`, {
        id, labels, score
    })
}
/**
 * 推送关注人的视频
 * @param {number} lastTime 
 * @returns 
 */
export const apiGetFollowVideo = (lastTime)=>{
    if(!lastTime) lastTime = Date.now()
    return request.get(`/video/follow/feed`,{
        params: {
            lastTime
        }
    })
}

/**
 * 初始化关注流
 * @returns 
 */
export const apiInitFollowFeed = ()=>{
    return request.post("/video/init/follow/feed")
}

/**
 * 根据用户id获取视频
 * @param {int} id 用户id
 * @returns 
 */
export const apiGetUserVideoById = (userId, page=1, limit=10)=>{
    return request.get(`/index/video/user`, {
        params: {
            userId,
            page,
            limit
        }
    })
}

/**
 * 分享视频记录
 * @param {int} id 视频Id
 * @returns 
 */
export const apiShareVideo = (id)=>{
    return request.post(`/index/share/${id}`)
}