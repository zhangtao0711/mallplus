<template>
  <div class="div-bg bg-white" >
    <div style="height: 0.5rem;width: 100%;background-color:#f7f7f7"></div>
    <!--物流跟踪-->
    <div style="padding-bottom: 0.5rem;">
      <div class="bg-white" style="width: 92%; margin-left: 4%;margin: auto;padding-left: 15px;padding-right: 15px;">
        <div style="font-size: 1rem;color: #111111;">物流跟踪<!--物流跟踪--></div>
        <div>
          <div class="track-rcol">
            <div class="track-list">
              <ul>
                <div v-for="(item,index) in logisticsList" :key="index">
                  <li class="first" v-if="index===0">
                    <div></div>
                    <i class="node-icon"></i>
                    <span class="txt">{{item.AcceptStation}}</span>
                    <span class="time">{{item.AcceptTime}}</span>
                  </li>
                  <li v-if="index > 0 && index !== logisticsList.length-1">
                    <i class="node-icon"></i>
                    <span class="txt">{{item.AcceptStation}}</span>
                    <span class="time">{{item.AcceptTime}}</span>
                  </li>
                  <li v-if="index === logisticsList.length-1" class="finall">
                    <i class="div-spilander"></i>
                    <i class="node-icon"></i>
                    <span class="txt">{{item.AcceptStation}}</span>
                    <span class="time">{{item.AcceptTime}}</span>
                  </li>
                </div>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Api from '@/common/api';
	
export default {
  name: 'Logic',
  data () {
    return {
      logisticsList: []
    }
  },
  async onLoad(options) {
  	let params = { orderId: options.order_id };
  	let returnLogisticby = await Api.apiCall('get', Api.order.logisticbyapi, params);
	this.logisticsList = returnLogisticby.Traces;
  },
  methods: {
  },
  /**
   * 加载时执行
   */
  mounted: function () {
  }
}
</script>
<style scoped>
  .message-text{
    font-family: MicrosoftYaHei;
    font-size: 1rem;
    font-weight: normal;
    font-stretch: normal;
    line-height: 3rem;
    letter-spacing: 0rem;
    color: #333333;
    width: 50%;
  }
  .fontblack{
    color:#999999
  }
  .img2{
    width: .81rem;
    height: .8rem;
    float: right;
  }
  .addressshow2{
    height: auto;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    width: 75%;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    white-space: normal;
    word-wrap: break-word;
    word-break: break-all;
    font-size: 1rem;
  }
  .addressshow1{
    height: auto;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    width: 75%;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    white-space: normal;
    word-wrap: break-word;
    word-break: break-all;
    font-size: 1rem;
  }
  .orderTitle{
    font-size: 1rem;
    color: #333333;
    height: auto;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    white-space: normal;
    word-wrap: break-word;
    word-break: break-all;
    height: 2.5rem;
  }
  .orderDetail{
    font-size: 0.8rem;
    color: #666666;
    text-align:left;
  }
  .border-ceter{
    width: 92%;
    padding-left: 15px;
    padding-right: 15px;
  }
  .pay-button{
    width: 88%;
    height: 2.6rem;
    position:relative;
    background-color: red;
    color: white;

    margin-left: 6%;
  }
  ul li{
    list-style:none;
    font-size: 0.7rem;
  }
  ul {
    padding-left: 1.5rem
  }
  .track-rcol{}
  .track-list{
    position:relative;
  }
  .track-list li{
    position:relative;
    padding:0px 0 1.5rem 25px;
    line-height:1rem;
    border-left:1px solid #d9d9d9;
    color: #999;
  }
  .track-list li.first{
    color:red;
    padding-top:0;
    width: 100%;
    text-align: left;
    border-left:1px solid #d9d9d9;
  }
  .track-list li .node-icon{
    position: absolute;
    left: -5.5px;
    border-radius: 0.25rem;
    width: 0.5rem;
    height: 0.5rem;
    top:4px;
    background-color: #999999;
  }
  .track-list li.first .node-icon{
    background-position:0-72px;
    background-color: red;
    width: 1rem;
    z-index: 2;
    height: 1rem;
    position: absolute;
    left: -9px;
    top: 0;
    border-radius: 0.5rem;
  }
  .track-list li .time{
    margin-right:20px;
    position:relative;
    top:4px;
    display:inline-block;
    vertical-align:middle;
    background-color: white;
    color: #999;
    width: 100%;
    text-align: left;
  }
  .track-list li .txt{
    position:relative;
    display:inline-block;
    vertical-align:top;
    color: #999;
  }
  .track-list li.first .time{
    text-align: left;
    width: 94%;
    color:red;
  }
  .track-list li.first .txt{
    color: red;
    text-align: left;
    width: 94%;
  }
  .track-list li.finall{
    position:relative;
    padding:0px 0 1.5rem 25px;
    line-height:18px;
    border-color: white;
    border-left:1px solid #ffffff;
    color: #999;
  }
  .track-list li.finall .div-spilander{
    width: 1px;
    position: absolute;
    position: absolute;
    left: -1.5px;
    height: 0.5rem;
    background-color: #d9d9d9;
  }

</style>