<template>
  <div id="exampleView">
    <MdEditor :value="codeValue" :handle-change="onCodeChange" />
    <CodeEditor :value="mdValue" :handle-change="onMdChange" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import CodeEditor from "@/components/CodeEditor.vue"; // @ is an alias to /src

const codeValue = ref();
const mdValue = ref();

const onCodeChange = (v: string) => {
  codeValue.value = v;
  console.log(v);
};
const onMdChange = (v: string) => {
  mdValue.value = v;
  console.log(v);
};
</script>
