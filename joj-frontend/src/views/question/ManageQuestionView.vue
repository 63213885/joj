<template>
  <div id="manageQuestionView">
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
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doUpdate(record)">修改</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import { Question, QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const show = ref(true);

const dataList = ref([]);
const total = ref(10);
const searchParams = reactive({
  pageSize: 2,
  current: 1,
});

const loadData = async () => {
  const resp = await QuestionControllerService.listQuestionByPageUsingPost(searchParams);
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
    title: "id",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "题目限制",
    dataIndex: "judgeConfig",
  },
  {
    title: "内容",
    dataIndex: "content",
  },
  {
    title: "题目样例",
    dataIndex: "judgeCase",
  },
  {
    title: "标签",
    dataIndex: "tags",
  },
  {
    title: "答案",
    dataIndex: "answer",
  },
  {
    title: "提交数",
    dataIndex: "submitNum",
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
  },
  {
    title: "出题人Id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];

const doDelete = async (question: Question) => {
  const resp = await QuestionControllerService.deleteQuestionUsingPost({ id: question.id });
  if (resp.code === 0) {
    message.success("删除成功");
    loadData();
  } else {
    message.error("删除失败：", resp.message);
  }
};

const router = useRouter();
const doUpdate = (question: Question) => {
  router.push({
    path: "/update/question",
    query: {
      id: question.id,
    },
  });
};

/**
 * 监听函数体内所有变量
 * （但是reactive怎么这么煞笔，watchEffect不监测对象）
 */
watchEffect(() => {
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
</script>

<style scoped>
#manageQuestionView {
}
</style>
