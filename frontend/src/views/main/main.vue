<template>
  <div id="wrapper">
    <div id="wrapper-div">
      <div id="container">
        <div id="intro">
          안녕하세요! 지금 <b>SSURBAR</b>를 통해 설문을 시작해보세요 📝
        </div>
        <div id="bottonBox">
          <div class="surveyButton" @click="clickScratch">
            <img class="buttonImg" src="@/assets/newdoc.png" />
            <span
              style="
                display: inline-block;
                vertical-align: middle;
                margin-top: 5%;
              "
            >
              Start from scratch
              <h5>신규 설문 생성</h5>
            </span>
          </div>
          <div class="surveyButton" @click="clickTemplate">
            <img class="buttonImg" src="@/assets/edit.png" />
            <span
              style="
                display: inline-block;
                vertical-align: middle;
                margin-top: 5%;
              "
            >
              Start from a template
              <h5>기본 서식에서 시작</h5>
            </span>
          </div>
          <div class="surveyButton" @click="clickPastSurvey">
            <img class="buttonImg" src="@/assets/copy.png" />
            <span
              style="
                display: inline-block;
                vertical-align: middle;
                margin-top: 5%;
              "
            >
              Copy a past survey
              <h5>지난 설문조사 복사</h5>
            </span>
          </div>
        </div>

        <hr />

        <div id="tab-div">
          <ul class="tabs">
            <li
              class="tab"
              :class="{ selectedTab: selectedTab == 0 }"
              @click="changeTab(0)"
            >
              진행중인 설문
            </li>
            <li
              class="tab"
              :class="{ selectedTab: selectedTab == 1 }"
              @click="changeTab(1)"
            >
              완료된 설문
            </li>
          </ul>
        </div>
        <ongoingList></ongoingList>
      </div>
    </div>
  </div>
</template>

<script>
import ongoingList from "@/views/main/ongoingList.vue";
import { mapActions, mapState } from "vuex";
export default {
  name: "App",
  components: {
    ongoingList,
  },
  computed: {
    ...mapState("survey", ["surveyCreateType"]),
    ...mapState("list", ["selectedTab"]),
  },
  methods: {
    ...mapActions("survey", ["setCreateType"]),
    ...mapActions("question", ["initQuestionData"]),
    ...mapActions("analysis", ["initAnalysisData"]),
    ...mapActions("filterQuestion", ["initFilterQuestionData"]),
    ...mapActions("template", ["initTemplateData"]),
    ...mapActions("list", ["initListData", "setSelectedTab"]),
    changeTab(tabNum) {
      this.setSelectedTab(tabNum);
    },
    clickScratch() {
      this.setCreateType(this.surveyCreateType.NEW);
      this.$router.push("/form/createform");
    },
    clickTemplate() {
      this.setCreateType(this.surveyCreateType.DEFAULT);
      this.$router.push("/template/default");
    },
    clickPastSurvey() {
      this.setCreateType(this.surveyCreateType.RECENT);
      this.$router.push("/template/recent");
    },
  },
  created() {
    this.initQuestionData();
    this.initAnalysisData();
    this.initFilterQuestionData();
    this.initTemplateData();
    this.initListData();
  },
};
</script>

<style scoped>
.nav-pills > li > a {
  border-radius: 0;
}

#wrapper {
  padding-left: 0;
  -webkit-transition: all 0.5s ease;
  -moz-transition: all 0.5s ease;
  -o-transition: all 0.5s ease;
  transition: all 0.5s ease;
  overflow: hidden;
}
#wrapper-div {
  background-position: center;
  background-color: white;
  margin-top: 3%;
  margin-left: 4%;
  margin-right: 4%;
  border-radius: 60px 60px 0% 0%;
}
#intro {
  font-size: 2.5em;
  text-align: center;
  margin-top: 3%;
  margin-bottom: 3%;
}
#container {
  padding: 4%;
  padding-right: 4%;
}
#bottonBox {
  display: flex;
  justify-content: space-between;
  text-align: center;
  margin-top: 1%;
  margin-bottom: 5%;
}
#bottonBox .surveyButton:hover {
  transform: scale(1.1);
  cursor: pointer;
}
#tab-div {
  margin-top: 2.5%;
  margin-bottom: 0px;
}
.surveyButton {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 25%;
  height: 100%;
  border-radius: 10px 10px 10px 10px;
  padding: 2% 2% 2% 2%;
  margin-left: 5%;
  margin-right: 5%;
  box-shadow: 2px 2px 2px 2px rgb(206, 206, 206);
  font-size: 0.5em;
  color: rgb(167, 167, 167);
}

.surveyButton > span > h5 {
  font-weight: 600;
  color: black;
}

.buttonImg {
  height: 50px;
  width: 50px;
  display: inline-block;
}

.sidebar-nav {
  position: absolute;
  top: 0;
  width: 100px;
  margin: 0;
  padding: 0;
  list-style: none;
  margin-top: 2px;
}

.sidebar-nav li {
  text-indent: 15px;
  line-height: 40px;
}

.sidebar-nav li a {
  display: block;
  text-decoration: none;
  color: #999999;
}

.sidebar-nav li a:hover {
  color: rgb(5, 25, 58);
  background: black;
  width: 8%;
}

@media (min-width: 768px) {
  #wrapper {
    padding-left: 100px;
  }
  .fixed-brand {
    width: 100px;
  }
  #wrapper.toggled {
    padding-left: 0;
  }
  #sidebar-wrapper {
    width: 100px;
  }
}
</style>
