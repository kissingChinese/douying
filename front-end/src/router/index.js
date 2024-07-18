import { createRouter, createWebHashHistory } from "vue-router";
import Home from "../views/home/index.vue";

const routes = [
  {
    path: "/",
    component: Home,
  },
  {
    path: "/video/:classify",
    component: Home,
    meta: {
      isClassify: true
    }
  }, 
  {
    path: "/video/search/:key",
    component: Home,
    meta: {
      isSearch: true
    }
  },
  {
    path: "/followVideo",
    component: () => import("../views/followVideo/index.vue"),
  },
  {
    path: "/video",
    component: () => import("../views/video/index.vue"),
  },
  {
    path:"/aiChat",
    component:()=>import("../views/chat/index.vue")
  },
  {
    path: "/pushVideo",
    component: () => import("../views/pushVideo/index.vue"),
  },
  {
    path: "/user",
    component: () => import("../views/user/index.vue"),
    redirect: "/user/home",
    children: [
      {
        path: "home",
        component: () => import("../views/user/home/index.vue"),
      },
      {
        path: "classify",
        component: () => import("../views/user/classify/index.vue"),
      },
      {
        path: "video",
        component: () => import("../views/user/myVideo/index.vue"),
      },
      {
        path: "favorites",
        component: () => import("../views/user/favorites/index.vue"),
      },
      {
        path: "like",
        component: () => import("../views/user/like/index.vue")
      },
      {
        path: "history",
        component: () => import("../views/user/history/index.vue")
      }
    ],
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes: routes,
});

export default router;
