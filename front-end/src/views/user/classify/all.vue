<template>
    <div>
        <VCardTitle style="display: inline;">全部分类</VCardTitle>
        <VCardSubtitle style="display: inline;">所有视频分类</VCardSubtitle>
        <VDivider />
        <v-chip-group class="ma-2">
            <v-chip v-for="(item, index) in classifyData" :key="index" :size="'large'"
                @click="closeEvent(item, true)" append-icon="mdi-plus-circle">
                <template #prepend>
                    <VAvatar :image="item.image" :icon="item.icon || 'mdi-file-document-alert-outline'" start></VAvatar>
                </template>
                {{ item.name }}
            </v-chip>
        </v-chip-group>
    </div>
</template>
<script setup>

const props = defineProps({
    classifyData: {
        type: Array,
        default: []
    },
    closeEvent: {
        type: Function,
        default: () => { }
    }
})
</script>