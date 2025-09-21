import store from "@/store";
import router from "@/router";
import ACCESS_ENUM from "@/access/accessEnum";

router.beforeEach(async (to, from, next) => {
  console.log("登录用户信息", store.state.user.loginUser);

  const loginUser = store.state.user.loginUser;
  // 如果之前没登录过就自动登录
  if (!loginUser || !loginUser.userRole) {
    await store.dispatch("user/getLoginUser");
  }
  const needAccess = to.meta?.access ?? ACCESS_ENUM.NOT_LOGIN;

  next();
});
