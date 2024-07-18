import request from './request'

export const apiAi = (question)=>{
    return request.get("/ai/sendQuestion?question="+question)
}