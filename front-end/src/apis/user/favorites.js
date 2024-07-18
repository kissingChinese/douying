import request from '../request'

/**
 * 获取用户收藏夹
 * @param {收藏夹id} id 不填则获取用户所有收藏夹
 * @returns 收藏夹信息
 */
export const apiGetFavorites = (id=null)=>{
    // 获取指定收藏夹信息
    if(id>0) {
        return request.get(`/customer/favorites/${id}`)
    }
    // 所有
    return request.get(`/customer/favorites`)
}

/**
 * 新增/修改收藏夹
 * @param {收藏夹信息} favoriteData 
 * @returns 成功
 */
export const apiSaveFavorite = (favoriteData)=>{
    return request.post(`/customer/favorites`, favoriteData)
}

/**
 * 删除收藏夹
 * 删除收藏夹，会一并把收藏夹中的视频也删除，请告知用户
 * @param {被删除的收藏夹id} id 多个以 ， 分割: 1,2,3 或 1
 * @returns 成功
 */
export const apiRemoveFavorite = (id) => {
    return request.delete(`/customer/favorites/${id}`)
}

/**
 * 收藏视频
 * @param {int} favoriteId 收藏夹id 
 * @param {int} videoId 视频id
 * @returns 
 */
export const apiFavoriteVideo = (favoriteId, videoId)=>{
    return request.post(`/video/favorites/${favoriteId}/${videoId}`)
}