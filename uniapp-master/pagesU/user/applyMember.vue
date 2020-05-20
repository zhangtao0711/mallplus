<template>
	<view>
		<view class="block">

			<view class="content">
				<view class="my">
					我的账户余额：{{money}} 元
				</view>
			</view>
		</view>
		<view class="block">
			<view class="title">
				会员等级
			</view>
			<view class="content">
				<view class="amount">
					<view class="list">
						<view class="box" v-for="(amount,index) in amountList" :key="index" @tap="select(amount)" :class="{'on':amount.id == inputAmount}">
							{{amount.price}}元  {{amount.name}}
						</view>
					</view>

				</view>
			</view>
		</view>
		<view class="block">
			<view class="title">
				选择支付方式
			</view>
			<view class="content">
				<view class="pay-list">
					<!-- #ifdef APP-PLUS -->
					<view class="row" @tap="paytype='alipay'">
							<view class="left">
								<image src="../../static/img/alipay.png"></image>
							</view>
							<view class="center">
								支付宝支付
							</view>
							<view class="right">
								<radio :checked="paytype=='alipay'" color="#70b162" />
							</view>
					</view>
					<!-- #endif -->
					<view class="row" @tap="paytype='wxpay'">
							<view class="left">
								<image src="../../static/image/wechatpay.png"></image>
							</view>
							<view class="center">
								微信支付
							</view>
							<view class="right">
								<radio :checked="paytype=='wxpay'" color="#70b162" />
							</view>
					</view>
					<view class="row" @tap="paytype='blancepay'">
                    							<view class="left">
                    								<image src="../../static/image/balancepay.png"></image>
                    							</view>
                    							<view class="center">
                    								余额支付
                    							</view>
                    							<view class="right">
                    								<radio :checked="paytype=='blancepay'" color="#70b162" />
                    							</view>
                    					</view>
				</view>
			</view>
		</view>
		<view class="pay">
			<view class="btn" @tap="pay">立即升级</view>
			<!-- <view class="tis">
				点击立即充值，即代表您同意<view class="terms">
					《条款协议》
				</view>
			</view> -->
		</view>

	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';


	export default {
	 components: {
            		mallplusCopyright
            	},
		data() {

			return {
				money:0,
				inputAmount:'',//金额
				amountList:[100,200,500],//预设3个可选快捷金额
				paytype:'wxpay'//支付类型
			};
		},
		onShow(){
			this.load()
		},
		methods:{
		async	load(){
				 let params = {  };
                  let data1 = await Api.apiCall('get', Api.member.currentMember, params);
                	this.money = data1.blance;

                	let data2 = await Api.apiCall('get', Api.member.memberLevelList, params);
                	data2 = data2.filter(item => item.id<data1.memberLevelId);
                     this.amountList = data2;
			},
			async onload() {
			     let params = {  };
            		let data1 = await Api.apiCall('get', Api.index.currentMember, params);
					this.money = data1.blance;
			},
			select(amount){
				this.inputAmount = amount.id;
			},

			async pay(){
				var _this=this



                    			 let params = {memberLevelId:this.inputAmount  };
                                   	let data1 = await Api.apiCall('post', Api.member.applyMember, params);
                                         if(data1.code==200){
                                         this.$api.msg('升级成功');
                                             uni.switchTab({
                                         								url: '/pages/index/user'
                                         							});
                                         }
                                            console.log(data1)




			},


		},
		onLoad() {

		},
	}
</script>

<style lang="scss">
	.block{
		width: 94%;
		padding: 20upx 3%;
		.title{
			width: 100%;
			font-size: 34upx;
		}
		.content{
			.my{
				width: 100%;
				height: 120upx;
				display: flex;
				align-items: center;
				font-size: 30upx;
				border-bottom: solid 1upx #eee;
			}
			.amount{
				width: 100%;

				.list{
					display: flex;
					justify-content: space-between;
					padding: 20upx 0;
					.box{
						width: 30%;
						height: 120upx;
						display: flex;
						justify-content: center;
						align-items: center;
						border-radius: 10upx;
						box-shadow: 0upx 5upx 20upx rgba(0,0,0,0.05);
						font-size: 36upx;
						background-color: #f1f1f1;
						color: 333;
						&.on{
							background-color: $uni-color-success;
							color: #fff;
						}
					}
				}
				.num{
					margin-top: 10upx;
					display: flex;
					justify-content: flex-end;
					align-items: center;
					.text{
						padding-right: 10upx;
						font-size: 30upx;
					}
					.input{
						width: 28.2vw;
						border-bottom: solid 2upx #999;

						justify-content: flex-end;
						align-items: center;
						input{
							margin: 0 20upx;
							height: 60upx;
							font-size: 30upx;
							color: $uni-color-success;
							justify-content: flex-end;
							align-items: center;
						}
					}
				}
			}
			.pay-list{
				width: 100%;
				border-bottom: solid 1upx #eee;
				.row{
					width: 100%;
					height: 120upx;
					display: flex;
					align-items: center;
					.left{
						width: 100upx;
						flex-shrink: 0;
						display: flex;
						align-items: center;
						image{
							width: 80upx;
							height: 80upx;
						}
					}
					.center{
						width: 100%;
						font-size: 30upx;
					}
					.right{
						width: 100upx;
						flex-shrink: 0;
						display: flex;
						justify-content: flex-end;
					}
				}
			}
		}
	}
	.pay{
		margin-top: 20upx;
		width: 100%;
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		.btn{
			width: 70%;
			height: 80upx;
			border-radius: 80upx;
			display: flex;
			justify-content: center;
			align-items: center;
			color: #fff;
			background-color: $uni-color-success;
			box-shadow: 0upx 5upx 10upx rgba(0,0,0,0.2);
		}
		.btn-tixi{
			color: $uni-color-success;
			background: #FFFFFF;
			border:1upx solid $uni-color-success;
		}
		.tis{
			margin-top: 10upx;
			width: 100%;
			font-size: 24upx;
			display: flex;
			justify-content: center;
			align-items: baseline;
			color: #999;
			.terms{
				color: #5a9ef7;
			}
		}
	}
</style>
