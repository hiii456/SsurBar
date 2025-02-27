<template>
  <div v-if="isDataExist" class="chart-container">
    <div class="select-div">
      <div class="select-div-text">비교 설문 선택</div>
      <el-select
        v-model="value"
        multiple
        :multiple-limit="5"
        @change="selectSurvey"
        placeholder="Select"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          :disabled="item.disabled"
        >
        </el-option>
      </el-select>
    </div>
    <div class="line-chart-div" :class="{ 'line-chart-div-flex-start': isFlexStart }">
      <div>
        <LineChart :style="{ width: '100%' }"></LineChart>
      </div>
    </div>
    <div class="group-bar-chart-div">
      <div>
        <GroupBarChart :style="{ width: '100%' }"></GroupBarChart>
      </div>
    </div>
  </div>
  <div v-else class="empty-div">
    <EmptyData></EmptyData>
  </div>
</template>

<script>
import { mapState, mapGetters, mapActions } from "vuex";
import LineChart from "@/components/charts/LineChart";
import GroupBarChart from "@/components/charts/GroupBarChart";
import axios from "@/utils/axios.js";
import EmptyData from "@/components/detail/DataEmpty.vue";

export default {
  name: "SurveyComparison",
  components: { LineChart, EmptyData, GroupBarChart },
  props: ["surveyId"],
  data() {
    return {
      count: null,
      widthTemp: "",
      isFlexStart: false,
      isDataExist: false,
      value: [],
      isSelectFull: false,
      myDataSet: null,
      options: [],
      colorList: [
        `rgba(66,57,55,0.6)`,
        `rgba(196,107,65,0.6)`,
        `rgba(86,57,195,0.6)`,
        `rgba(26,147,200,0.6)`,
        `rgba(166,25,235,0.6)`,
      ],
    };
  },
  computed: {
    ...mapState("analysis", ["answerDataList", "questionCount"]),
  },
  watch: {
    answerDataList() {
      this.count = this.questionCount;

      this.widthTemp = this.count * 1 + "vh";
      if (this.count * 6 > 60) {
        this.isFlexStart = true;
      }

      this.makeChart();
    },
  },
  methods: {
    ...mapActions("analysis", [
      "setComparisonDataSets",
      "setComparisonLabels",
      "setSurveyTitleList",
    ]),
    ...mapGetters("analysis", ["getAnswerDataList", "getComparisonDataSets"]),
    makeChart() {
      if (this.getAnswerDataList() == null || this.getAnswerDataList().length == 0) {
        this.isDataExist = false;
        return;
      }

      this.isDataExist = true;

      let questionDataList = [];
      let questionTitles = [];

      let color = `rgba(156,187,255,0.6)`;

      this.getAnswerDataList().forEach((category) => {
        category.questionDataList.forEach((el) => {
          questionDataList.push(el.averageScore);

          let title = el.number + " " + el.title;
          let length = title.length;
          let label = length > 10 ? title.substring(0, 10) + "..." : title;

          questionTitles.push(label);
        });
      });
      let dataSet = {
        label: "현재 설문",
        pointBackgroundColor: color,
        backgroundColor: color,
        borderWidth: 2,
        borderColor: color,
        fill: true,
        pointBorderColor: color,
        data: questionDataList,
      };
      let dataSets = [];

      dataSets.push(dataSet);

      this.myDataSet = dataSet;

      this.setComparisonDataSets(dataSets);
      this.setComparisonLabels(questionTitles);
    },
    getSameTemplateSurvey() {
      axios.get(`/survey/${this.surveyId}/template`).then((res) => {
        res.data.surveyInfoList.forEach((el, idx) => {
          let endTime = el.endTime;
          let id = el.surveyId;
          let teamName = el.teamName;
          let option = {
            id: id,
            value: idx,
            label: `${teamName} ${endTime}`,
            disabled: false,
          };
          this.options.push(option);
        });
      });
    },
    selectSurvey() {
      let dataSets = [];
      dataSets.push(this.myDataSet);
      if (this.value.length == 0) {
        this.setComparisonDataSets(dataSets);
        return;
      }

      let surveyTitleList = ["현재 설문"];
      this.value.forEach((optionIdx, idx) => {
        surveyTitleList.push(this.options[optionIdx].label);
        axios
          .get(`/survey/${this.options[optionIdx].id}/answer`, {
            params: { filterDataStr: encodeURI("[]") },
          })
          .then((res) => {
            let color = this.colorList[idx];

            let dataList = [];

            res.data.answerDataList.forEach((answerData) => {
              answerData.questionDataList.forEach((el) => {
                dataList.push(el.averageScore);
              });
            });

            let dataSet = {
              label: `${this.options[optionIdx].label}`,
              pointBackgroundColor: color,
              backgroundColor: color,
              borderWidth: 2,
              borderColor: color,
              fill: false,
              pointBorderColor: color,
              data: dataList,
            };

            dataSets.push(dataSet);
          });
      });

      this.setSurveyTitleList(surveyTitleList);

      this.setComparisonDataSets(dataSets);
    },
  },
  mounted() {
    this.getSameTemplateSurvey();
  },
  created() {
    this.count = this.questionCount;

    console.log(this.count);

    if (this.count != null) {
      this.widthTemp = this.count * 6 + "vw";
      if (this.count * 6 > 60) {
        this.isFlexStart = true;
      }
    }

    this.makeChart();
  },
};
</script>

<style scoped>
.chart-container {
  /* display: flex; */
  /* flex-direction: column;
  align-items: center; */
  overflow: scroll;
  overflow-x: hidden;
  height: 100%;
  width: 100%;
}

.chart-container::-webkit-scrollbar {
  width: 1vh;
}
.chart-container::-webkit-scrollbar-track {
  background-color: #dde0e7;
}

.chart-container::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: #9cbbff;
}

.empty-div {
  height: 100%;
}
.line-chart-div {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 70vh;
  margin-left: 5%;
  margin-right: 5%;
  margin-top: 3vh;
  width: 90%;
  overflow: auto;
  overflow-y: hidden;
  overflow-x: hidden;
}

.line-chart-div > div {
  height: 100%;
  padding-top: 3%;
  width: 100%;
}

.line-chart-div > div > div {
  height: 90%;
}

.line-chart-div-flex-start {
}

.select-div {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-left: 5%;
  padding-right: 5%;
  padding-top: 2vh;
  width: 100%;
}

.select-div div {
  width: 100%;
}

.el-select {
  width: 150% !important;
}

.select-div-text {
  width: 20% !important;
}

.line-chart-div::-webkit-scrollbar {
  height: 1vh;
}
.line-chart-div::-webkit-scrollbar-track {
  background-color: #dde0e7;
}

.line-chart-div::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: #9cbbff;
}

.group-bar-chart-div {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.group-bar-chart-div > div {
  width: 70%;
}
</style>
