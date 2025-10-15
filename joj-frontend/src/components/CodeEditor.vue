<template>
  <div id="code-editor" ref="codeEditorRef" style="min-height: 600px" />
  <!--  {{ value }}-->
  <!--  <a-button @click="fillValue">填充内容</a-button>-->
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import { onMounted, ref, toRaw, defineProps, withDefaults, watch } from "vue";

/**
 * 定义组件类型
 */
interface Props {
  value: string;
  language: string;
  handleChange: (value: string) => void;
}

/**
 * 给组件指定初始值
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  language: "cpp",
  handleChange: (value: string) => {
    console.log(value);
  },
});

const codeEditorRef = ref();
const codeEditor = ref();

// const fillValue = () => {
//   if (!codeEditor.value) {
//     return;
//   }
//   // 改变值
//   toRaw(codeEditor.value).setValue("new value");
// };

// watch(
//   () => props.language,
//   () => {
//     alert(props.language);
// codeEditor.value = monaco.editor.create(codeEditorRef.value, {
//   value: props.language,
//   language: props.language,
//   automaticLayout: true,
//   minimap: {
//     enabled: true,
//     scale: 5,
//   },
//   // lineNumbers: "off",
//   // roundedSelection: false,
//   // scrollBeyondLastLine: false,
//   readOnly: false,
//   theme: "vs-dark",
// });
//   }
// );

onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value,
    language: "cpp",
    automaticLayout: true,
    minimap: {
      enabled: true,
      scale: 5,
    },
    // lineNumbers: "off",
    // roundedSelection: false,
    // scrollBeyondLastLine: false,
    readOnly: false,
    theme: "vs-dark",
  });

  // 编辑 监听内容变化
  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue());
  });
});
</script>

<style scoped></style>
