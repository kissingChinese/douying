import * as qiniu from 'qiniu-js'
import request from '../request'
const Config = {
    qiniuOSS: {}
}
/**
 * 获取用户信息
 * @param {Int} userId 用户Id 
 * @returns 
 */
export const apiGetUserInfo = (userId="")=>{
    return request.get(`/customer/getInfo/${userId}`)
}

export const apiAvatarGetToken = async() => await request.get("/customer/avatar/token")

export const apiUploadAvatar = async (file, callBack={next:()=>{},error:()=>{}, complete:()=>{}})=> {
    const videoToken = (await apiAvatarGetToken()).data.data
    
    const observable = qiniu.upload(file, null, videoToken, {}, Config.qiniuOSS)
    const subscription = observable.subscribe(callBack.next, callBack.error, callBack.complete) // 这样传参形式也可以
    // subscription.unsubscribe() // 上传取消
    return subscription;
}

/**
 * 修改用户信息
 * @param {object} info 用户信息
 */
export const apiChangeUserInfo = (info)=>{
    return request.put(`/customer`, info)
}

/**
 * 获取搜索记录
 * @returns 
 */
export const apiGetUserSearchHistory = ()=>{
    return request.get(`/index/search/history`)
}