<template>
    <v-card title="我的视频" elevation="0">
        <v-divider/>
        <VideoListVue :video-list="videoList"/>
        <v-pagination
        v-if="pageInfo.pages>1"
              v-model="pageInfo.page"
              :length="pageInfo.pages"
            ></v-pagination>
    </v-card>
</template>
<script setup>
import { ref, watch } from 'vue';
import { apiGetUserVideoById } from '../../../apis/video';
import VideoListVue from '../../../components/video/list.vue';
import { useUserStore } from '../../../stores';
const userStore = useUserStore()
const videoList = ref([])
const pageInfo = ref({
    page: 1,
    pages: 0
})
const getVideo = ()=>{
    apiGetUserVideoById(userStore.$state.lookId, pageInfo.value.page).then(({data})=>{
        if(!data.state){
            return
        }
        pageInfo.value.pages = data.data.pages
        videoList.value = data.data.records
    })
}
watch(()=>pageInfo.value.page, ()=>{
    getVideo()
}, {immediate:true})
</script>