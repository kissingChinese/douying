<template>
    <v-card v-show="true">
        <v-list :density="'compact'" lines="one" height="350px" >
            <v-list-subheader>热度视频 排行榜</v-list-subheader>
            <v-list-item v-for="(rank, n) in rankList" :title="rank.title" :to="`/?play=${rank.videoId}&n=${n}`" :active="false">
                <template #prepend>
                    <v-icon size="30">{{ `mdi-numeric-${n + 1}` }}</v-icon>
                </template>
                <template #subtitle>
                    {{ `${rank.hotFormat} 热度`}}
                </template>
            </v-list-item>
        </v-list>
    </v-card>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { apiVideoHotRank } from '../apis/video';

const rankList = ref([])
onMounted(() => {
    apiVideoHotRank().then(({ data }) => {
        rankList.value = data.data.splice(0, 10)
    })
})
</script>