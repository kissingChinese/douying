<template>
  <v-layout v-if="currentVideo" full-height>
    <v-navigation-drawer app permanent v-model="drawer" location="right" :width="350" style="background-color: #252632;">
      <v-card color="background" class="pa-4" id="videoPlayList">
        <VideoCard :overlay="currentIndex == index" class="mb-4" :video-info="videoItem"
          v-for="(videoItem, index) in similarList" :key="index" @click="currentIndex = index" />
      </v-card>
    </v-navigation-drawer>
    <v-main>
      <v-card rounded="0" width="100%" height="100%">
        <video ref="video" class="video-js vjs-default-skin" controls :poster="currentVideo.playCover">
          <source :src="currentVideo.playUrl" :type="currentVideo.videoType || 'video/mp4'" />
        </video>
        <div style="position: absolute;left: 15px;top: 15px;z-index: 99999;">
          <v-btn size="40" color="bg" icon v-if="!hideClose" @click="closeVideo">
            <v-icon :size="20">mdi-close</v-icon>
          </v-btn>
        </div>
        <v-card class="pa-2" elevation="0" style="display: flex; flex-direction: column;
    gap: 12px;position: absolute; background-color: transparent; right: 25px; bottom: 25px;z-index: 99999;">
          <v-badge color="red" icon="mdi-plus" location="bottom" @click="likeUser()">
            <v-avatar class="elevation-2" :image="currentVideo.user.avatar?apiFileGet(currentVideo.user.avatar): '/logo.png'"></v-avatar>
          </v-badge>
          <v-btn size="40" color="blue" icon @click="openRgihtD()">
            <v-icon :size="20">mdi-more</v-icon>
          </v-btn>
          <v-badge color="red" :content="currentVideo.startCount" location="bottom">
            <v-btn size="40" :color="'pink'" icon @click="starVideo()">
              <v-icon :size="20">mdi-heart</v-icon>
            </v-btn>
          </v-badge>

          <FavoriteCom :video-id="currentVideo.id" :callback="favoriteCallBack">
            <template #default="{ props }">
              <v-badge color="red" :content="currentVideo.favoritesCount" location="bottom">
                <v-btn v-bind="props" size="40" color="warning" icon>
                  <v-icon :size="20">mdi-star</v-icon>
                </v-btn>
              </v-badge>
            </template>
          </FavoriteCom>
          <v-badge color="red" :content="currentVideo.startCount" location="bottom">
            <v-btn size="40" color="success" icon @click="copyUrl()">
            <v-icon :size="20">mdi-near-me</v-icon>
          </v-btn>
          </v-badge>
          

        </v-card>
      </v-card>
      <v-snackbar v-model="snackbar.show" :color="snackbar.color">
        {{ snackbar.text }}

        <template v-slot:actions>
          <v-btn color="blue" variant="text" @click="snackbar.show = false">
            了解
          </v-btn>
        </template>
      </v-snackbar>
    </v-main>
  </v-layout>
</template>
<script setup>
import { computed, getCurrentInstance, nextTick, onMounted, onUnmounted, ref, watch } from 'vue';
import { apiFileGet } from '../../apis/file';
import { apiFollows } from '../../apis/user/like';
import { apiAddHistory, apiGetVideoBySimilar, apiInitFollowFeed, apiSetUserVideoModel, apiShareVideo, apiStarVideo } from '../../apis/video';
import FavoriteCom from '../../components/favorite/index.vue';
import VideoCard from '../../components/video/card.vue';
import strUtils from '../../utils/strUtil';
const props = defineProps({
  videoInfo: {
    type: Object,
    default: null
  },
  videoList: {
    type: Array,
    default: []
  },
  hideClose: {
    type: Boolean,
    default: false
  },
  nextVideo: {
    type: Function,
    default: () => {

    }
  },
  closeVideo: {
    type: Function,
    default: () => { }
  }
})
const snackbar = ref({
  show: false,
  text: ""
})

const shareBtn = ()=>{
  apiShareVideo(currentVideo.value.id)
}

const handleMouseWheel = (event) =>{
  console.log(event.deltaY)
  if(event.deltaY >0) {
    if (currentIndex.value >= similarList.value.length - 1) {
        return;
      }
      currentIndex.value++;
  }else {
    if (currentIndex.value < 1) {
        return;
      }
      currentIndex.value--
  }
}
const drawer = ref(true)
const instance = getCurrentInstance().proxy
const video = ref()
const videoPlayer = ref()
const similarList = ref([
  props.videoInfo
])

const currentIndex = ref(0)
const currentVideo = computed(() => {

  let temp = currentIndex.value >= 0 ? similarList.value[currentIndex.value] : props.videoInfo
  temp.playUrl = apiFileGet(temp.url)
  temp.playCover = apiFileGet(temp.cover)
  console.log(temp, "aa")
  return temp
})
const openRgihtD = () => {
  drawer.value = !drawer.value
}
const favoriteCallBack = (e) => {
  if (e == "已收藏") {
    currentVideo.value.favoritesCount++
  } else {
    currentVideo.value.favoritesCount--
  }
  snackbar.value = {
    show: true,
    text: e
  }
}
const isAddHistory = ref(true)
const isLikeVideo = ref(false)
const windowKeyEvent = (event) => {
  switch (event.which) {
    case 38:
      if (currentIndex.value < 1) {
        return;
      }
      currentIndex.value--
      break
    case 40:
      if (currentIndex.value >= similarList.value.length - 1) {
        return;
      }
      currentIndex.value++;
      break
    case 27:
      props.closeVideo()
      break
    case 70:
      videoPlayer.value.requestFullscreen()
      break
  }
}
const copyUrl = () => {
  shareBtn()
  snackbar.value = {
    text: strUtils.copyContent(location.host + "/#/?play=" + currentVideo.value.id) ? "视频地址复制成功" : "视频地址复制失败",
    show: true
  }
}
onUnmounted(() => {
  document.removeEventListener('wheel', handleMouseWheel);
  window.removeEventListener("keydown", windowKeyEvent)
})

const firstInitVideo = () => {
  console.log(currentVideo)
  if (videoPlayer.value || !currentVideo.value) return;
  videoPlayer.value = instance.$video(video.value, {
    playbackRates: [0.5, 1, 1.5, 2],
    notSupportedMessage: "暂不支持该视频类型",
    fill: true,
    autoplay: true
  })
  videoPlayer.value.volume(localStorage.getItem("volume") || 1)
  document.addEventListener('wheel', handleMouseWheel);
  window.addEventListener("keydown", windowKeyEvent)
  videoPlayer.value.on("volumechange", () => {
    localStorage.setItem("volume", videoPlayer.value.volume())
  })
  videoPlayer.value.on("timeupdate", function () {
    // 播放三秒后添加历史记录
    if (this.currentTime() >= 3 && isAddHistory.value) {
      isAddHistory.value = false
      apiAddHistory(currentVideo.value.id)
    }
    let duration = this.duration()
    let score = this.currentTime() >= (duration / 5)
    if (score) {
      if (!isLikeVideo.value)
        apiSetUserVideoModel(currentVideo.value.id, currentVideo.value.labelNames, 1)
      isLikeVideo.value = true;
    } else isLikeVideo.value = false

  })
  nextTick(() => {
    video.value.style['background-image'] = `url(${currentVideo.playCover})`
    videoPlayer.value.play()
  })
  if (props.videoList.length == 0) {
    apiGetVideoBySimilar(props.videoInfo.labelNames, props.videoInfo.id).then(({ data }) => {
      similarList.value = similarList.value.concat(data.data)
    })
  }

}
const likeUser = () => {
  apiFollows(currentVideo.value.user.id).then(({ data }) => {
    if (data.message == '已关注') {
      apiInitFollowFeed()
    }
    snackbar.value = {
      text: data.message,
      show: true
    }
  })
}
onMounted(() => {
  video.value.style['background-size'] = " cover"
  video.value.style['background-position'] = "center"
  video.value.style['backdrop-filter'] = "blur(50px)"
  if (document.documentElement.clientWidth < 800)
    drawer.value = false
  firstInitVideo()
})

const starVideo = () => {

  apiStarVideo(currentVideo.value.id).then(({ data }) => {
    snackbar.value = {
      show: true,
      text: data.message
    }
    if (!data.state) {
      return;
    }
    if (data.message == "已点赞") {
      currentVideo.value.startCount++
    } else {
      currentVideo.value.startCount--
    }


  })
}
const playVideo = (n) => {
  if (n) {
    props.nextVideo(currentIndex.value)
    // videoPlayer.value.reset()
    setTimeout(() => {
      video.value.style['background-image'] = `url(${n.playCover})`

      firstInitVideo()
      isAddHistory.value = true
      videoPlayer.value.src([
        {
          src: n.playUrl,
          type: n.videoType,
          poster: n.playCover
        }
      ])
      videoPlayer.value.load()
      videoPlayer.value.play()
      apiSetUserVideoModel(n.id, n.labelNames, -0.5)
    }, 10)
  }
}
watch(() => props.videoList, () => {
  if (props.videoList && props.videoList.length > 0) {
    similarList.value = props.videoList
  }
}, {
  immediate: true,
  deep: true
})
watch(currentVideo, playVideo)


</script>   