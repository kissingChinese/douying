<template>
    <!-- 上传视频 -->
    <v-card>
        <VCard>
            <VRow>
                <VCol cols="6">
                    <VCard border="dashed" min-height="'300px'" class="ma-4" :variant="'outlined'"
                        style="text-align: center;">
                        <VList density="compact" style="margin: 0 auto;">
                            <VListItem>
                                <VIcon color="blue" :size="50">mdi-upload</VIcon>
                            </VListItem>
                            <VListItem>
                                <VCardSubtitle>点击上传视频</VCardSubtitle>
                            </VListItem>
                            <VListItem>
                                <VBtn color="primary" @click="videoFileRef.click()">上传视频</VBtn>
                            </VListItem>
                            <VListItem>
                                <v-chip class="ma-2 font-weight-bold" color="green" label>
                                    <v-icon start :icon="'mdi-file-cloud-outline'"></v-icon>
                                    审核队列: {{ queueState }}
                                </v-chip>
                            </VListItem>
                        </VList>
                        <div style="display: none;">
                            <form>
                                <input v-on:change="uploadVideo" ref="videoFileRef" type="file" accept="video/*" />
                            </form>
                        </div>
                    </VCard>
                </VCol>
                <VCol cols="6">
                    <v-list :density="'compact'" lines="three" class="mt-2">
                        <VListItemTitle>
                            <VIcon>mdi-menu-down</VIcon>用户提醒
                        </VListItemTitle>
                        <v-list-item v-for="(item, i) in items" :key="i" link>
                            <template v-slot:prepend>
                                <v-avatar class="me-4 mt-2" rounded="0">
                                    <v-img :src="item.image" cover></v-img>
                                </v-avatar>
                            </template>

                            <v-list-item-title class="text-uppercase font-weight-regular text-caption"
                                v-text="item.category"></v-list-item-title>
                            <div v-text="item.title"></div>
                        </v-list-item>
                    </v-list>
                </VCol>
            </VRow>
        </VCard>
        <VCard v-if="uploadList.length > 0">
            <v-item-group mandatory>
                <VCardTitle>基本信息</VCardTitle>
                <VDivider></VDivider>
                <v-container>
                    <v-row>
                        <v-col v-for="(uploadItem, index) in uploadList" :key="index" cols="12" md="3">
                            <v-item v-slot="{ isSelected, toggle }">
                                <v-card :color="isSelected ? 'primary' : 'white'" dark height="100"
                                    @click="() => { toggle(); currentVideoIndex = index }">
                                    <v-scroll-y-transition>
                                        <div>
                                            <v-card-title>{{ uploadItem.title }}</v-card-title>
                                            <VCardText class="pb-0">
                                                <v-progress-linear color="orange" striped :model-value="uploadItem.progress"
                                                    height="25">
                                                    <strong>{{ Math.ceil(uploadItem.progress) }}%</strong>
                                                </v-progress-linear>
                                            </VCardText>
                                        </div>
                                    </v-scroll-y-transition>
                                </v-card>
                            </v-item>
                        </v-col>
                    </v-row>
                </v-container>
            </v-item-group>
            <v-card-text v-if="currentVideo">
                <VideoEdit :current-video="currentVideo" :clear="clearUp" :save="pushVideo" />
            </v-card-text>
        </VCard>
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
import { computed, onMounted, ref } from 'vue';
import { apiUploadFile } from '../../../apis/file';
import { apiGetAuditQueueState } from '../../../apis/video';
import VideoEdit from './edit.vue';
const snackbar = ref({
    show: false,
    text: ""
})
const items = ref([
    {
        image: 'https://cdn.vuetifyjs.com/docs/images/chips/globe.png',
        title: '视频格式需要满足:mp4、avi、aav、cawd',
        category: '视频',

    },
    {
        image: 'https://cdn.vuetifyjs.com/docs/images/chips/cpu.png',
        title: '七牛云自动审核机制',
        category: '审核',
    },
    {
        image: 'https://cdn.vuetifyjs.com/docs/images/chips/rocket.png',
        title: '七牛云的oss云存储服务',
        category: '存储',
    }
])
const currentVideoIndex = ref(null)
const videoFileRef = ref()
const uploadList = ref([])
const queueState = ref("快速")
const currentVideo = computed(() => currentVideoIndex.value > -1 ? uploadList.value[currentVideoIndex.value] : null)
onMounted(() => {
    getQueueState()
})
const getQueueState = () => {
    apiGetAuditQueueState().then(({ data }) => {
        if (!data.state) {
            return
        }
        queueState.value = data.message
    })
}
const clearUp = () => {
    if (currentVideo.value.status < 1) {
        currentVideo.value.subscription.unsubscribe()
    }
    uploadList.value.splice(currentVideoIndex.value, 1);
}
const pushVideo = (data) => {
    snackbar.value = {
        text: data.message,
        show: true
    }
    if(data.state) {
        clearUp();
        getQueueState()
    }
    
}
const uploadVideo = async () => {
    if(!videoFileRef.value.files[0]) return;
    let curFile = videoFileRef.value.files[0]
    const curUploadInfo = {
        progress: 0,
        status: 0,
        msg: "",
        result: "",
        title: curFile.name,
        description: "",
        url: "",
        cover: "",
        file: curFile,
        labelNames: []
    }
    uploadList.value.push(curUploadInfo)
    if(uploadList.value.length==1) {
        currentVideoIndex.value = 0
    }
    curUploadInfo.subscription = await apiUploadFile(curFile, {
        next: (e) => {
            curUploadInfo.progress = e.total.percent
            uploadList.value = Object.assign([], uploadList.value)
        }, error: (e) => {
            curUploadInfo.status = -1
            curUploadInfo.msg = "上传失败：" + e
            uploadList.value = Object.assign([], uploadList.value)
        },
        complete: (e,fileId) => {
            if(!fileId.state) {
                snackbar.value = {
                    text: fileId.message,
                    show: true
                }
                return;
            }
            curUploadInfo.result = e
            curUploadInfo.status = 1
            curUploadInfo.url = fileId.data
            curUploadInfo.cover = null
            uploadList.value = Object.assign([], uploadList.value)
        }
    })
    videoFileRef.value.value = ""
}
</script>