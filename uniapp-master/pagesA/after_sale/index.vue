<template>
	<view class="content">
		<form @submit="submit" report-submit='true'>
			<view class="content-top">
				<view class="img-list cart-list">
					<checkbox-group class="cart-checkbox" @change="checkboxChange">
						<view class="cart-checkbox-item" v-for="(item, key) in orderInfo.orderItemList" :key="key">
							<label class="uni-list-cell uni-list-cell-pd">
								<view class="cart-checkbox-c">
									<checkbox v-if="item.status!=30" :value='item.id'  :checked="item.checked" color="#FF7159"/>
								</view>
								<view class="img-list-item">
									<image class="img-list-item-l little-img have-none" :src="item.productPic" mode="aspectFill"></image>
									<view class="img-list-item-r little-right">
										<view class="little-right-t">
											<view class="goods-name list-goods-name">{{item.productName}}</view>
											<view v-if="item.status==30" class="list-goods-name red-price">已申请</view>
										</view>
										<view class="goods-item-c">
											<view class="goods-buy">
												<!-- 商品规格 -->
												<view class="goods-salesvolume">
													{{item.productPrice}} x{{item.productQuantity}}
												</view>
											</view>
										</view>
									</view>
								</view>
							</label>
						</view>
					</checkbox-group>
				</view>
				<view class='cell-group margin-cell-group'>
					<view class='cell-item'>
						<view class='cell-item-hd'>
							<view class='cell-hd-title'>服务类型</view>
						</view>
						<view class='cell-item-ft'>
							<view class="uni-form-item uni-column invoice-type">
								<!-- <radio-group class="uni-list" @change="radioChange">
									<label class="uni-list-cell uni-list-cell-pd" v-for="(item,index) in type_list" :key="index">
										<view>
											<radio :id="item.name" :value="item.name" :checked="item.checked"></radio>
										</view>
										<view>
											<label class="label-2-text" :for="item.name">
												<text>{{item.value}}</text>
											</label>
										</view>
									</label>
								</radio-group> -->
								<!-- #ifndef MP-ALIPAY -->
								<radio-group class="uni-list" @change="radioChange">
									<label class="uni-list-cell uni-list-cell-pd" v-for="(item, index) in type_list" :key="index">
										<view class="invoice-type-icon">
											<radio class="a-radio" :id="item.name" :value="item.value" :checked="item.checked" :disabled="item.disabled"></radio>
										</view>
										<view class="invoice-type-c">
											<label class="label-2-text" :for="item.name">
												<text>{{item.name}}</text>
											</label>
										</view>
									</label>
								</radio-group>
								<!-- #endif -->
								<!-- #ifdef MP-ALIPAY -->
								<jhlable></jhlable>
								<!-- #endif -->
							</view>
						</view>
					</view>
					<view class='cell-item'>
						<view class='cell-item-hd'>
							<view class='cell-hd-title'>退款金额</view>
						</view>
						<view class='cell-item-ft'>
							<input class='cell-bd-input red-price' v-model="refund" :disabled="refund_input_noedit"></input>
						</view>
					</view>
				</view>
				<view class='cell-group margin-cell-group'>
					<view class='cell-item right-img'>
						<view class='cell-item-hd'>
							<view class='cell-hd-title'>上传凭证</view>
						</view>
					</view>
					<view class="">
						<view class="evaluate-c-b">
							<view class="goods-img-item" v-for="(item, key) in images" :key="key">
								<image class="del" src="/static/image/del.png" mode="" @click="delImage(item)"></image>
								<image class="" :src="item" mode="" @click="clickImg(item)"></image>
							</view>
							<view class="upload-img" v-show="isImage" @click="upImage">
								<image class="icon" src="/static/image/camera.png" mode=""></image>
								<view class="">上传照片</view>
							</view>
						</view>
					</view>
				</view>
				<view class='cell-group margin-cell-group'>
					<view class='cell-item right-img'>
						<view class='cell-item-hd'>
							<view class='cell-hd-title'>问题描述</view>
						</view>
					</view>
					<view class="cell-textarea ">
						<textarea v-model="reason" placeholder="请您在此描述问题(最多200字)" maxlength="200"/>
					</view>
				</view>
			</view>
			<view class="button-bottom">
				<button class="btn btn-b btn-square" formType="submit">提交</button>
			</view>
		</form>
	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import jhlable from '@/components/jihai-lable.vue'
export default {
    data() {
        return {
			type_list: [
			{ value: '0', name: '换货', checked: true, disabled: false },
				{ value: '1', name: '仅退款', checked: true, disabled: false },
				{ value: '2', name: '退货', checked: false, disabled:false },
				{ value: '3', name: '退货退款', checked: false, disabled:false },
			],
			orderInfo: [],
			order_id:'',
			items:[],   //退货明细
			item_ids:[],  //选择的退货
			aftersale_type:1,     //售后类型1退款，2退款退货
			refund:0,   //退款金额，等于已支付的金额减去已退款的金额
			refund_show:0,
			images:[],      //图片
			reason:'',      //原因
			image_max: 5,    //用于前台判断上传图片按钮是否显示
			refund_input_noedit: false,
			mode: 'aspectFill',
			submitStatus: false
        }
    },
	components: { jhlable },
	computed: {
		isImage() {
			let num = this.image_max - this.images.length;
			if(num > 0) {
				return true;
			}else{
				return false;
			}
		}
	},
    methods: {
		// 单选框点击切换
        radioChange: function(evt) {
			this.type_list.forEach(item => {
				if (item.value === evt.target.value) {
					item.checked = true;
					this.aftersale_type = evt.target.value;
				}else{
					item.checked = false;
				}
			});
			if(this.type_list[0].checked){
				this.refund_input_noedit = true;
			}else{
				this.refund_input_noedit = false;
			}
		},
		
		//订单商品信息
		async getOrderInfo() {

			let params = { id: this.order_id };
            this.orderInfo = await Api.apiCall('get', Api.order.orderDetail, params);
			this.refund_show=this.orderInfo.payAmount;

		},
		
		//退货商品选择
		checkboxChange (e) {
			let nums = 0;
			this.item_ids = [];
			for (var i = 0; i < e.detail.value.length; i++) {
				let k = e.detail.value[i];
				for(var j = 0; j < this.orderInfo.orderItemList.length; j++){
					if(this.orderInfo.orderItemList[j].id == k) {
						this.item_ids = this.item_ids.concat(k);
					}
				}
			}
		},

		//提交
		async submit(e) {
			this.submitStatus = true;
			/* let images = [];
			for(var i = 0; i<this.images.length; i++) {
				images = images.concat(this.images[i].image_id);
			} */

			//判断退款金额
			let reg = /^[0-9]+(.[0-9]{1,2})?$/;
			if (!reg.test(this.refund)) {
				this.$common.errorToShow('请输入正确金额');
				this.submitStatus = false;
				return false;
			} else {
				if(this.refund<0){
					this.$common.errorToShow('退款金额不能小于0');
					return false;
				}
				if (this.refund > this.refund_show) {
					this.$common.errorToShow('退款金额过大');
					this.submitStatus = false;
					return false;
				} 
			}
			
			if(!this.item_ids || this.item_ids.length==0){
				this.$common.errorToShow('请选择需要售后的商品');
				return false;
			}
			//组装数据，提交数据
			let data = {
				orderId:this.order_id,
				type: this.aftersale_type,
				items:this.item_ids,
				images:this.images,
				returnAmount: this.refund,
				desc:this.reason
			};
			
			// #ifdef MP-WEIXIN
			data['formId'] = e.detail.formId;
			// #endif

				let res = await Api.apiCall('post', Api.order.saveOmsOrderReturnApply, data);
            				if(res.code==200){
											this.$common.successToShow('提交成功', ress => {
												this.submitStatus = false;
												uni.navigateBack({
													delta: 1
												});
											});
                            				}else{
                            					this.$common.errorToShow(res.msg);
                            					this.submitStatus = false;
                            					uni.navigateBack({delta: 1});
                            				}

		},
		
		//上传图片
		upImage() {
			let num = this.image_max - this.images.length;
			if(num > 0){
				this.$otherApi.uploadFiles(res => {
				//this.$otherApi.uploadImage(num, res => {
					if(res.code==200){
						this.images=this.images.concat(res.data);
						this.$common.successToShow(res.msg);
					}else{
						this.$common.errorToShow(res.msg);
					}
				});
			}
		},

		//删除图片
		delImage(e) {
			let newImages = [];
			for(var i = 0; i < this.images.length; i++) {
				if(this.images[i].image_id != e.image_id){
					newImages.push(this.images[i]);
				}
			}
			this.images = newImages;
		},
		// 图片点击放大
		clickImg (img) {
			// 预览图片
			uni.previewImage({
				urls: img.split()
			});
		}
    },
	onLoad(e) {
		this.order_id = e.order_id;
		this.getOrderInfo();
	}
}
</script>

<style lang="scss">
	
@import '../../static/css/style.css';

.list-goods-name{
	width: 100% !important;
}
.cart-checkbox-item{
	position: relative;
}
.invoice-type .uni-list-cell{
	display: inline-block;
	font-size: 26upx;
	color: #333;
	position: relative;
	margin-left: 50upx;
}
.invoice-type .uni-list-cell>view{
	display: inline-block;
}
.invoice-type-icon{
	position: absolute;
	top: 50%;
	transform: translateY(-50%);
}
.invoice-type-c{
	margin-left: 50upx;
	line-height: 2;
}
.cell-item-ft .cell-bd-input{
	text-align: right;
	width: 500upx;
	font-size: 28upx;
}
.right-img{
	border-bottom: 0;
}
.cell-textarea{
	padding: 0 26upx 20upx;
}
.cell-textarea textarea{
	width: 100%;
	height: 200upx;
	font-size: 26upx;
	color: #333;
}
.evaluate-c-b{
	overflow: hidden;
	padding: 0 20upx;
}
.upload-img{
	width: 146upx;
	height: 146upx;
	margin: 14upx;
	text-align: center;
	color: #999999;
	font-size: 22upx;
	border: 2upx solid #E1E1E1;
	border-radius: 4upx;
	display: inline-block;
	float: left;
	padding: 24upx 0;
}
.goods-img-item{
	width: 174upx;
	height: 174upx;
	padding: 14upx;
	float: left;
	position: relative;
}
.goods-img-item:nth-child(4n){
	margin-right: 0;
}
.goods-img-item image{
	width: 100%;
	height: 100%;
}
.del{
	width: 30upx !important;
	height: 30upx !important;
	position: absolute;
	right: 0;
	top: 0;
	z-index: 999;
}

.cell-group{
  background-color: #fff;
}
.margin-cell-group{
	margin: 20upx 0;
}

/* #ifdef MP-ALIPAY */

/* #endif */
</style>
