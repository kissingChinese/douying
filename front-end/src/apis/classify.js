import request from './request'

/**
 * 获取所有视频分类
 * @returns 分类
 */
export const apiClassifyGetAll = ()=>{
    return request.get("/index/types")
}

/**
 * 获取用户订阅的分类
 * @returns 成功
 */
export const apiGetClassifyByUser = ()=>{
    return request.get(`/customer/subscribe`)
}

/**
 * 用户订阅分类/取消订阅分类
 * @param {int} id 分类id
 * @returns 成功
 */
export const apiClassifySubscribe = (id)=>{
    console.log(id)
    return request.post(`/customer/subscribe?types=${id}`)
}
/**
 * 未订阅的分类
 * @returns 
 */
export const apiGetNoSubscribe =()=>{
    return request.get(`/customer/noSubscribe`)
}