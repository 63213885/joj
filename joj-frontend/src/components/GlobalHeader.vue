<template>
  <a-row id="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu mode="horizontal" :selected-keys="selectedKeys" @menu-item-click="doMenuclick">
        <a-menu-item key="0" :style="{ padding: 0, marginRight: '38px' }" disabled>
          <div class="title-bar">
            <img class="logo" src="@/assets/oj-logo.svg" />
            <div class="title">joj</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div v-if="checkAccess(store.state.user?.loginUser, ACCESS_ENUM.USER)">
        <a-dropdown @select="handleSelect">
          <a-avatar :style="{ backgroundColor: '#3370ff' }">
            <!--            <IconUser />-->
            {{ store.state.user?.loginUser?.userName ?? "登录有bug了" }}
          </a-avatar>
          <template #content>
            <a-doption value="profile">个人中心</a-doption>
            <a-doption value="space">我的空间</a-doption>
            <a-doption value="logout">退出登录</a-doption>
          </template>
        </a-dropdown>
      </div>
      <div v-else>
        <a-space>
          <a-button type="primary" @click="doLogin">登录</a-button>
          <a-button type="outline" @click="doRegister">注册</a-button>
        </a-space>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, onUnmounted, ref } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import ACCESS_ENUM from "@/access/accessEnum";

const router = useRouter();
const store = useStore();

// 展示在菜单的路由数组
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    if (!checkAccess(store.state.user.loginUser, item?.meta?.access as string)) {
      return false;
    }
    return true;
  });
});

// 默认主页
const selectedKeys = ref(["/"]);

// 路由跳转后，更新选中的菜单项
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

const doMenuclick = (key: string) => {
  router.push({
    path: key,
  });
};

const doLogin = () => {
  router.push({
    path: "/user/login",
  });
};

const doRegister = () => {
  router.push({
    path: "/user/register",
  });
};

const handleSelect = async (value: string) => {
  if (value === "logout") {
    await store.dispatch("user/Logout");
    router.push({ path: "/" });
  } else if (value === "profile") {
    router.push("/user/profile");
  } else {
    router.push("/user/space");
  }
};
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: #444;
  margin-left: 16px;
}

.logo {
  height: 48px;
}
</style>
