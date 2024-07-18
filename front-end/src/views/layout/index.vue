<template>
  <v-app class="rounded rounded-md">
    <defaultHeader :click-event="clickEvent" />
    <navigation v-model="leftMenu"/>
    <v-main class="align-center justify-center" style="min-height: 100%;">
      <router-view />
    </v-main>
<!-- 
    <v-footer app name="footer">
    </v-footer> -->
  </v-app>
</template>
<script setup>
import { ref } from 'vue';
import defaultHeader from './header.vue';
import navigation from './navigation.vue';
const leftMenu = ref(true)
const clickEvent = (type,data)=>{
  switch(type) {
    case 1:
      // 左侧导航的展开与隐藏按钮
      leftMenu.value = !leftMenu.value
  }
}
</script>