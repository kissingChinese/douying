<template>
    <v-dialog v-model="dialog" width="500">
        <template #activator="{ props }">
            <slot :props="props"></slot>
        </template>
        <template #default="{ isActive }">
            <v-card v-if="currentData" :title="`${title}收藏夹`">
                <v-divider class="mt-2" />
                <v-card-text>
                    <v-text-field label="收藏夹名称" v-model="currentData.name" single-line :variant="'outlined'"></v-text-field>
                    <v-text-field label="收藏夹描述" v-model="currentData.description" single-line :variant="'outlined'"></v-text-field>
                </v-card-text>

                <v-card-actions>
                    <v-btn color="red" text="删除" @click="deleteFavorite()" v-if="props.editData"></v-btn>
                    <v-spacer></v-spacer>
                    <v-btn color="blue" text="保存" @click="saveFavorite()"></v-btn>
                </v-card-actions>
            </v-card>
        </template>
    </v-dialog>
</template>
<script setup>
import { computed, ref, watch } from 'vue';
import { apiRemoveFavorite, apiSaveFavorite } from '../../apis/user/favorites';
const dialog = ref(false)
const props = defineProps({
    editData: {
        type: Object,
        default: null,
    },
    closeEvent: {
        type: Function,
        default: ()=>{}
    }
})
const title = computed(() => props.editData ? "编辑" : "添加")
const currentData = ref(props.editData ? props.editData : { name: "", description: "" })
const saveFavorite = () => {
    apiSaveFavorite(currentData.value).then(({ data }) => {
        if(!data.state) {
            return;
        }
        dialog.value = false
    })
}
const deleteFavorite = ()=>{
    apiRemoveFavorite(currentData.value.id).then(({data})=>{
        if(!data.state) {
            return;
        }
        dialog.value = false;
    })
}
watch(dialog, (e)=>{
    if(!e) {
        props.closeEvent()
    }
})
watch(props, (e)=>{
    
    if(e.editData == null) {
        dialog.value = false
        return;
    }else dialog.value = true
    currentData.value = Object.assign({}, e.editData ? e.editData : { name: "", description: "" })
}, {
    deep: true
})
</script>