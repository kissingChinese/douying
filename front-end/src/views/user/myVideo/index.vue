<template>
    <v-card >
        <v-tabs v-model="tab" color="#7bbfea">
            <v-tab v-for="item in items" :key="item">
                {{ item }}
            </v-tab>
        </v-tabs>
        <v-divider/>
        <v-window v-model="tab" >
            <!-- 发布稿件(视频) -->
            <v-window-item value="0">
                <UploadVideo></UploadVideo>
            </v-window-item>
            <!-- 管理稿件(视频) -->
            <v-window-item value="1">
                <videoList></videoList>
            </v-window-item>
        </v-window>
    </v-card>
</template>
<script setup>
import { ref } from 'vue';
import UploadVideo from './upVideo.vue';
import videoList from './videoList.vue';
const tab = ref(0)
const items = ref([
    '上传稿件', '稿件管理'
])
</script>