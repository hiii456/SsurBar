<template>
  <div id="wrapper">
    <div class="main-container">
      <div id="tab-div">
        <ul class="tabs">
          <li
            class="tab"
            :class="{ selectedTab: selectedTabNum == 0 }"
            @click="changeTab(0)"
          >
            분석
          </li>
          <li
            class="tab"
            :class="{ selectedTab: selectedTabNum == 1 }"
            @click="changeTab(1)"
          >
            비교
          </li>
          <li
            class="tab"
            :class="{ selectedTab: selectedTabNum == 2 }"
            @click="changeTab(2)"
          >
            배포
          </li>
          <!-- <li class="tab" :class="{ selectedTab: selectedTabNum == 3 }" @click="changeTab(3)">
            결과
          </li> -->
          <li @click="exportExcel">
            <i class="fas fa-file-excel fa-lg"></i>
          </li>
        </ul>
      </div>
      <div class="detail-container">
        <div
          class="filter-container el-card is-always-shadow"
          v-if="!checkFullContent"
        >
          <div class="filter-title-div" @click="clickFilterOpen()">
            <div class="name-text"><i class="fas fa-filter"></i> 필터</div>
            <i v-if="isFilterOpened" class="el-icon-arrow-down"></i>
            <i v-else class="el-icon-arrow-right"></i>
          </div>
          <div v-if="isFilterOpened" class="filter-main-div">
            <div class="filter-list">
              <div
                class="filter-div"
                v-for="(filter, filterIdx) in filterList"
                :key="filterIdx"
              >
                <div class="filter el-card is-always-shadow">
                  <div class="filter-title" @click="clickFilterDiv(filterIdx)">
                    {{ filter.name }}

                    <i v-if="filter.isSelected" class="el-icon-arrow-down"></i>
                    <i v-else class="el-icon-arrow-right"></i>
                  </div>
                  <div class="filter-content" v-if="filter.isSelected">
                    <div
                      v-for="(filterName, nameIdx) in filter.filterNames"
                      :key="nameIdx"
                    >
                      <label class="filter-label" :for="filter + filterName">{{
                        filterName
                      }}</label>
                      <input
                        type="checkbox"
                        :id="filter + filterName"
                        :value="filterName"
                        v-model="checkedFilter[filterIdx].filterValue"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="button-div">
              <button @click="applyFilter()" class="custom-btn2 btn-6">
                적용하기
              </button>
            </div>
          </div>
        </div>
        <div
          class="content-container"
          :class="{ isFullContent: checkFullContent }"
        >
          <div
            class="component-container el-card is-always-shadow"
            :class="{ isFullContent: checkFullContent }"
          >
            <div class="component-div" v-if="selectedTabNum == 0">
              <SurveyAnalysis :surveyId="surveyId" />
            </div>
            <div class="component-div" v-if="selectedTabNum == 1">
              <SurveyComparison :surveyId="surveyId" />
            </div>
            <div class="component-div" v-if="selectedTabNum == 2">
              <SurveyRealease :surveyId="surveyId" />
            </div>
            <div class="component-div" v-if="selectedTabNum == 3">
              <SurveyResult :surveyId="surveyId" />
            </div>
          </div>
          <div
            class="feedback-container el-card is-always-shadow"
            :class="{ 'opened-feedback': isFeedbackOpened }"
            v-if="!checkFullContent"
          >
            <div
              :class="[
                { 'feedback-title-open': isFeedbackOpened },
                { 'feedback-title-close': !isFeedbackOpened },
              ]"
              @click="clickFeedbackDiv()"
            >
              <div class="feedback-name-text">
                <i class="far fa-comments"></i> 피드백
              </div>
              <i v-if="isFeedbackOpened" class="el-icon-arrow-down"></i>
              <i v-else class="el-icon-arrow-right"></i>
            </div>
            <div v-if="isFeedbackOpened" class="feedback-main-div">
              <div class="feedback-content-div">
                <textarea
                  v-if="isEditState"
                  class="feedback-content"
                  type="textarea"
                  placeholder="Feedback input"
                  v-model="feedbackContent"
                >
                </textarea>
                <textarea
                  disabled
                  class="feedback-content feedback-text"
                  v-else
                  v-model="feedbackContentBackup"
                ></textarea>
              </div>
              <div class="feedback-button-div">
                <button
                  @click="clickEditButton"
                  v-if="!isEditState"
                  class="custom-btn2 btn-6"
                >
                  수정</button
                ><button
                  @click="clickEditSubmitButton"
                  v-if="isEditState"
                  class="custom-btn2 btn-6"
                >
                  완료
                </button>
                <button
                  @click="clickEditCancelButton"
                  v-if="isEditState"
                  class="custom-btn1 btn-5"
                >
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "@/utils/axios.js";
import { mapActions } from "vuex";
import SurveyRealease from "@/components/detail/SurveyRealease.vue";
import SurveyAnalysis from "@/components/detail/SurveyAnalysis.vue";
import SurveyResult from "@/components/detail/SurveyResult.vue";
import SurveyComparison from "@/components/detail/SurveyComparison.vue";
import excel from "@/utils/excel.js";
export default {
  name: "SurveyDetail",
  props: ["selectedSurveyId"],
  components: {
    SurveyRealease,
    SurveyAnalysis,
    SurveyResult,
    SurveyComparison,
  },
  data() {
    return {
      selectedTabNum: 0,
      checkedFilter: [],
      filterList: [],
      isFeedbackOpened: false,
      feedbackContentBackup: "",
      feedbackContent: "",
      isEditState: false,
      isFilterOpened: false,
    };
  },
  methods: {
    ...mapActions("analysis", ["setAnswerData"]),
    changeTab(tabNum) {
      this.selectedTabNum = tabNum;
    },
    applyFilter() {
      console.log(this.checkedFilter);

      let filterStr = JSON.stringify(this.checkedFilter);
      console.log(filterStr);
      let searchData = {
        filterStr: filterStr,
        surveyId: this.surveyId,
      };
      this.setAnswerData(searchData);
    },
    clickFilterDiv(filterIdx) {
      this.filterList[filterIdx].isSelected =
        !this.filterList[filterIdx].isSelected;
    },
    clickFeedbackDiv() {
      this.isFeedbackOpened = !this.isFeedbackOpened;
    },
    clickEditButton() {
      this.feedbackContentBackup = this.feedbackContent;
      this.isEditState = true;
    },
    clickEditSubmitButton() {
      this.saveIntegratedFeedback();
      this.feedbackContentBackup = this.feedbackContent;
      this.isEditState = false;
    },
    clickEditCancelButton() {
      this.feedbackContent = this.feedbackContentBackup;
      this.isEditState = false;
    },
    saveIntegratedFeedback() {
      axios
        .post("/feedback", {
          surveyId: this.surveyId,
          comment: this.feedbackContent,
        })
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getIntegratedFeedback() {
      axios
        .get(`/feedback/${this.surveyId}`)
        .then((res) => {
          console.log(res);
          this.feedbackContent = this.feedbackContentBackup = res.data.comment;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    clickFilterOpen() {
      this.isFilterOpened = !this.isFilterOpened;
    },
    exportExcel() {
      excel.downloadSurvey(this.surveyId);
    },
  },
  computed: {
    checkFullContent() {
      if (this.selectedTabNum == 0) {
        return false;
      }
      return true;
    },
  },
  created() {
    this.surveyId = this.$route.params.selectedSurveyId;
    this.getIntegratedFeedback();
    axios
      .get(`/survey/${this.surveyId}/filters`)
      .then((res) => {
        res.data.filterQuestionList.forEach((el) => {
          let title = el.title;
          let content = JSON.parse(el.content);

          let filterNames = [];

          Object.keys(content).forEach((key) => {
            filterNames.push(content[key]);
          });

          this.checkedFilter.push({
            filterKind: title,
            filterValue: [],
          });

          this.filterList.push({
            name: title,
            isSelected: false,
            filterNames: filterNames,
          });
        });
      })
      .catch((err) => {
        console.log(err);
      });

    let filterStr = JSON.stringify(this.checkedFilter);
    let searchData = {
      filterStr: filterStr,
      surveyId: this.surveyId,
    };
    this.setAnswerData(searchData);
  },
};
</script>

<style>
@import "../../assets/style/buttons.css";

.component-div {
  height: 100%;
}

ul.tabs {
  margin: 0px;
  padding: 0px;
  list-style: none;
}

ul.tabs li {
  background: none;
  color: #dde0e7;
  display: inline-block;
  padding: 10px 15px;
  cursor: pointer;
}

ul.tabs li:hover {
  background: none;
  color: #9cbbff;
  display: inline-block;
  padding: 10px 15px;
  cursor: pointer;
}

.main-container {
  padding: 3%;
  width: 100%;
}

.detail-container {
  min-height: 75vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.feedback-container {
  width: 100%;
}
.component-container {
  width: 100%;
  height: 100%;
}
.filter-container {
  width: 100%;
  margin-bottom: 10px;
}

.isFullContent {
  width: 100% !important;
}

.selectedTab {
  color: #0e4194 !important;
  font-weight: 700;
}

.teste {
}

#tab-div {
  margin-bottom: 1%;
}

.el-card {
  transition: 0s !important;
}
</style>

<style scoped>
button {
  font-size: 15px;
}

.filter-div {
  display: flex;
  justify-content: center;
  width: 20%;
  margin-bottom: 1vh;
}
.filter-title {
  display: flex;
  justify-content: space-between;
  border-radius: 4px;
  padding: 2%;
  /* padding-left: 2%; */
  /* margin: 2%; */
  background-color: #dde0e7;
  width: 100%;
  /* border-style: solid; */
  cursor: pointer;
  /* transition: border-bottom-color 0.3s; */
}
.filter {
  /* display: flex;
  flex-direction: column;
  align-items: center; */
  width: 90% !important;
  border: 0px;
  transition: 0.3s;
  /* border-style: solid; */
  /* padding: 5%; */
}
.filter-content {
  padding: 5%;
  width: 90%;
}

.content-container {
  width: 100%;
  height: 75vh;
  display: flex;
  flex-direction: column;
}

.filter-content div {
  cursor: pointer;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-content label {
  cursor: pointer;
}

.filter-content input {
  cursor: pointer;
}

.filter-list {
  width: 100%;
  height: 80%;
  overflow: auto;
  overflow-x: hidden;
  display: flex;
  flex-wrap: wrap;
  /* flex-direction: column; */
  align-items: flex-start;

  /* padding-top: 10%; */
  /* scrollbar-width: 3%; */
  justify-content: flex-start;
}

.filter-list::-webkit-scrollbar {
  width: 1vh;
}
.filter-list::-webkit-scrollbar-track {
  background-color: #dde0e7;
}

.filter-list::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: #9cbbff;
}

.filter-list i {
  display: flex;
  align-items: center;
  font-weight: 600;
}

.filter {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.button-div {
  display: flex;
  height: 15%;
  justify-content: flex-end;
  align-items: flex-end;
  padding: 10px;
  /* margin-bottom: 10%; */
}

.apply-button {
  /* margin: 5%;
  height: 50%; */
}

.filter-main-div {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 92%;
  /* overflow: scroll; */
}
.name-text {
  font-weight: 600;
  padding: 2vh;
}

.feedback-name-text {
  font-weight: 600;
  width: 100%;
  height: 100%;
  display: flex;
  padding-left: 1%;
  align-items: center;
}

.feedback-title-open {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 20%;
  cursor: pointer;
}

.feedback-title-close {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  cursor: pointer;
}

.feedback-text {
  background-color: white;
  border-style: none !important;
}

.feedback-title-open i {
  font-weight: 900;
  margin: 1%;
}

.feedback-title-close i {
  font-weight: 900;
  margin: 1%;
}
.filter-label {
  width: 100%;
}

.opened-feedback {
  height: 50%;
}

.feedback-button-div {
  display: flex;
  height: 25%;
  justify-content: flex-end;
}

.feedback-button-div button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 10%;
  margin: 0.5% !important;
}
.feedback-content-div {
  display: flex;
  justify-content: center;
  height: 80%;
}
.feedback-content {
  width: 95%;
  height: 90%;
  border: 1px solid;
  border-radius: 4px;
  border-color: #dde0e7;
  resize: none;
}

.feedback-content:focus {
  outline-color: #9cbbff;
}

.feedback-main-div {
  height: 75%;
}

.feedback-content::-webkit-scrollbar {
  width: 1vh;
}
.feedback-content::-webkit-scrollbar-track {
  background-color: #dde0e7;
}

.feedback-content::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: #9cbbff;
}

.filter-title-div {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-title-div:hover {
  cursor: pointer;
}

.filter-title-div > i {
  font-weight: 600;
  padding-right: 2vh;
}

.fa-file-excel {
  color: green;
}
</style>
