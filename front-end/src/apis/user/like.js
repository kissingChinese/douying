import request from '../request'


/**
 * 获取用户的粉丝或关注人员(fans/follows)
 * @param {int} userId 用户Id
 * @param {int} page 当前页
 * @param {int} limit 条数
 */
export const apiGetLike = (type = "fans", userId = 1, page = 1, limit = 10) => {
    return request.get(`/customer/${type}`, {
        params: {
            userId,
            page,
            limit
        }
    })
}

/**
 * 关注/取关
 * @param {int} followsUserId 用户id
 * @returns 
 */
export const apiFollows = (followsUserId) => {
    return request.post(`/customer/follows`, null, {
        params: {
            followsUserId
        }
    })
}