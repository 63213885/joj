<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style>
#app {
}
</style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { onMounted } from "vue";

/**
 * 全局初始化函数
 */
const doInit = () => {
  console.log("hello JOJ!");
};

onMounted(() => {
  doInit();
});
</script>
