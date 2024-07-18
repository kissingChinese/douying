import { defineStore } from 'pinia'
export default defineStore('user', {
    state: ()=> ({
        info: {},
        token: sessionStorage.getItem("token"),
        lookId: null
    })
})