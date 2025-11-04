import { StoreOptions } from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";
import { UserControllerService } from "../../generated/user";
import message from "@arco-design/web-vue/es/message";

const getters = {};

export default {
  namespaced: true,
  state: () => ({
    loginUser: {
      userName: "未登录",
      //  userRole: ACCESS_ENUM.NOT_LOGIN,
    },
  }),
  actions: {
    async getLoginUser({ commit, state }, payload) {
      const resp = await UserControllerService.getLoginUserUsingGet();
      if (resp.code === 0) {
        commit("updateUser", resp.data);
      } else {
        commit("updateUser", {
          ...state.loginUser,
          userRole: ACCESS_ENUM.NOT_LOGIN,
        });
      }
    },
    async Logout({ commit, state }, payload) {
      const resp = await UserControllerService.userLogoutUsingPost();
      if (resp.code === 0) {
        commit("updateUser", {
          userName: "未登录",
          userRole: ACCESS_ENUM.NOT_LOGIN,
        });
        message.success("注销成功！");
      } else {
        message.error("注销失败！");
      }
    },
  },
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
