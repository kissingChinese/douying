<template>
    <v-card ref="cardRef" v-if="props.videoInfo"  hover ripple :elevation="0" rounded="lg"> 
        <v-img :src="props.videoInfo.cover?apiFileGet(props.videoInfo.cover):'/not-found.png'" class="align-end"
            gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)" height="300px" cover>
            <!-- <v-avatar :image="props.videoInfo.user.head||'/logo.png'" style="position:absolute;top:10px;right:10px"></v-avatar> -->
            <v-card-text class="text-white pa-0" v-if="!overlay">
                <v-card-actions class="ml-1 mr-1 pa-0">
                    <span class="ma-2 font-weight-bold" text-color="white" prepend-icon="mdi-heart">
                        <v-icon>mdi-heart</v-icon>
                        {{ props.videoInfo.startCount }} 点赞
                    </span>
                    <v-spacer />
                    <v-btn :variant="'tonal'" :density="'comfortable'">{{ props.videoInfo.duration }}</v-btn>
                </v-card-actions>
            </v-card-text>
        </v-img>
        <v-card-actions>
            <!-- <v-card-title>{{ video.title }}</v-card-title> -->
            <span style="max-height: 20px;color: white;" class="ml-1 overflow-hidden">{{ props.videoInfo.title }}</span>
            <!-- <v-card-subtitle>{{ props.videoInfo.userName || "无" }}</v-card-subtitle> -->
            <v-spacer></v-spacer>
            <v-btn size="small" color="white" variant="tonal" v-if="props.videoInfo.user" @click.stop :to="userStore.token?`/user?lookId=${props.videoInfo.user.id}`:''">@{{ props.videoInfo.user.nickName
            }}</v-btn>
        </v-card-actions>
        <v-overlay scrim="black" :model-value="overlay" contained persistent width="100%" style="top:0px">
            <v-card color="rgba(1,1,1,0.5)" height="350px">
                <v-card-title class="pb-0">播放中 </v-card-title>
                <v-chip-group class="ml-2 mr-2">
                    <v-chip v-for="item in props.videoInfo.labelNames.split(',')">{{ item }}</v-chip>
                </v-chip-group>
                <v-card-subtitle>
                    <v-row>
                        <v-col>
                            {{ props.videoInfo.historyCount }} 播放
                        </v-col>
                        <v-col>
                            {{ props.videoInfo.startCount }} 点赞
                        </v-col>
                        <v-col>
                            {{ props.videoInfo.favoritesCount||0 }} 收藏
                        </v-col>
                    </v-row>
                </v-card-subtitle>

                <v-card-actions class="pb-0 pt-0">
                    <v-btn color="orange-lighten-2" variant="text">
                        描述
                    </v-btn>

                    <v-spacer></v-spacer>

                    <v-btn :icon="showDescription ? 'mdi-chevron-up' : 'mdi-chevron-down'"
                        @click="showDescription = !showDescription"></v-btn>
                </v-card-actions>

                <v-expand-transition>
                    <div v-show="showDescription">
                        <v-divider></v-divider>

                        <v-card-text>
                            {{ props.videoInfo.description || "作者没有添加描述信息" }}
                        </v-card-text>
                        <v-card-actions>
                            <v-chip :density="'compact'" @click="copyUrl()">YV: {{ props.videoInfo.yv }}</v-chip>
                        </v-card-actions>
                    </div>
                </v-expand-transition>
            </v-card>
        </v-overlay>
        <v-snackbar v-model="snackbar.show" :color="snackbar.color">
        {{ snackbar.text }}

        <template v-slot:actions>
          <v-btn color="blue" variant="text" @click="snackbar.show = false">
            了解
          </v-btn>
        </template>
      </v-snackbar>
    </v-card>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { apiFileGet } from '../../apis/file';
import { useUserStore } from '../../stores';
import strUtils from '../../utils/strUtil';
const showDescription = ref(false)
const userStore = useUserStore()
const props = defineProps({
    videoInfo: {
        type: Object,
        default: null
    },
    overlay: {
        type: Boolean,
        default: false
    }
})
const snackbar = ref({
  show: false,
  text: ""
})
const copyUrl = () => {

snackbar.value = {
  text: strUtils.copyContent(props.videoInfo.yv) ? "视频YV号复制成功" : "视频YV号复制失败",
  show: true
}
}
const cardRef = ref()
onMounted(() => {
    showDescription.value = props.overlay
})
watch(() => props.overlay, (e) => {
    showDescription.value = e
    if(e) {
        cardRef.value.$el.scrollIntoView({ behavior: 'smooth', block: 'center' })
    }
})
</script>
