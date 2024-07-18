import * as qiniu from 'qiniu-js'
import request from './request'
const apiFileGetToken = async() => await request.get("/file/getToken")
const Config = {
    qiniuOSS: {}
}
/**
 * 上传文件
 * @param {file} file 文件
 * @param {{next:()=>{},error:()=>{}, complete:()=>{}}} callBack 回调，（注：complete(qiniuResult,pushFileIdResult)）
 * @returns 
 */
export const apiUploadFile = async (file, callBack={next:()=>{},error:()=>{}, complete:()=>{}})=> {
    const fileToken = (await apiFileGetToken()).data.data
    const observable = qiniu.upload(file, null, fileToken, {}, Config.qiniuOSS)
    const subscription = observable.subscribe(callBack.next, callBack.error, (e)=>{
        apiPushFile(e.key, (fileId)=>callBack.complete(e,fileId))
    }) // 这样传参形式也可以
    // subscription.unsubscribe() // 上传取消
    return subscription;
}
const apiPushFile = async (fileKey, callback=()=>{})=>{
    let result = (await request.post(`/file`,null, {
        params: {
            fileKey
        }
    }))
    callback(result.data)
} 

/**
 * 查看文件
 * @param {int} fileId 文件id
 * @returns 
 */
export const apiFileGet = (fileId)=>{
    return request.getUri({
        url: `/file/${fileId}`
    })
}