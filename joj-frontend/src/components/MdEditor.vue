<template>
  <Editor :value="value" :mode="mode" :plugins="plugins" @change="handleChange" />
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, withDefaults, defineProps } from "@bytemd/vue-next";

/**
 * 定义组件类型
 */
interface Props {
  value: string;
  mode?: string;
  handleChange: (value: string) => void;
}

const plugins = [
  gfm(),
  highlight(),
  // Add more plugins here
];

/**
 * 给组件指定初始值
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  mode: "split",
  handleChange: (value: string) => {
    console.log(value);
  },
});
</script>

<style scoped>
:deep(.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child) {
  display: none;
}
</style>
