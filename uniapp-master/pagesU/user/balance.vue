<template>
	<view class="content column">
		<view class="balance bg-green margin-bottom-20 text-while column align-item">
			<view class="column balance-top">
				<text class="text-size-max">￥{{totalMoney}}</text>
				<text class="font-bold">账户余额</text>
			</view>
			<view class="balance-title font-bold row">
				<view class="balance-content">
					<text>充值</text>
				</view>
				<view class="balance-content">
					<text>提现</text>
				</view>
				<view class="balance-content">
					<text>说明</text>
				</view>
			</view>
		</view>
		
		<view class="content margin-bottom-20 column">
			<view class="balance-nav bg-white bottom-line">
				<view class="nav-content margin-width-20 justify-between align-item">
					<text>充值记录</text>
					<text class="icon-you yticon"></text>
				</view>
			</view>
			<view class="balance-nav bg-white bottom-line">
				<view class="nav-content margin-width-20 justify-between align-item">
					<text>提现记录</text>
					<text class="icon-you yticon"></text>
				</view>
			</view>
		</view>
		<view class="content margin-bottom-20 column">
			<view class="balance-nav bg-white bottom-line">
				<view class="nav-content margin-width-20 justify-between align-item">
					<text>交易流水</text>
					<text class="icon-you yticon"></text>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
	export default{
	components: {
    		mallplusCopyright
    	},
		data(){
			return{
				totalMoney:0.00,//总余额
				codeMoney:0.00,//扫码余额
				rechargeMoney:0.00,//充值余额
				userinfo: {},
			}
		},
		async onShow(){
			 let params = {  };
                        				let data1 = await Api.apiCall('get', Api.member.currentMember, params);
            					this.totalMoney = data1.blance;
		},
		methods:{
			
		}
	}
</script>

<style>
page{background-color: #F3F3F3;}
view {font-size: 30upx;line-height: 1.8;display: flex;}
.row{flex-direction: row;}
.column {flex-direction: column;}
.text-size-max{font-size: 80upx;}
.justify-center{justify-content: center;}
.justify-between{justify-content: space-between;}
.align-item{align-items: center;}
.bg-white{background: #FFFFFF;}
.bg-green {background: #F44142;}
.text-size-24 {font-size: 24upx;}
.text-size-mim {font-size: 26upx;}
.text-grey {color: #9B9B9B;}
.text-while{color: #FFFFFF;}
.font-bold{font-weight: bold;}
.bottom-line{border-bottom: #EEEEEE solid 1upx}
.margin-bottom-20{margin-bottom: 20upx;}
.margin-top-20{margin-top: 20upx;}
.margin-width-20{margin:0 20upx;}

.content{width: 100%;}
.balance{width: 100%;}
.balance-top{width: 100%;align-items: center;line-height: 1.5;margin-top: 20upx;}
.balance-title{background: rgba(255,255,255,0.12);margin-top: 40upx;width: 100%;height: 100upx;justify-content: center;align-items: center;}
.balance-content{width: 33%;height: 100upx;align-items: center;justify-content: center;}
.balance-content image{width: 50upx;height: 50upx;margin-right: 10upx;}
.balance-nav{width: 100%;height: 100upx;}
.nav-content{width: 100%;}
</style>
