import request from '../request'

/**
 * 登录或注册
 * @param {int} type 验证类型 1=登录，0=注册
 * @param {object} data 验证信息
 * @returns 
 */
export const apiAuth = (type=1, data)=>{
    return request.post(type?`/login`:`/login/register`, data)
}

/**
 * 忘记密码，重新设置密码
 * @param {object} data 
 * @returns 
 */
export const apiForgetPassword = (data)=>{
    return request.post(`/login/findPassword`, data)
}

/**
 * 获取验证码
 * @param {int} type 验证码类型 1=图形验证码 0=邮箱验证码
 * @param {object} data 请求体
 */
export const apiGetCode = (type=1, data) => {
    if(type) {
        // 图形
        return request.getUri({url: `/login/captcha.jpg/${data}`}) 
    }else return request.post(`/login/getCode`, data, {
        
    })
}

/**
 * 验证验证码
 * @param {string} email 邮箱 
 * @param {string} code 验证码
 * @returns 
 */
export const apiCheckCode = (data)=>{
    return request.post(`/login/check`,null, {
        params: data
    })
}


/**
 * 访问文件
 * @returns 
 */
export const apiGetCdnAuthFile = (fileId)=>{
    return request.getUri({
        url: `/cdn/auth/get/${fileId}`
    })

}