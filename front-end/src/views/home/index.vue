<template>
  <v-container style="height: 500px;">
    <VideoListVue :video-list="videoList" :showHot="currentClassify == 0" />
    <v-dialog :model-value="dialog" fullscreen transition="dialog-bottom-transition">
      <v-card v-if="dialog">
        <Video :video-info="searchVideoInfo" :close-video="() => searchVideoInfo = null" />
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script setup>
import { computed, onMounted, onUnmounted, onUpdated, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { apiGetClassifyByUser } from '../../apis/classify';
import { apiGetVideoById, apiSearchVideo, apiVideoByClassfiy } from '../../apis/video';
import Video from '../../components/video/index.vue';
import VideoListVue from '../../components/video/list.vue';
const userClassifys = ref([])
const isLoading = ref(true)
const videoList = ref([])
const currentClassify = ref(0)
const route = useRoute()
const searchVideoInfo = ref(null)
const pageInfo = ref({
  page: 1,
  limit: 15
})
const dialog = computed(() => searchVideoInfo.value ? true : false)
// 获取分类视频
const getCurrentClassifyVideo = () => {
  if (route.meta.isSearch) return;
  isLoading.value = true
  apiVideoByClassfiy(currentClassify.value, pageInfo.value.page, pageInfo.value.limit).then(({ data }) => {
    videoList.value = videoList.value.concat(data.data)
    isLoading.value = false
  })
}
const getSearchVideo = () => {
  apiSearchVideo(route.params.key, pageInfo.value.page, pageInfo.value.limit).then(({ data }) => {
    isLoading.value = false
    if (!data.state) {
      return;
    }
    videoList.value = videoList.value.concat(data.data.records)
  })
}
watch(currentClassify, () => {
  videoList.value = []
  pageInfo.value.page = 1
  getCurrentClassifyVideo()
})
const initView =
  () => {
    if (route.meta.isClassify) {
      pageInfo.value.page = 1
      videoList.value = []
      currentClassify.value = route.params.classify
    } else if (route.meta.isSearch) {
      // 搜索
      videoList.value = []
      getSearchVideo()
      currentClassify.value = -1
      return;
    } else {
      pageInfo.value.page = 1
      videoList.value = []
      currentClassify.value = 0
      apiGetClassifyByUser().then(({ data }) => {
        userClassifys.value = data.data
      })
      getCurrentClassifyVideo()
    }
    if (route.query.play) {
      apiGetVideoById(route.query.play).then(({ data }) => {
        if (!data.state) {
          return;
        }
        searchVideoInfo.value = data.data
      })
    }
  }

const listenScroll = () => {
  //变量scrollTop是滚动条滚动时，距离顶部的距离
  var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
  //变量windowHeight是可视区的高度
  var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
  //变量scrollHeight是滚动条的总高度
  var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
  //滚动条到底部的条件
  let aaa = scrollHeight - ((scrollTop + windowHeight))
  if (aaa < 150 && aaa > -1 && !isLoading.value) {
    isLoading.value = true
    pageInfo.value.page++
    if (route.meta.isSearch) getSearchVideo()
    else {
      getCurrentClassifyVideo()
    }
  }
}
onUpdated(() => {
  initView()
})
onMounted(() => {
  initView()
  window.addEventListener('scroll', listenScroll);
})
onUnmounted(() => {
  window.removeEventListener('scroll', listenScroll);
})
</script>
<style scoped></style>