<template>
    <v-card style="height: 100%;" elevation="2" color="background">
        <Video :video-list="videoList" :hide-close="true" v-if="videoList.length > 0" :next-video="nextVideo" />
    </v-card>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { apiVideoByPush } from '../../apis/video.js';
import Video from '../../components/video/index.vue';
const videoList = ref([])
const nextVideo = (idnex) =>{
    if((videoList.value.length - idnex) <= 3) {
        getVideo()
    }
}

const getVideo = ()=>{
    apiVideoByPush().then(({ data }) => {
        if (!data.state) {
            return;
        }
        videoList.value = videoList.value.concat(data.data)
    })
}
onMounted(() => {
    getVideo()
})
</script>