<template>
  <div id="addQuestionView">
    <h2>创建题目</h2>
    <a-form :model="form" label-align="left">
      <a-form-item field="title" label="标题">
        <a-input v-model="form.title" placeholder="请输入标题" />
      </a-form-item>

      <a-form-item label="题目限制" :content-flex="false" :merge-props="false">
        <a-space direction="vertical" style="min-width: 480px">
          <a-form-item field="judgeConfig.timeLimit" label="时间限制">
            <a-input-number
              v-model="form.judgeConfig.timeLimit"
              placeholder="请输入时间限制"
              mode="button"
              min="0"
              size="large"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
            <a-input-number
              v-model="form.judgeConfig.memoryLimit"
              placeholder="请输入内存限制"
              mode="button"
              min="0"
              size="large"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
            <a-input-number
              v-model="form.judgeConfig.stackLimit"
              placeholder="请输入堆栈限制"
              mode="button"
              min="0"
              size="large"
            />
          </a-form-item>
        </a-space>
      </a-form-item>

      <a-form-item field="content" label="题目内容">
        <MdEditor :value="form.content" :handle-change="onContentChange" />
      </a-form-item>

      <a-form-item label="题目样例" :content-flex="false" :merge-props="false">
        <a-space direction="vertical" style="min-width: 640px">
          <a-form-item v-for="(judgeCaseItem, index) of form.judgeCase" :key="index" no-style>
            <a-form-item :field="`form.judgeCase[${index}].input`" :label="`input-${index}`" :key="index">
              <a-input v-model="judgeCaseItem.input" placeholder="样例输入" />
            </a-form-item>
            <a-form-item :field="`form.judgeCase[${index}].output`" :label="`output-${index}`" :key="index">
              <a-input v-model="judgeCaseItem.output" placeholder="样例输出" />
            </a-form-item>
            <a-button status="danger" @click="handleDelete(index)">删除</a-button>
          </a-form-item>
        </a-space>
        <div style="margin-top: 32px">
          <a-button @click="handleAdd" type="outline" status="success">新增测试用例</a-button>
        </div>
      </a-form-item>

      <a-form-item field="answer" label="答案">
        <MdEditor :value="form.answer" :handle-change="onAnswerChange" />
      </a-form-item>

      <a-form-item field="tags" label="标签">
        <a-input-tag v-model="form.tags" placeholder="请输入标签" />
      </a-form-item>

      <div style="margin-top: 32px" />

      <a-form-item>
        <a-button type="primary" style="min-width: 200px" @click="doSubmit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import { QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";

const form = reactive({
  title: "",
  content: "",
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  answer: "",
  tags: ["easy", "简单"],
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
});

const doSubmit = async () => {
  console.log(form);
  const resp = await QuestionControllerService.addQuestionUsingPost(form);
  if (resp.code === 0) {
    message.success("创建成功");
  } else {
    message.error("创建失败：", resp.message);
  }
};

/**
 * 新增样例
 */
const handleAdd = () => {
  form.judgeCase.push({
    input: "",
    output: "",
  });
};

/**
 * 删除样例
 */
const handleDelete = (index: number) => {
  form.judgeCase.splice(index, 1);
};

const onContentChange = (value: string) => {
  form.content = value;
};
const onAnswerChange = (value: string) => {
  form.answer = value;
};
</script>

<style scoped>
#addQuestionView {
}
</style>
