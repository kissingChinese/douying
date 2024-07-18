import request from './request'

/**
 * 模糊搜索，可搜索视频与用户
 * @param {关键词: 标题,用户,YV(视频id)} name 
 */
export const apiCommonSearch = (name)=>{
    return request.get(`/index/search?searchName=${name}`)
}