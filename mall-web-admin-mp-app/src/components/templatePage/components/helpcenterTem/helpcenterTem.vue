<style lang="less" scoped>
@import "./helpcenterTem.less";
</style>

<script>
export default {
  props: ["options"],
  data() {
    return {
      order_num: "4",
      order_bg: "#fff",
      order_iconcolor: "#fff",
      order_textcolor: "#fff",
      order_badgecolor: "#fff",
      newOptions: {},
      colorGroup: []
    };
  },
  watch: {
    options() {
      let _this = this;
      _this.newOptions = _this.options;
      _this.init(_this.newOptions);
    }
  },
  created() {
    let _this = this;
    _this.init(_this.options);
  },
  methods: {
    // 初始化
    init(options) {
      let _this = this;
      if (JSON.stringify(options) == "{}") {
        _this.restore();
      } else {
        _this.order_num = options.order_num;
        _this.order_bg = options.order_bg;
        _this.order_iconcolor = options.order_iconcolor;
        _this.order_textcolor = options.order_textcolor;
        _this.order_badgecolor = options.order_badgecolor;
        _this.colorGroup = options.colorGroup;
      }
    },
    // 恢复初始
    restore() {
      let _this = this;
      _this.order_num = "4"
      _this.order_bg = "#fff"
      _this.order_iconcolor = "#fff"
      _this.order_textcolor = "#fff"
      _this.order_badgecolor = "#fff"
      _this.colorGroup = []
      for(let i = 0; i < _this.order_num; i++) {
        let newObj = {
          adImg: "",
          adText:"",
          adLink: ""
        }
        _this.colorGroup.push(newObj)
      }
    },
    // 更换banner
    changeSwiper(oldvalue, value) {
      let _this = this;
      _this.currentId = value;
    }
  }
};
</script>
