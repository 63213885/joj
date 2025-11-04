<template>
  <div id="QuestionsView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="title" label="名称" style="min-width: 240px">
        <a-input v-model="searchParams.title" placeholder="请输入名称" />
      </a-form-item>
      <a-form-item field="tags" label="标签" style="min-width: 280px">
        <a-input-tag v-model="searchParams.tags" placeholder="请输入标签" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">提交</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />

    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
      @page-change="onPageChange"
    >
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="green">{{ tag }}</a-tag>
        </a-space>
      </template>
      <template #acceptedRate="{ record }">
        {{
          `${record.submitNum ? record.acceptedNum / record.submitNum : 0}% (${record.acceptedNum}/${record.submitNum})`
        }}
      </template>
      <template #createTime="{ record }"> {{ moment(record.createTime).format("YYYY-MM-DD") }} </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="toQuestionPage(record)">做题</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";
import { Question, QuestionControllerService } from "../../../generated/question";

const show = ref(true);

const dataList = ref([]);
const total = ref(10);
const searchParams = reactive({
  title: "",
  tags: [],
  pageSize: 2,
  current: 1,
});

const loadData = async () => {
  const resp = await QuestionControllerService.listQuestionVoByPageUsingPost(searchParams);
  if (resp.code === 0) {
    dataList.value = resp.data.records;
    total.value = resp.data.total;
  } else {
    message.error("加载失败：", resp.message);
    // console.log("error");
  }
};

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const columns = [
  {
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "题目名称",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "通过率",
    slotName: "acceptedRate",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    slotName: "optional",
  },
];

/**
 * 跳转到题目页面
 */
const router = useRouter();
const toQuestionPage = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};

/**
 * 监听函数体内所有变量
 * （但是reactive怎么这么煞笔，watchEffect不监测对象）
 */
watchEffect(() => {
  // searchParams.pageSize;
  // searchParams.current;
  Object.values(searchParams);
  loadData();
});

const onPageChange = (page: number) => {
  console.log(page);
  Object.assign(searchParams, {
    ...searchParams,
    current: page,
  });
  console.log(searchParams);
  // loadData();
};

const doSubmit = () => {
  searchParams.current = 1;
  loadData(); // todo 为什么删了不行？
};
</script>

<style scoped>
#QuestionsView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
