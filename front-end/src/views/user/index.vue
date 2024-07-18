<template>
    <v-container :fluid="true" style="height: 500px;">
        <v-card>
            <v-img :height="220" aspect-ratio="16/9" cover src="../../../public/background-img.jpg">
                <v-list
                    style="position: absolute;left: 0;bottom: 0; width: 100%; background-color: rgba(1,1,1,0.5); color: white;">
                    <v-list-item :title="userInfo.nickName" :subtitle="userInfo.description">
                        <template #prepend>
                            <v-avatar :image="avatarImg" size="50" />
                        </template>
                    </v-list-item>
                </v-list>
            </v-img>
            <v-tabs v-model="tab" color="#7bbfea">
                <v-tab value="aa" :to="`/user/home${isSelf ? '' : '?lookId=' + userInfo.id}`">主页</v-tab>
                <v-tab value="one" to="/user/video" v-if="isSelf">创作中心</v-tab>
                <v-tab value="two" to="/user/favorites" v-if="isSelf">收藏夹</v-tab>
                <v-tab value="3" to="/user/history" v-if="isSelf">历史记录</v-tab>
                <v-tab value="two3" to="/user/classify" v-if="isSelf">订阅分类</v-tab>
                <v-tab value="4" :to="`/user/like${isSelf ? '' : '?lookId=' + userInfo.id}`">关注/粉丝</v-tab>
                <v-spacer></v-spacer>
                <v-btn class="ma-2" variant="text" @click="editDialog = !editDialog" v-if="isSelf">编辑信息</v-btn>
            </v-tabs>
        </v-card>
        <router-view class="mt-2" />
        <v-dialog v-model="editDialog" max-width="400">
            <v-card title="编辑用户信息">
                <v-divider />
                <v-card-text>
                    <v-form>
                        <v-hover v-slot="{ isHovering, props }">
                            <v-card v-bind="props" class="mb-2 mx-auto" height="70px" width="70px" :rounded="10"
                                @click="avatarFileRef.click()">
                                <v-img :rounded="10" :src="avatarImg" />
                                <v-overlay :model-value="isHovering == true || uploading > -1" contained scrim="black"
                                    class="align-center justify-center">
                                    <v-icon v-if="uploading == -1">mdi-upload</v-icon>
                                    <v-progress-circular v-else :model-value="uploading"></v-progress-circular>
                                </v-overlay>
                            </v-card>
                        </v-hover>

                        <input hidden v-on:change="uploadAvatar" ref="avatarFileRef" type="file" accept="image/*" />
                        <v-text-field v-model="userInfo.nickName" label="昵称" placeholder="请输入昵称"></v-text-field>
                        <v-text-field v-model="userInfo.description" label="描述" placeholder="请输入描述"></v-text-field>
                        <v-switch v-model="userInfo.sex" :label="userInfo.sex ? '男生' : '女生'" inset color="blue" />
                    </v-form>
                </v-card-text>
                <v-divider />
                <v-card-actions>
                    <v-btn text="取消" color="warning" @click="editDialog = false" />
                    <v-spacer />
                    <v-btn text="保存" color="success" @click="saveInfo()" />
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
    </v-container>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { apiFileGet, apiUploadFile } from '../../apis/file';
import { apiChangeUserInfo, apiGetUserInfo } from '../../apis/user/user';
import router from '../../router';
import { useUserStore } from '../../stores';
const tab = ref()
const userStore = useUserStore()
const route = useRoute()
const userInfo = ref({})
const editDialog = ref(false)
const avatarFileRef = ref()
const uploading = ref(-1)
const snackbar = ref({
    show: false,
    text: ""
})
const isSelf = computed(() => {
    if (!userInfo.value) return false
    return userStore.$state.info.id == userInfo.value.id
})
const avatarImg = computed(() => {
    if (!userInfo.value) {
        getUserInfo()
        return "/logo.png"
    }
    if (avatarFileRef.value && avatarFileRef.value.files && avatarFileRef.value.files[0]) {
        var URL = window.URL || window.webkitURL;
        return URL.createObjectURL(avatarFileRef.value.files[0])
    }
    if (userInfo.value == null) {
        return "/logo.png"
    }
    if (userInfo.value.avatar == null) return "/logo.png"
    
    return apiFileGet(userInfo.value.avatar)
})
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
        }, complete: (_e, fileId) => {
            uploading.value = -1
            if (!fileId.state) {
                snackbar.value = {
                    text: fileId.message,
                    show: true
                }
                return;
            }

            userInfo.value.avatar = fileId.data
            snackbar.value = {
                text: "上传完成",
                show: true,
                color: "success"
            }
        }
    })
}
const getUserInfo = () => {
    if (route.query && route.query.lookId) {
        userStore.$patch({
            lookId: route.query.lookId
        })
    } else {
        userStore.$patch({
            lookId: userStore.$state.info.id
        })
    }
    apiGetUserInfo(userStore.$state.lookId).then(({ data }) => {
        if (data.state) {
            userInfo.value = data.data
        }
    })
}
const saveInfo = () => {
    apiChangeUserInfo(userInfo.value).then(({ data }) => {
        snackbar.value = {
            text: data.message,
            show: true,
            color: data.state ? "success" : "error"
        }
        if (!data.state) {
            return;
        }
        editDialog.value = false
        apiGetUserInfo(userInfo.value.id).then(({ data }) => {
            if (data.state) {
                userStore.$patch({
                    info: data.data
                })
            }
        })
    })
}
watch(() => route.query, () => {
    getUserInfo()

}, {
    immediate: true
})
onMounted(() => {
    if (!userStore.$state.token) {
        router.push({ path: "/" })
    }

    userStore.$patch({
        lookId: route.query.lookId || userStore.info.id
    })
    getUserInfo()
})
</script>

<style scoped></style>