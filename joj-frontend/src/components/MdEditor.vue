<template>
  <Editor :value="value" :plugins="plugins" @change="handleChange" />
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer, withDefaults, defineProps } from "@bytemd/vue-next";
import { ref } from "vue";

/**
 * 定义组件类型
 */
interface Props {
  value: string;
  handleChange: (value: string) => void;
}

const plugins = [
  gfm(),
  highlight(),
  // Add more plugins here
];

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  handleChange: (value: string) => {
    console.log(value);
  },
});
</script>

<style scoped></style>
