<template>
    <v-card color="background" elevation="0" class="pb-2"  v-show="true">
        <v-row dense class="mb-2">
            <v-divider class="ma-2" />

            <v-col cols="9" v-if="!videoList || videoList.length == 0">
                <VCard height="600px" class="ma-4" :variant="'tonal'" style="text-align: center;line-height: 300px;">
                    {{ noDataMsg }}
                </VCard>
            </v-col>

            <v-col v-if="showHot">
                <v-card elevation="5">
                    <v-list :density="'compact'" lines="one" height="600px" style="background-color:gray">
                        <v-list-subheader>热度视频 排行榜</v-list-subheader>
                    <v-list-item v-for="(rank, n) in rankList" :title="rank.title" :to="`/?play=${rank.videoId}&n=${n}`" :active="false">
                        <template #prepend>
                            <v-icon size="30">{{ `mdi-numeric-${n+1}` }}</v-icon>
                        </template>
                    <template #subtitle>
                        {{ `${rank.hotFormat} 热度` }}
                    </template>
                    </v-list-item>
                    </v-list>
                </v-card>
            </v-col>
            
            <v-col v-for="(video, index) in videoList" :key="index" xs="12" sm="6" md="4" lg="3" xl="2">
                <VideoCard @click="playVideo(video)" :video-info="video" />
            </v-col>
        </v-row>
        <v-dialog :model-value="videoDialog" fullscreen transition="dialog-bottom-transition">
            <v-card v-if="currentVideo">
                <Video :video-info="currentVideo" :close-video="() => playVideo(null)" />
            </v-card>
        </v-dialog>
    </v-card>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { apiVideoHotRank } from '../../apis/video';
import VideoCard from './card.vue';
import Video from './index.vue';
const { videoList, noDataMsg, showHot } = defineProps({
    videoList: {
        type: Object,
        default: []
    },
    noDataMsg: {
        type: String,
        default: "未找到相关视频"
    },
    showHot: {
        type: Boolean,
        default: false
    }
})
const rankList = ref([])
onMounted(() => {
    if (showHot) {
        apiVideoHotRank().then(({ data }) => {
            rankList.value = data.data.splice(0,10)
        })
    }
})
const currentVideo = ref(null)
const videoDialog = ref(false)
const playVideo = (video) => {
    videoDialog.value = false
    currentVideo.value = video
    videoDialog.value = video ? true : false
}
</script>