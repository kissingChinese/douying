<template>
    <v-dialog v-model="dialog" scrollable :width="300">
        <template v-slot:activator="{ props }">
            <slot :props="props"></slot>
        </template>
        <v-card>
            <v-card-title>选择收藏夹
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text style="height: 300px;">
                <v-radio-group v-model="dialogm1" column>
                    <v-radio :label="item.name" :value="item.id" v-for="item in favoriteItems"></v-radio>
                </v-radio-group>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
                <v-btn color="red" variant="text" @click="dialog = false">
                    取消
                </v-btn>
                <v-spacer />
                <FavoriteEdit :close-event="getFavorites">
                    <template #default="{ props }">
                        <v-btn :variant="'text'" v-bind="props">创建收藏夹</v-btn>
                    </template>

                </FavoriteEdit>
                <v-btn color="primary" variant="text" @click="save()">
                    收藏
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
<script setup>
import { ref, watch } from 'vue';
import { apiFavoriteVideo, apiGetFavorites } from '../../apis/user/favorites';
import FavoriteEdit from './edit.vue';
const dialog = ref(false)
const dialogm1 = ref(0)
const favoriteItems = ref([])
const props = defineProps({
    videoId: {
        type: Number,
        default: null
    },
    callback: {
        type: Function,
        default: () => { }
    }
})
const getFavorites = ()=>{
    apiGetFavorites().then(({ data }) => {
            if (!data.state) {
                dialog.value = false
                return
            }
            favoriteItems.value = data.data
            if(favoriteItems.value.length>0) {
                dialogm1.value = favoriteItems.value[0].id
            }
        })
}
watch(dialog, (newV) => {
    if (newV)
    getFavorites()
})
const save = () => {
    if (props.videoId > 0 && dialogm1.value > 0) {
        apiFavoriteVideo(dialogm1.value, props.videoId).then(({ data }) => {
            if (!data.state) {
                return;
            }
            dialog.value = false
            props.callback(data.message)
        })
    }
}
</script>