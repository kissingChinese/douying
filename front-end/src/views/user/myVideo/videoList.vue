<template>
    <v-card elevation="0">
        <v-list lines="three" class="mr-2 ml-2">
            <template v-for="(item) in videoList">
                <v-list-item class="pa-0" max-height="110px" style="overflow: hidden;">
                    <template v-slot:prepend>
                        <v-img width="200" height="100" class="mr-4" :src="apiFileGet(item.cover)" cover @click="playVideo(item)">
                            
                        </v-img>
                        <v-chip class="ma-2" color="warning" :variant="'flat'" v-if="item.auditStatus == 2">{{ item.msg
                            }}</v-chip>
                            <v-chip style="position: absolute; left: 0;top: 0;" class="ma-2" color="warning" :variant="'flat'"
                                v-else-if="item.auditStatus > 0">审核中</v-chip>
                    </template>

                    <v-list-item-title class="font-weight-bold" v-text="item.title"></v-list-item-title>

                    <div v-text="item.description" style="line-height: 25px;overflow: hidden;"></div>
                    <v-chip-group>
                        <v-chip v-for="item in item.labelNames.split(',')">
                            {{ item }}
                        </v-chip>
                    </v-chip-group>
                    <template #append>
                        <v-btn-group :variant="'outlined'">
                            <v-btn color="blue" @click="edit(item)">编辑</v-btn>
                            <v-btn color="red" @click="videoInfo = item; dialog = true">删除</v-btn>
                        </v-btn-group>
                    </template>
                </v-list-item>
                <v-divider class="ma-2" />
                <div class="mt-2 mb-2"></div>
            </template>
        </v-list>
        <v-pagination v-if="pageInfo.pages>1"
              v-model="pageInfo.page"
              :length="pageInfo.pages"
            ></v-pagination>
        <v-dialog v-model="dialog" persistent width="auto">
            <v-card>
                <v-card-title class="text-h5">
                    确定要删除该视频？
                </v-card-title>
                <v-card-text>视频名 {{ videoInfo.title }}.</v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="green-darken-1" variant="text" @click="removeVideo()">
                        确定
                    </v-btn>
                    <v-btn color="green-darken-1" variant="text" @click="dialog = false">
                        不确定
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <v-snackbar v-model="snackbar.show" :color="snackbar.color">
            {{ snackbar.text }}

            <template v-slot:actions>
                <v-btn color="blue" variant="text" @click="snackbar.show = false">
                    了解
                </v-btn>
            </template>
        </v-snackbar>
        <v-dialog :model-value="videoDialog" fullscreen transition="dialog-bottom-transition">
            <v-card v-if="currentVideo">
                <Video :video-info="currentVideo" :close-video="() => playVideo(null)" />
            </v-card>
        </v-dialog>
        <v-dialog v-model="editDialog" max-width="500">
            <v-card class="" title="信息编辑">
                <v-divider />
                <VideoEdit :current-video="videoInfo" class="ma-4" :clear="clear" :save="saveEdit" />
            </v-card>
        </v-dialog>
    </v-card>
</template>
<script setup>
import { onMounted, onUpdated, ref, watch } from 'vue';
import { apiFileGet } from '../../../apis/file';
import { apiRemoveVideo } from '../../../apis/user/videoManger';
import { apiGetVideoByUser } from '../../../apis/video';
import Video from '../../../components/video/index.vue';
import VideoEdit from './edit.vue';
const pageInfo = ref({
    page: 1,
    pages: 0
})
const videoList = ref([])
const dialog = ref(false)
const editDialog = ref(false)
const videoInfo = ref({})

const snackbar = ref({
    show: false,
    text: ""
})
const getVideo = () => {
    apiGetVideoByUser(pageInfo.value.page).then(({ data }) => {
        if (!data.state) {
            return;
        }
        videoList.value = data.data.records
        pageInfo.value.pages = data.data.pages
    })
}
onUpdated(()=>{
    getVideo()
})
onMounted(() => {
    getVideo()
})
const edit = (info) => {
    let temp = Object.assign({}, info)
    temp.labelNames = temp.labelNames.split(",")
    videoInfo.value = temp
    editDialog.value = info ? true : false
}
const removeVideo = () => {
    apiRemoveVideo(videoInfo.value.id).then(({ data }) => {
        snackbar.value = {
            text: data.message,
            show: true,
            color: data.state ? "success" : "error"
        }
        dialog.value = false
        videoInfo.value = {}
        if (data.state) {
            getVideo()
        }
    })
}
const saveEdit = (data) => {
    snackbar.value = {
        text: data.message,
        show: true
    }
    if (data.state) {
        clear()
        getVideo()
    }
}
const clear = () => {
    videoInfo.value = {}
    editDialog.value = false
}
const currentVideo = ref(null)
const videoDialog = ref(false)
const playVideo = (video) => {
    videoDialog.value = false
    currentVideo.value = video
    videoDialog.value = video ? true : false
}
watch(()=>pageInfo.value.page, ()=>{
    getVideo()
}, {immediate:true})
</script>