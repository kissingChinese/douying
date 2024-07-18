<template>
  <v-card>
    <v-card-title inset class="float-left">粉丝</v-card-title>
    <div class="float-none"></div>
    <v-tabs v-model="currentType" align-tabs="end" @update:model-value="getLike">
      <v-tab value="follows">关注</v-tab>
      <v-tab value="fans">粉丝</v-tab>
    </v-tabs>
    <v-divider />
    <v-list lines="two">
      <template v-for="item in currentItems">
        <v-list-item :to="`/user?lookId=${item.id}`" :title="item.nickName" :subtitle="item.description || '这个人很懒，没有任何描述'">
          <template v-slot:prepend>
            <v-avatar :image="item.avatar?apiFileGet(item.avatar): '/logo.png'" :color="item.sex ? 'blue' : 'pink'">
              <v-icon color="white">{{ item.sex ? 'mdi-human-male' : 'mdi-human-female' }}</v-icon>
            </v-avatar>
          </template>

          <template v-slot:append>
            <v-btn @click.stop color="grey-lighten-1" variant="text" @click="unLikeOrLike(item.id)">{{ currentType == 'fans' ? item.each?'取消互关':'互相关注' : '取消关注' }}</v-btn>
          </template>
        </v-list-item>
        <v-divider />
      </template>

    </v-list>
    
    <VCard v-if="currentItems.length == 0" height="300px" class="ma-4" :variant="'tonal'"
      style="text-align: center;line-height: 300px;">
      好像没有什么内容呢
    </VCard>
    <v-pagination v-else-if="pageInfo.pages>1"
              v-model="pageInfo.page"
              :length="pageInfo.pages"
            ></v-pagination>
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
import { ref, watch } from 'vue';
import { apiFileGet } from '../../../apis/file';
import { apiFollows, apiGetLike } from '../../../apis/user/like';
import { useUserStore } from '../../../stores';
const currentType = ref("fans")
const currentItems = ref([])
const snackbar = ref({
  show: false,
  text: ""
})
const pageInfo = ref({
    page: 1,
    pages: 0
})
const userStore = useUserStore()
/**
 * 获取关注/粉丝
 */
const getLike = () => {
  currentItems.value = []
  apiGetLike(currentType.value, userStore.lookId, pageInfo.value.page).then(({ data }) => {
    if (!data.state) {
      return;
    }
    currentItems.value = data.data.records
  })
}

const unLikeOrLike = (id) => {
  apiFollows(id).then(({ data }) => {
    snackbar.value = {
      text: data.message,
      show: true
    }
    if (!data.state) {
      return;
    }
    getLike()
  })
}
watch(()=>pageInfo.value.page, ()=>{
    getLike()
}, {immediate:true})

</script>