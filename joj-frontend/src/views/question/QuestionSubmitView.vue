<template>
  <div id="QuestionSubmitView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="questionId" label="题号" style="min-width: 240px">
        <a-input v-model="searchParams.questionId" placeholder="请输入题号" />
      </a-form-item>
      <a-select v-model="searchParams.language" :style="{ width: '320px' }" placeholder="选择编程语言">
        <a-option v-for="language in selectLanguage" :key="language" :value="language" :label="language" />
      </a-select>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">搜索</a-button>
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
      <template #judgeInfo="{ record }"> {{ JSON.stringify(record.judgeInfo) }} </template>
      <template #createTime="{ record }"> {{ moment(record.createTime).format("YYYY-MM-DD") }} </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import { Question, QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const show = ref(true);

const dataList = ref([]);
const total = ref(10);
const searchParams = reactive({
  questionId: undefined,
  language: undefined,
  pageSize: 10,
  current: 1,
});

const loadData = async () => {
  const resp = await QuestionControllerService.listQuestionSubmitByPageUsingPost({
    ...searchParams,
    sortField: "createTime",
    sortOrder: "desc",
  });
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
    title: "提交号",
    dataIndex: "id",
  },
  {
    title: "编程语言",
    dataIndex: "language",
  },
  {
    title: "判题信息",
    dataIndex: "judgeInfo",
    slotName: "judgeInfo",
  },
  {
    title: "判题状态",
    dataIndex: "status",
  },
  {
    title: "题目id",
    dataIndex: "questionId",
  },
  {
    title: "提交者id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    slotName: "createTime",
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
#QuestionSubmitView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
