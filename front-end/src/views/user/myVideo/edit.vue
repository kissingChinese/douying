<template>
    <div>
        <v-text-field variant="filled" label="视频标题" v-model="currentVideo.title" clearable></v-text-field>

        <v-textarea variant="filled" label="视频描述" :rows="3" v-model="currentVideo.description" clearable></v-textarea>
        <v-hover v-slot="{ isHovering, props }">
            <v-card v-if="!currentVideo.id" v-bind="props" class="mb-2 mx-auto" height="150px" width="150px" :rounded="10"
                @click="avatarFileRef.click()">
                视频封面:
                <v-img :rounded="10" :src="coverImg" />
                <v-overlay :model-value="isHovering == true || uploading > -1" contained scrim="black"
                    class="align-center justify-center">
                    <v-icon v-if="uploading == -1">mdi-upload</v-icon>
                    <v-progress-circular v-else :model-value="uploading"></v-progress-circular>
                </v-overlay>
            </v-card>
        </v-hover>

        <input hidden v-on:change="uploadAvatar" ref="avatarFileRef" type="file" accept="image/*" />
        <v-autocomplete v-model="currentVideo.typeId" :items="allClassifyList" chips closable-chips
            color="blue-grey-lighten-2" item-title="name" item-value="id" label="视频分类" no-data-text="无视频分类">
            <template v-slot:chip="{ props, item }">
                <v-chip v-bind="props" :prepend-icon="item.raw.icon || 'mdi-file-document-alert-outline'"
                    :text="item.raw.name"></v-chip>
            </template>

            <template v-slot:item="{ props, item }">
                <v-list-item v-bind="props" :prepend-icon="item?.raw?.icon || 'mdi-file-document-alert-outline'"
                    :title="item?.raw?.name" :subtitle="item?.raw?.description || '无相关描述信息'"></v-list-item>
            </template>
        </v-autocomplete>
        <v-combobox v-model="currentVideo.labelNames" label="视频标签" multiple chips closable-chips></v-combobox>
        <v-divider></v-divider>

        <v-card-actions>
            <v-btn color="warning" class="font-weight-bold" :variant="'tonal'" @click="clearUp()">取消</v-btn>
            <v-spacer></v-spacer>
            <v-btn color="success" class="font-weight-bold" :variant="'tonal'" @click="pushVideo()">
                发布视频
            </v-btn>
        </v-card-actions>
        <v-snackbar v-model="snackbar.show" :color="snackbar.color">
            {{ snackbar.text }}

            <template v-slot:actions>
                <v-btn color="blue" variant="text" @click="snackbar.show = false">
                    了解
                </v-btn>
            </template>
        </v-snackbar>
    </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { apiClassifyGetAll } from '../../../apis/classify';
import { apiFileGet, apiUploadFile } from '../../../apis/file';
import { apiVideoPush } from '../../../apis/user/videoManger';
const avatarFileRef = ref()
const uploading = ref(-1)
const allClassifyList = ref([])
const { currentVideo, save, clear } = defineProps({
    currentVideo: {
        type: Object,
        default: {}
    },
    clear: {
        type: Function,
        default: () => { }
    },
    save: {
        type: Function,
        default: () => { }
    }
})
const snackbar = ref({
    show: false,
    text: ""
})
const clearUp = () => {
    clear()
}
const uploadAvatar = () => {
    if (!avatarFileRef.value.files[0]) return;
    apiUploadFile(avatarFileRef.value.files[0], {
        next: (e) => {
            uploading.value = e.total.percent
        }, error: () => {
            uploading.value = -1
            snackbar.value = {
                text: "上传失败",
                show: true,
                color: "error"
            }
        }, complete: (e,fileId) => {
            if(!fileId.state) {
                snackbar.value = {
                    text: fileId.message,
                    show: true
                }
                return;
            }
            uploading.value = -1
            currentVideo.cover = fileId.data
            snackbar.value = {
                text: "上传完成",
                show: true,
                color: "success"
            }
        }
    })
}
const coverImg = computed(() => {
    if (!currentVideo) {
        return "/logo.png"
    }
    if (avatarFileRef.value && avatarFileRef.value.files && avatarFileRef.value.files[0]) {
        var URL = window.URL || window.webkitURL;
        return URL.createObjectURL(avatarFileRef.value.files[0])
    }
    if (currentVideo.cover == null || currentVideo.cover == "") {
        return "/logo.png"
    }
    return apiFileGet(currentVideo.cover)
})
onMounted(() => {
    apiClassifyGetAll().then(({ data }) => {
        if (!data.state) {
            allClassifyList.value = []
            return;
        }
        allClassifyList.value = data.data
    })
})
const pushVideo = () => {
    apiVideoPush(currentVideo).then(({ data }) => {
        save(data)
    })
}
</script>