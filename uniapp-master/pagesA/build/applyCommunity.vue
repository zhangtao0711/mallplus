<template>
	<view class="content">
		<view class="list-wraper logo-wraper" @click="uplodingImg(1)">
			<view class="tit">
				<text>点击上传商家logo</text>
			</view>
			<view class="input-logo" >
				<image v-if="!logImg" src="http://mp.emshop.eonfox.com/zrhzstatic/muying/shop.png" mode=""></image>
				<image v-else :src="logImg" mode=""></image>
			</view>
		</view>
		<view class="list-wraper">
			<view class="tit">
				<text>商家名称：</text>
			</view>
			<view class="input-all">
				<input class="inpt" type="text" value="" v-model="infor.name"  placeholder="请输入商家名称" />
			</view>
		</view>
		<view class="list-wraper">
			<view class="tit">
				<text>商家QQ：</text>
			</view>
			<view class="input-all">
				<input class="inpt" type="text" value="" v-model="infor.contactQq"  placeholder="请输入商家QQ" />
			</view>
		</view>
		<view class="list-wraper">
			<view class="tit">
				<text>商家手机号：</text>
			</view>
			<view class="input-all">
				<input class="inpt" type="number" maxlength="11" v-model="infor.supportPhone" value="" placeholder="请输入手机号" />
			</view>
		</view>
		<view class="list-wraper">
			<view class="tit">
				<text>商家类别：</text>
			</view>
			<view class="input-all">
				<radio-group>
					 <label class="radio" v-for="(item,index) in shopsType" :key='index'><radio  value="r1"  class="radio" @click="gain(item.type_id)"/>{{item.name}}</label>
				</radio-group>
			</view>
		</view>
		<view class="list-wraper"  @click="showMulLinkageThreePicker">
			<view class="tips_left">
				<text>{{pickerText}}</text>
			</view>
			<view class="tips_right">
				<image src="../../static/right-arrow.png" ></image>
			</view>
		</view>

		<view class="tips"  @click="goPage">
			<view class="tips_left">
				<text>{{addressName}}</text>
			</view>
			<view class="tips_right">
				<image src="../../static/right-arrow.png" ></image>
			</view>
		</view>
		<view class="h-20upx"></view>

		<view class="list-wraper ind-wraper">
			<view class="tit">
				<text>商家介绍：</text>
			</view>
			<view class="input-lg">
				<textarea class="lg-text" value=""  v-model="infor.description" placeholder="请输入..." />
			</view>
		</view>

		<view class="list-wraper logo-wraper"  @click="uplodingImg(2)">
			<view class="tit">
				<text>点击上传营业执照：</text>
			</view>
			<view class="input-Image">
				<image v-if="!BusinessImg"  src="http://mp.emshop.eonfox.com/zrhzstatic/muying/yingyezhizhao.png" mode=""></image>
				<image v-else :src="BusinessImg" mode=""></image>
			</view>
		</view>

		<view class="btn-box">
			<button class="btn" @click="commit()">提交申请</button>
		</view>
	</view>
</template>

<script>

	import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
	// #ifdef H5
	let jweixin = require('jweixin-module');
	// #endif

	export default {
		data() {
			return {
				cityPickerValueDefault: [0, 0, 1],
				themeColor: '#007AFF',
				pickerText: '选择省/市/区',
				addressName:'请选择商家地址',
				logImg:'',
				infor:{},
				BusinessImg:''  ,// 营业执照
				BusinessImgId:'',
				logImgID:'',
				shopsType:[
						{
					id: 1,
					name: "服装",
				     },
					{
						id: 2,
						name: "化妆品",
					},
					{
						id: 3,
						name: "家装",
					},
					{
						id: 4,
						name: "水果",
					},
				]
			}
		},

		onLoad(){

		},
		methods: {
			goPage(){
				var _this = this
				uni.chooseLocation({
				    success: function (res) {
						console.log('res',res)
				        console.log('位置名称：' + res.name);
						_this.addressName=res.address +res.name
						_this.infor.addressDetail = res.address +res.name
						_this.infor.addressLat=res.latitude
						_this.infor.addressLng=res.longitude
				        console.log('经度：' + res.longitude);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
				    }
				});
			},
			gain(id){
				this.infor.type_id=id
				console.log('类别id',this.infor.type_id)
			},
			// 上传图片
			uplodingImg (name) {
				this.$otherApi.uploadFiles(res => {
					if (res.code == 200) {
						console.log(res.data)
						if(name==1){
							this.logImg = res.data
							this.infor.logo=res.data
						}else{
							this.BusinessImg = res.data
							this.infor.supportName=res.data
						}
					} else {
						this.$common.errorToShow(res.msg)
					}
				})
			},
			uplodingImg1(name){
				var _this=this
				console.log(name)
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album'],
					success: (res) => {
						console.log('chooseImage success, temp path is', res.tempFilePaths[0])
						var imageSrc = res.tempFilePaths[0]
						ef.left_token(function(left_token){
							//encodeURIComponent  encodeURI
							var uploadUrl=ef.api_server_url+"?"+encodeURI('data=[["APPLICATIONIMAGEUPLOADQINIU"]]')+"&token="+left_token;
							uni.uploadFile({
								url: uploadUrl,
								filePath: imageSrc,
								fileType: 'image',
								name: 'file',
								success: (res) => {
									console.log('上传成功:',res.data)
									var imageJson=JSON.parse(res.data)
									console.log(imageJson)
									uni.showToast({
										title: '上传成功',
										icon: 'success',
										duration: 1000,
										success() {
											if(name==1){
												_this.logImg = imageSrc
												_this.infor.logo_image_id=imageJson.data[0].data.image_id
											}else{
												_this.BusinessImg = imageSrc
												_this.infor.license_image_id=imageJson.data[0].data.image_id
											}
										}
									})
								},
								fail: (err) => {
									console.log('uploadImage fail', err);
									uni.showModal({
										content: err.errMsg,
										showCancel: false
									});
								}
							});
						});
					},
					fail: (err) => {
						console.log('chooseImage fail', err)

					}
				})
			},
			async commit(){
				var _this=this
				let form = Object.assign({},_this.infor)
				let data = await Api.apiCall('post', Api.member.applyStore, form);
				console.log(data)
				if (data) {
					this.$api.msg(`商户入驻成功`);
					setTimeout(() => {
						let url = '../../pagesC/store/storeResult?id='+data.id;
						uni.navigateTo({
							url
						});
					}, 800);
				} else {
					this.$api.msg(`商户入驻失败`);
				}
			},
			onConfirm(e) {
				console.log(e);
				var label=e.label;
				this.pickerText = label;
				var labelArr=label.split('-');
				this.infor.addressProvince=labelArr[0];
				this.infor.addressCity=labelArr[1];
				this.infor.addressArea=labelArr[2];
				},

			openLocation(){
				// #ifdef H5
				jweixin.getLocation({
					type: 'wgs84',
					success: res => {
						const latitude = res.latitude;
						const longitude = res.longitude;
						jweixin.openLocation({
							latitude: latitude, // 纬度，浮点数，范围为90 ~ -90
							longitude: longitude, // 经度，浮点数，范围为180 ~ -180。
							name: '位置名', // 位置名

							scale: 16, // 地图缩放级别,整形值,范围从1~28。默认为最大
							infoUrl:"http://www.baidu.com"
						})
					},
					fail: () => {},
					complete: () => {}
				});

				// #endif
			}
		}
	}
</script>

<style>
	page{
		background: #F5F5F5;
		width: 100%;
		height: 100%;
	}
	.h-20upx{
		height: 20upx;
		display: flex;
	}
	.tips{
		display: flex;
		/* width: calc(100% - 60upx); */
		padding: 20upx;
		background-color: #FFFFFF;
	}
	.tips_left{
		width: 94%;
		font-size: 28upx;
		color: #de7d67;
	}
	.tips_right{
		width: 5%;
	}
	.tips_right image{
		width:40upx;
	    height: 40upx;
	}
	.btn{
		background-color: rgba( 242 155 135);
		color: #FFFFFF;
	}
	.input-logo{
		width: 120upx;
		height: 120upx;
		line-height: 120upx;
		text-align: center;
		font-size: 27upx;
		color: #444444;
	}
	.input-logo image{
		width: 120upx;
		height: 120upx;
	}
	.input-Image{
		width: 420upx;
		height: 320upx;
		line-height: 320upx;
		text-align: center;
		font-size: 27upx;
		border: 1upx #CCCCCC solid;
		color: #444444;
	}
	.input-Image image{
		width: 420upx;
		height: 320upx;
	}
	.radio{
		font-size: 28upx;
		transform:scale(0.7)
	}
</style>
<style scoped lang="stylus" ref="stylesheet/stylus">
	.content
		width 100%
		display flex
		flex-direction column
		color #333333
		.list-wraper
			position relative
			display flex
			align-items center
			// width calc(100% - 60upx)
			padding 20upx
			background #FFFFFF
			.tit
				flex 1.5
				text
					font-size 28upx

			.input-all
				flex 4
				.inpt
					// background red
					padding-top 6upx
					font-size 28upx

		.logo-wraper
			.input-all
				text-align right
				image
					width 100upx
					height 100upx

		.list-wraper::after
			position absolute
			content ""
			width calc(100% - 60upx)
			height 2upx
			left 30upx
			bottom 0
			background #CCCCCC
		.ind-wraper
			flex-direction column
			justify-content left
			.tit
				width 100%
			.input-lg
				width 100%
				padding 20upx 10upx
				// background red
				.lg-text
					font-size 24upx
					color #777777
		.btn-box
			width 690upx;
			padding 40upx 30upx
</style>
