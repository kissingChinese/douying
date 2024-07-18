<template>
    <div>
        <VCardTitle style="display: inline;">订阅分类</VCardTitle>
        <VCardSubtitle style="display: inline;">您所喜欢的分类</VCardSubtitle>
        <VDivider />
        <v-chip-group class="ma-2">
            <v-chip v-for="(item, index) in classifyData" :key="index" :size="'large'" append-icon="mdi-close"
                @click="closeEvent(index)">
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