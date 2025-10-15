<template>
  <div id="ViewQuestionView">
    <a-row :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card v-if="question" :title="question.title">
              <a-descriptions title="题目限制" :column="{ xs: 1, md: 2, lg: 3 }">
                <a-descriptions-item label="时间限制"> {{ question.judgeConfig?.timeLimit }}ms </a-descriptions-item>
                <a-descriptions-item label="内存限制">{{ question.judgeConfig?.memoryLimit }}MB</a-descriptions-item>
                <a-descriptions-item label="堆栈限制">{{ question.judgeConfig?.stackLimit }}MB</a-descriptions-item>
              </a-descriptions>
              <MdViewer :value="question.content" />
              <template #extra>
                <a-space wrap>
                  <a-tag v-for="(tag, index) of question.tags" :key="index" color="green">{{ tag }}</a-tag>
                </a-space>
              </template>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="comment" title="评论"> 评论区 </a-tab-pane>
          <a-tab-pane key="answer" title="题解">{{ question.answer }}</a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :md="12" :xs="24">
        <a-form :model="form" layout="inline">
          <a-form-item field="title" label="编程语言" style="min-width: 240px">
            <a-select v-model="form.language" :style="{ width: '320px' }" placeholder="选择编程语言">
              <a-option v-for="language in selectLanguage" :key="language" :value="language" :label="language" />
            </a-select>
            {{ form.language }}
          </a-form-item>
        </a-form>
        <a-divider size="0" />
        <CodeEditor :value="form.code" :language="form.language" :handle-change="onCodeChange" />
        <a-divider size="0" />
        <a-button type="primary" style="min-width: 200px" @click="doSubmit">提交代码</a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect, withDefaults, defineProps } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionSubmitControllerService,
  QuestionVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import CodeEditor from "@/components/CodeEditor.vue";
import MdViewer from "@/components/MdViewer.vue";

interface Props {
  id: number;
}
const props = withDefaults(defineProps<Props>(), {
  id: () => 0,
});

const question = reactive({} as QuestionVO);

const loadData = async () => {
  const resp = await QuestionControllerService.getQuestionVoByIdUsingGet(props.id);
  if (resp.code === 0) {
    Object.assign(question, resp.data);
  } else {
    message.error("加载失败：" + resp.message);
    // console.log("error");
  }
};

const selectLanguage = ref(["cpp", "java", "python", "go"]);

const form = reactive({
  language: "cpp",
  code: "",
  questionId: props.id,
} as QuestionSubmitAddRequest);

/**
 * 提交代码
 */
const doSubmit = async () => {
  console.log(form);
  const resp = await QuestionSubmitControllerService.doQuestionSubmitUsingPost(form);
  if (resp.code === 0) {
    message.success("提交成功");
  } else {
    message.error("提交失败" + resp.message);
  }
};

const onCodeChange = (v: string) => {
  form.code = v;
  console.log(v);
};

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});
</script>

<style>
#ViewQuestionView {
  max-width: 1400px;
  margin: 0 auto;
}

#viewQuestionView .arco-space-horizational .arco-space-item {
  margin-bottom: 0 !important;
}
</style>
