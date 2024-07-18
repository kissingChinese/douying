<template>
  <v-dialog v-model="dialog" persistent >
    <template v-slot:activator="{ props }" >
      <v-btn v-bind="props" :ripple="false" v-if="!userStroe.token"><v-icon>mdi-account</v-icon>登录</v-btn>
      <v-btn v-else :ripple="false" @click="outlogo()">退出登录</v-btn>
    </template>
    <v-card class="mx-auto" elevation="1" width="100%" max-width="500" style="background-color: rgb(0, 106, 255);">
      <component :is="components[isLogin]" :show-message="showMessage" :close-event="closeEvent" />
      <v-row class="ma-0">
        <v-sheet width="100%" style="text-align:center;background-color: rgb(0, 106, 255)" >
          <v-btn :variant="'plain'" @click="isLogin = 2" v-show="isLogin != 2">忘记密码?</v-btn>
        </v-sheet>
        <v-col>
          <v-btn block class="text-none" color="grey-lighten-3" variant="tonal" @click="dialog = false">
            取消
          </v-btn>
        </v-col>
        <v-col> <v-btn block class="text-none mb-4" color="orange" :variant="'tonal'"
            @click="() => { isLogin != 0 ? isLogin = 0 : isLogin = 1 }">
            {{ isLogin == 0 ? '注册' : '登录' }}账号
          </v-btn></v-col>
      </v-row>
      <v-snackbar v-model="snackbar.show" :color="snackbar.color">
        {{ snackbar.text }}

        <template v-slot:actions>
          <v-btn color="blue" variant="text" @click="snackbar.show = false">
            了解
          </v-btn>
        </template>
      </v-snackbar>
    </v-card>
  </v-dialog>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { apiGetUserInfo } from '../../apis/user/user';
import { apiInitFollowFeed } from '../../apis/video';
import router from '../../router';
import { useUserStore } from '../../stores';
import forget from './forget.vue';
import login from './login.vue';
import register from './register.vue';
import EventBus from "../../apis/common/bus"


const userStroe = useUserStore()
const dialog = ref(false)
const isLogin = ref(0)
const components = [
  login,
  register,
  forget
]
const snackbar = ref({
  text: "",
  show: false
})
const getUserInfo = () => {
  apiGetUserInfo().then(({ data }) => {
    if (!data.state) {
      userStroe.$patch({
        info: {},
        token: null
      })
      return
    }
    apiInitFollowFeed()
    userStroe.$patch({
      info: data.data
    })
  })
}
const showMessage = (text, color) => {
  snackbar.value = {
    text,
    color,
    show: true
  }
}
const closeEvent = (data) => {
  userStroe.$patch({
    token: data.token
  })
  sessionStorage.setItem("token", data.token)
  getUserInfo()
  dialog.value = false
}
const outlogo = () => {
  sessionStorage.removeItem("token")
  userStroe.$patch({
    token: null,
    info: {}
  })
  router.push({ path: "/" })
}
onMounted(() => {
  getUserInfo()
  EventBus.on("send",val=>{
    dialog.value = val
  })
})
</script>