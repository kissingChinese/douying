<template>
  <v-navigation-drawer color="#252632">
    <v-list v-if="userStore.token">
      <v-list-item :prepend-avatar="userStore.info.avatar?apiFileGet(userStore.info.avatar):'/logo.png'" :title="userStore.info.nickName"
        :subtitle="userStore.info.description"></v-list-item>
    </v-list>
    <v-list v-else>
      <v-list-item prepend-icon="mdi-account" title="当前未登录" subtitle="请登入后体验完整功能" @click="openLogin">
      </v-list-item>
    </v-list>
    <v-divider />
    <v-list density="compact" nav>
      <v-list-item prepend-icon="mdi-home" title="热门视频" to="/"></v-list-item>
      <v-list-item prepend-icon="mdi-video" title="推荐视频" to="/pushVideo"></v-list-item>
      <!-- <v-list-item prepend-icon="mdi-label-multiple" title="视频分类" to="/classify"></v-list-item> -->
      <template v-if="userStore.token">
        <v-list-item prepend-icon="mdi-account" title="个人中心" to="/user"></v-list-item>
        <v-list-item prepend-icon="mdi-heart" title="关注的人" to="/followVideo"></v-list-item>
      </template>
      <v-list-item :prepend-icon="item.icon || 'mdi-file-document-alert-outline'" :title="item.name"
        v-for="item in allClassifyList" :to="`/video/${item.id}`"></v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>
<script setup>
import { ref } from 'vue';
import { apiClassifyGetAll } from '../../apis/classify';
import { apiFileGet } from '../../apis/file';
import { useUserStore } from '../../stores';
import EventBus from "../../apis/common/bus"
const userStore = useUserStore()
const allClassifyList = ref([])
const openLogin = ()=>{
  EventBus.emit("send",true)
  console.log("我发送了信号")
}
apiClassifyGetAll().then(({ data }) => {
  if (!data.state) {
    allClassifyList.value = []
    return;
  }
  allClassifyList.value = data.data
})
</script>
<style lang="scss" scoped>
.v-navigation-drawer {
  border: none !important;

}
</style>
