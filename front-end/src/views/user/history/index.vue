<template>
  <v-card :loading="isload" elevation="0">
    <template v-for="item in items">
      <v-card-title>{{ item.time }}</v-card-title>
      <VideoList :video-list="item.data" />
    </template>
  </v-card>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { apiGetHistoryVideo } from '../../../apis/video';
import VideoList from '../../../components/video/list.vue';

const items = ref([])
const isload = ref(true)
const getHistoryVideos = () => {
  isload.value = true
  apiGetHistoryVideo().then(({ data }) => {
    isload.value = false
    for (let key in data.data) {
      console.log(key)
      items.value.push({
        time: key,
        data: data.data[key]
      })
    }
  })
}
onMounted(getHistoryVideos)

</script>