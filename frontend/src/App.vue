<template>
  <div id="app">
    <Sidebar id="sidebar" v-if="showSide" />
    <div v-if="showFrame" id="main-frame">
      <div id="background-frame">
        <RouterView id="router" :key="$route.fullPath" />
      </div>
    </div>
    <RouterView v-if="!showFrame" id="router" :key="$route.fullPath" />
  </div>
</template>

<script>
import Sidebar from "@/components/common/Sidebar.vue";

export default {
  name: "App",
  components: {
    Sidebar,
  },
  computed: {
    showSide() {
      return !(
        this.$route.name === "Form" ||
        this.$route.name === "Finish" ||
        this.$route.name == "ResultLink" ||
        this.$route.name == "Login" ||
        this.$route.name == "SurveyClosed" ||
        this.$route.name == "SignUp"
      );
    },
    showFrame() {
      return !(
        this.$route.name === "Form" ||
        this.$route.name === "Finish" ||
        this.$route.name == "Login" ||
        this.$route.name == "SurveyClosed" ||
        this.$route.name == "SignUp"
      );
    },
  },
};
</script>

<style>
@import "./assets/style/buttons.css";

#app {
  display: flex;
}

#router {
  width: 100%;
}

@import "./assets/style/main.css";
</style>
