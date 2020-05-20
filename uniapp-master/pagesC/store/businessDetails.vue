<template>
	<view class="content">
		<view class="head" >
			<view class="backstyle" >
			</view>
			<image :src="imgA" mode="widthFix"></image>
		</view>
		<!-- 商家内容标题部分 -->
		<view class="content-title">
			<view class="title-img"  @click="magnifyThree">
				<image :src="imgA"  mode=""></image>
			</view>
			<view class="title-font">
				<view class="fontBox">
					{{name}}
				</view>
				<view class="fontInfo">
					{{store.supportPhone}}
				</view>
			<!-- 	<view class="fontBox" >
					<uni-icon v-for="(n,ind) in star" type="star-filled"  size="15" class="star-filled" color="8e8e8e" :key="ind"></uni-icon>
				</view> -->
				<!-- <view class="fontBox">
					{{distance}}
				</view> -->
			</view>
			<view class="title-btn">
				<view v-if="store.favorite" @click="goCollect(store)">
					<text  class="yticon icon-shoucang2" style="color: #ffba00 ; font-size: 35px;"  ></text>
				</view>
				<view v-else @click="goCollect(store)">
					<text  class="yticon icon-shoucang2" style="color: #ffffff; font-size: 35px;" ></text><!-- 收藏1 取消收藏2 -->
				</view>
				<view @click="showEvaluateLayer()">
					<text  class="yticon icon-pinglun-copy" style="color: #ffffff ; font-size: 35px;"  ></text>
				</view>

			</view>
		</view>
		<view class="BigBOX" v-show="dispalyImgThree" @click="clickshowThree">
			 <image :src="imgA" mode="" ></image>
		</view>
		<view class="tab" >
			<view class="tab-item" :class="selectIndex==1||selectIndex==5?'tab-item-checked':''" @click="changTab(1)">
				商品
			</view>
			<view class="tab-item" :class="selectIndex==2?'tab-item-checked':''" @click="changTab(2)">
				分类
			</view>
			<view class="tab-item" v-if="reviewNum<99" :class="selectIndex==3?'tab-item-checked':''" @click="changTab(3)">
				评价({{pingjia.length}})
			</view>
			<view class="tab-item" v-else :class="selectIndex==3?'tab-item-checked':''" @click="changTab(3)">
				评价(99+)
			</view>
			<view class="tab-item" :class="selectIndex==4?'tab-item-checked':''" @click="changTab(4)">
				商家
			</view>
			<view class="tab-item" :class="selectIndex==4?'tab-item-checked':''" @click="changTab(5)">
            				门店
            			</view>
		</view>
		<view class="bbox">

		</view>
		<view v-show="selectIndex==1">
			<view class="goods-list">
				<view
					v-for="(item, index) in newProductList" :key="index"
					class="goods-item"
					@click="navToDetailPage(item)"
				>
					<view class="image-wrapper">
						<image :src="item.pic" mode="aspectFill"></image>
					</view>
					<text class="title clamp">{{item.name}}</text>
					<view class="price-box">
						<text class="price">{{item.price}}</text>
						<text>已售 {{item.sale}}</text>
					</view>
				</view>
			</view>
			<uni-load-more :status="loadingType"></uni-load-more>
		</view>
		<view  v-show="selectIndex==2">
			<view class="categroy" >
				<view class="categroy-item" v-if="category" v-for="(item,index) in category" :key="index" @click="toCategoryList(item.id)">

					<image :src="item.pic" mode="" class="categroy-item-img"  v-if="item.pic" ></image>
                      <image  mode="" class="categroy-item-img"  v-else src="/static/missing-face.png"  ></image>
					<text class="categroy-item-text">{{item.name}}</text>
				</view>

			</view>
		</view>
		<view  v-show="selectIndex==3">
			<!-- 商家位置 -->
			<view class="evaluateTitle" >
				<view class="evaluateTitle-left">
					<view class="evaluateTitle-leftBack">
					</view>
					店铺评价
				</view>
				<view class="evaluateTitle-right" v-if="pingjia" @click="toCommentList()">
					全部评价({{pingjia.length}})
					<image src="../../static/right-arrow.png" style="width:22upx;height:22upx;" mode="" @click.stop=""></image>
				</view>

			</view>
			<view class="evaluateContainer" v-if="pingjia && pingjia.length>0">
				<view class="evaluate-item" v-if="pingjia" v-for="(item,index) in pingjia" :key="index">
					<image class="evaluate-itemlogo" v-if="item.pic" :src="item.pic"   mode=""></image>
					<image class="evaluate-itemlogo" v-else src="/static/missing-face.png"   mode=""></image>
					<view class="evaluate-item-content">
						<view class="evaluate-item-content-user">
							<text class="title" v-if="item.name">
								{{item.name}}
							</text>
							<text v-else>未设置昵称</text>
							<text class="date">
								{{item.createTime |formatCreateTime}}
							</text>
						</view>
						<view class="evaluate-item-content-text">
							{{item.memo}}
						</view>
					</view>
				</view>

			</view>
			<view class="evaluateContainer" v-else>
				暂无评价
			</view>

		</view>
		<view  v-show="selectIndex==4">
			<!-- 商家位置 -->
			<view class="addressBox" @click="openMap()">
				<view class="addressBox-left">
					<image src="../../static/position.png" style="width:34upx;height:34upx;" mode=""></image>
					<view class="content-content">{{store.addressDetail}}</view>
				</view>
				<view class="addressBox-right">
					<image src="../../static/temp/share_wechat.png" style="width:44upx;height:44upx;" mode="" @click.stop="showContactInfo()"></image>
					<image src="../../static/iphone.png" style="width:44upx;height:44upx;" mode="" @click.stop="makePhoneCall(store.supportPhone)"></image>
				</view>

				<!-- <view class="addressFont">
					商家电话：{{phone}}
				</view> -->
			</view>
			<view class="bbox">

			</view>
			<!-- 商家风采 -->
			<view class="imgBox">
				<view class="imgBoxImg" v-show="dispaly" @click="magnifyTwo">
					<image :src="imgA" mode=""></image>
				</view>
	 			<view class="imgBoxImg" v-show="dispalyx">
					<image :src="store.supportName" mode=""></image>
				</view>
				<view class="imgBoxImg" v-for="(item,index) in sjimg" :key="index">
					<image :src="address+item" mode=""  @click="magnify(index)"></image>
				</view>
			</view>
			<view class="BigBOX" v-show="dispalyImgTwo" @click="clickshowTwo">
				 <image :src="imgB" mode="" ></image>
			</view>
			<view class="BigBOX"  @click="clickshow" v-show="dispalyImg">
				 <image :src="address+sjimg[imgIndex]" mode=""  ></image>
			</view>
		</view>
		<view v-show="selectIndex==5">
		<view class="shop-box">
			<view v-if="!shopList" style="text-align: center; color:#5C5C5C; font-size: 30upx;">暂无商家记录</view>
            			<view class="shop" v-for="(item, index) in shopList" @click="gobrand" :data-Businessid="item.id" :key="index">

            				<image :src="item.logo" mode="" class="image" width="100%" height="100%" v-if="item.logo" ></image>
                            <image  mode="" class="image" width="100%" height="100%" v-else src="/static/missing-face.png"  ></image>
            				<view class="shopText">
            					<text class="name">{{ item.storeName }}【{{item.linkman}}】</text>
            					<view class="evaluate">
            						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
            						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
            						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
            						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
            						<uni-icon type="star-filled" size="15" class="star-filled" color="8e8e8e"></uni-icon>
            						 <text class="garde" style="margin-right:10upx; color:#666666; ">{{ item.collect }}</text>
            					</view>
            					<view class="bottomInformation">
            						<text class="crux" style="margin-top: 30upx; color:#666666; overflow: hidden; display: inline-block; height: 40upx; text-overflow:ellipsis;">
            							{{ item.address }}
            						</text>
            						<text class="distance" style=" color:#666666;">{{ item.mobile }}</text>
            					</view>
            				</view>
            				</view>
            			</view>

		</view>
		<view class="mask1" v-show="contactInfoDisplay" @click="showContactInfo()">

		</view>
		<view class="contactInfo" v-show="contactInfoDisplay">
			<text class="contactInfo-title">识别二维码添加店家微信</text>
			<image :src="store.contactQrcode" mode="" style="width: 318upx;height:318upx;"></image>
			<text>{{name}}</text>
			<text>微信号{{store.contactQq}}</text>
			<text>客服电话：{{store.supportPhone}}</text>
		</view>
		<view class="mask1" v-show="evaluateLayer" @click="showEvaluateLayer()">

		</view>
		<view class="evaluateLayer" v-show="evaluateLayer">
			<text>请输入评价内容</text>
			<textarea v-model="evaluate" placeholder="请输入评价信息" />
			<view class="evaluateLayerBtn">
				<view class="cancel" @click="showEvaluateLayer()">
					取消
				</view>
				<view class="confirm" @click="submitEvaluate()">
					确定
				</view>
			</view>
		</view>

	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import { formatDate } from '@/common/date';
	import uniIcon from "@/components/uni-icon/uni-icon.vue";
	import eonfox from "@/components/eonfox/eonfox.js"
	import fns from '@/components/eonfox/fns.js';
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	// #ifdef H5
	let jweixin = require('jweixin-module');
	// #endif
	var ef = new eonfox();
	export default {
	components: {
    		mallplusCopyright
    	},
		data(){
			return {
			store:'',
				id:'',
			    name:'',
				info:'',
				license_image_id:'',
				logimg:'',
				phone:'',
				star:'',
				imgA:'',
				imgB:'',
				sjimg:[],
				address:'',
				Latitude:'',
				Longitude:'',
				BusinessAddress:'',
				dispaly:true,
				dispalyx:true,
				distance:'',
				dispalyImg:false,
				index:0,
				imgIndex:'',
				dispalyImgTwo:false,
				dispalyImgThree:false,
				contactInfoDisplay:false,//是否展示商家联系方式
				selectIndex:1,//当前选中栏
				shopLatitude:'',
				shopLongitude:'',
				loadingType: 'more', //加载更多状态
				filterIndex: 0,
				priceOrder: 0, //1 价格从低到高 2价格从高到低
				goodsList: [],
				shopList: [],
				qiniu:'',
				search:{},
				sort:[],
				page:10,
				category:null,
				loadingcategoryType: 'more', //分类加载更多状态
				categoryPage:10,//分类商品分页
				categoryType:'',//分类类型
				newProductList:[],
				evaluateLayer:false,//评价弹窗
				evaluate:'',//评价内容
				pingjia:'',//评价列表
				reviewNum:0,//评价数量
				params:{},
				pageNum: 1,
				lon:'',
				lat:'',
				favorite: false//收藏
			}
		},
filters: {
              formatCreateTime(time) {
                let date = new Date(time);
                return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
              },
            },
		methods:{
			toCommentList(){
				var _this=this;
				if(_this.id){
					uni.navigateTo({
						url: '/pagesA/commentList/commentList?type=merchant&id='+_this.id
					});
				}
				else{
					uni.showToast({
						title:"店铺id异常",
						icon:'none'
					})
				}
			},
			async submitEvaluate(){
				var _this=this;
				if(_this.evaluate.trim()==''){
					uni.showToast({
						title:"请输入评价内容",
						icon:'none'
					})
					return
				}
				else{
				let params = { storeId: this.id,memo:_this.evaluate };
                	let groupHotGoodsList = await Api.apiCall('post', Api.member.addStoreComment, params);
				if(groupHotGoodsList){
				this.storeCommentList(this.id);
                										_this.showEvaluateLayer();
                										uni.showToast({
                											title: '评价成功'
                										});
this.changTab(3);
                									}

				}
			},
			showEvaluateLayer(){
			        this.evaluate='';
				this.evaluateLayer=!this.evaluateLayer;
			},
			toCategoryList(typeid){
				this.changTab(1);
				this.categoryType=typeid;
				this.params.storeClassId=this.typeid;
				this.getNewProductList('refresh');
			},
			//详情
			navToDetailPage(item){
				// console.log(item)

				let id = item.id;
				uni.navigateTo({
					url: `../../pagesA/product/product?id=${id}`
				})
			},
			
			//加载分类商品 ，带下拉刷新和上滑加载
			async loadCateGoryData(type='add', loading) {

			},
			//唤起拨号
			makePhoneCall(phone){
				uni.makePhoneCall({phoneNumber:phone});
			},
			//展示商家联系方式
			showContactInfo(){
				this.contactInfoDisplay=!this.contactInfoDisplay;
			},
			//切换Tab
			changTab(index){
				this.selectIndex=index;
			},
			//收藏
			async goCollect(item){
				var _this=this
				_this.favorite = !this.favorite;
			let params = { objId: this.id, type: 3, name: item.name, meno1: item.logo, meno2: item.collect, meno3: item.hit };
            Api.apiCall('post', Api.goods.favoriteSave, params);

			},
				openMap1() {
                    			var _this = this;
                    			uni.chooseLocation({

                    				success: function(res) {
                    			console.log('res', res);
                                _this.addressData.longitude=res.longitude;
                                _this.addressData.latitude=res.latitude;
                                _this.addressId = 1 ;
                    					_this.addressData.province = res.address ;
                    					_this.addressData.name =  res.name;
                    					console.log('经度：' + res.longitude);
                    					console.log('详细地址：' + res.address);
                    					console.log('纬度：' + res.latitude);
                    					_this.storeList = {};
                    				}
                    			});

                    		},
			openMap(){
				var _this=this
				uni.openLocation({
					latitude: parseFloat(_this.shopLatitude),
					longitude: parseFloat(_this.shopLongitude),
					name:_this.name,
					address:_this.store.addressDetail,
					success: function () {
						console.log('success');
					}
				});

			},
			//点击获取图片的下标
			magnify:function(index){
				console.log('点击了',index);
				this.imgIndex=index;
				this.dispalyImg=true;
				this.dispalyImgTwo=false;
				this.dispalyImgThree=false;
			},
			//点击关闭图片
			clickshow(){
				this.dispalyImg=false;
			},
			magnifyTwo(){
				this.dispalyImgTwo=true;
				this.dispalyImg=false;
				this.dispalyImgThree=false;
			},
			magnifyThree(){
				this.dispalyImgThree=true;
				this.dispalyImgTwo=false;
				this.dispalyImg=false;
			},
			clickshowTwo(){
				this.dispalyImgTwo=false;
			},
			clickshowThree(){
				this.dispalyImgThree=false;
			},
			returnto() {
				uni.navigateBack({
					delta:1
				})
			},
			        async getShopList(storeId) {
            			let params = { storeId: storeId };
            			let groupHotGoodsList = await Api.apiCall('get', Api.index.shoplist, params);
            			this.shopList = groupHotGoodsList;
            		},

            		async storeCommentList(storeId) {
                                                    			let params = { storeId: storeId };
                                                    			let storeClass = await Api.apiCall('get', Api.index.storeCommentList, params);
                                                    			this.pingjia = storeClass.records;
                                                    		},
            		 async storeClassList(storeId) {
                                			let params = { storeId: storeId };
                                			let storeClass = await Api.apiCall('get', Api.index.storeClassList, params);
                                			this.category = storeClass.records;
                                		},
			/**
                		     * 获取新品上市信息
                		     */
                		    async getNewProductList(type = 'add', loading){
                		    //没有更多直接返回
                            			if (type === 'add') {
                            				if (this.loadingType === 'nomore') {
                            					return;
                            				}
                            				this.loadingType = 'loading';
                            			} else {
                            				this.loadingType = 'more';
                            			}
                            			this.params.pageNum=this.pageNum;
                            			this.params.storeId=this.id;

                		      let list = await Api.apiCall('get', Api.goods.goodsList, this.params);
            
            
                		        let goodsList = list.records;
            
                                			if (type === 'refresh') {
                                				this.newProductList = [];
                                			}
            
                    	this.newProductList = this.newProductList.concat(goodsList);
            
                                			//判断是否还有下一页，有是more  没有是nomore(测试数据判断大于20就没有了)
                                			this.loadingType = this.newProductList.length > list.total ? 'nomore' : 'more';
                                			if (type === 'refresh') {
                                				if (loading == 1) {
                                					uni.hideLoading();
                                				} else {
                                					uni.stopPullDownRefresh();
                                				}
                                			}
            
                		    },
		},
			async onLoad(ops) {

        	if(ops.id && ops.id!=undefined){
        	this.id=ops.id;
        	}else{
        	this.id=0;
        	}
                this.getNewProductList('refresh');
        		let params = { id:this.id };
        		let store = await Api.apiCall('get', Api.member.storeDetail1, params);
        		this.store=store.store;
                this.imgA=this.store.logo;
                this.name=this.store.name;

        if(ops.lat && ops.lat!=undefined){
        	this.shopLatitude=ops.lat;
       	}
        if(ops.lon && ops.lon!=undefined){
        	this.shopLongitude=ops.lon;
       	}
         this.getShopList(this.id);
        this.storeClassList(this.id);
        this.storeCommentList(this.id);
        	},

			//加载更多
                	onReachBottom() {
                		this.pageNum = this.pageNum + 1;

                		this.getNewProductList();
                	},
            	onPullDownRefresh() {
            	this.pageNum = this.pageNum + 1;
            	this.getNewProductList('refresh');


            		setTimeout(function () {
            			uni.stopPullDownRefresh();
            		}, 2000);
            	},
		components:{
			uniIcon,
			uniLoadMore
		},
	}
</script>
<style scoped lang="stylus" ref="stylesheet/stylus">
.content
	width 100%
	overflow hidden
	.title-box
		display flex
		justify-content center // 水平居中
		align-items center // 垂直居中
		flex-direction row // 左到右
		width 750upx
		padding 0 20upx
		height 100upx
		.scan
			width 40upx
			height 40upx
		.input-box
			// margin-left 20upx
			width 700upx
			height 58upx
			// border 1upx solid #d3d3d3
			border-radius 50upx
			.search
				display inline-block
				position absolute
				top 0
				left 0
			.input
				width 80%
				display inline-block
				font-size 28upx
				text-align center
				// text-align left
				line-height 100%
				height 100%
		.chat
			flex 1
	.topTitle
		width 100%
		height 80upx
		display flex
		align-items center // 垂直居中
		flex-direction row
		padding 0 20upx
		color #333
		font-size 30upx
		.allStore
			vertical-align middle
			line-height 80upx
			color #e7313a
		.discountStore
			line-height 100upx
			padding-left 20upx
		.leftBox
			width 300upx
			text-align right
	.classification
		height 80upx
		display flex
		align-items center
		justify-content center // 垂直居中
		flex-direction row
		font-size 28upx
		.typeList
			text-align center
			line-height 80upx
			border 1px solid #d3d3d3
			width 50%
			height 100%
	.place
		font-size 30upx
		height 100upx
		text-align left
		line-height 100upx
		border-bottom 1px solid #d3d3d3
		padding-left 20px
	.shop-box, width 100%
		height auto
	.shop
		height 200upx
		border-bottom 1px solid #eee
		display flex
		align-items center
		justify-content flex-start
		flex-direction row
		.image
			width 160upx
			height 160upx
			margin-left 25upx
			border-radius 50%
		.shopText
			width 500upx
			height 220upx
			margin-left 25upx
			padding-top 25upx
			.name
				color #333
				font-weight 600
				font-size 30upx
				width 100%
				height 30px
			.evaluate
				font-size 20upx
				margin-top 20upx
			.bottomInformation
				align-items center
				justify-content space-between
				flex-direction row
				width 100%
				color #333
				font-size 28upx
				.crux
					width 240upx
					display inline-block
				.distance
					width calc(100% - 250upx)
					float right
					text-align right
					margin-top 30upx
			.icoimg
				width 48upx
				height 48upx
</style>
<style>
	.content{
		font-size:28upx;
		width:100%;
		height:auto;
	}
	.head{
		width: 750upx;
		height: 300upx;
		overflow: hidden;
		position: relative;
	}
	.head image{
		width: 750upx;
		height: 180upx;
	}
	.backstyle{
		position: absolute;
		width: 100%;
		height: 100%;
		background-size: cover;
		background-color: rgba(0,0,0,0.4);
		background-repeat: no-repeat;

	}
	.content-title{
		width: 100%;
		height:auto;
		display: flex;
		margin-top: -120upx;
		position: relative;
		background-color: rgba(0,0,0,0.5);
	}
	.content-content{
		padding-left: 16upx;
	}
	.title-img{
		float:left;
		width: 130upx;
		height:130upx;
		margin-left: 30upx;
		margin-right: 30upx;
		margin-top: 10upx;
	}
	.title-img image{
		width: 130upx;
		height:130upx;
		border-radius: 6upx;
	}
	.title-font{
		width: 440upx;
		height: 150upx;
		float: left;
		margin-top: 10upx;
		color: #FFF;
	}
	.title-btn{
		width:170upx;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-right: 15upx;
	}
	.title-btn view{
		width: 70upx;
		height: 70upx;
		margin-left: 15upx;
	}
	.fontBox{
		width: 100%;
		height: 50upx;
		line-height: 50upx;
		font-size: 36upx;
	}
	.fontInfo{
		font-size: 24upx;
	}
	.bbox{
		width: 100%;
		height:20upx;
		background-color: #e4e2e2;
	}
	.addressBox{
		width: 690upx;
		height:auto;
		padding-top: 20upx;
		padding-bottom: 20upx;
		padding-left:30upx;
		display: flex;
		justify-content: space-between;
	}
	.addressBox-left{
		display: flex;
	}
	.addressBox-right{
		display: flex;
		width:148upx;
		justify-content: space-between;
	}
	.imgBox{
		width: 750upx;
		padding:0 30upx;
		height: auto;
		display: flex;
		flex-wrap: wrap;
	}
	.imgBoxImg{
		margin-top:40upx;
		margin-left: 5upx;
		margin-right: 5upx;
		width: 220upx;
		height:220upx;
	}
	.imgBoxImg image{
		width: 220upx;
		height:220upx;
		border-radius: 10upx;
	}
	.BigBOX{
		position:fixed;
		float: left;
		top:0upx;
		width:100%;
		height:1600upx;
		background-color: #101010;
		z-index:5;
	}
	.BigBOX image{
		float: left;
		width: 100%;
		height: 600upx;
		margin-top: 200upx;
	}
	.tab{
		height:90upx;
		display: flex;
		justify-content: space-between;
	}
	.tab-item{
		width: 25%;
		height:90upx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.tab-item-checked{
		border-bottom:solid 4upx #F8C6B5 ;
	}
	.mask1 {
		position: fixed;
		top:0;
		left:0;
		z-index:4;
		width:100%;
		height:100vh;
		background:rgba(0,0,0,0.4);
	}
	.contactInfo{
		z-index:5;
		position: fixed;
		width:500upx;
		height:570upx;
		background-color: #fff;
		border-radius:10upx ;
		top:25vh;
		left: 125upx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-around;
		font-size: 28upx;
	}

	.contactInfo-title{

		font-size: 32upx;
		font-weight: 500;
	}
	.evaluateTitle{
		display: flex;
		width: 730upx;
		height: 80upx;
		align-items: center;
		justify-content: space-between;
	}
	.evaluateTitle-left{
		font-size: 32upx;
		align-items: center;
		display: flex;
	}
	.evaluateTitle-leftBack{
		width:20upx;
		height: 34upx;
		background-color:#F8C6B5 ;
		margin-right: 20upx;
	}
	.evaluateTitle-right{
		display: flex;
		font-size: 24upx;
		align-items: center;
	}
	.evaluateContainer{
		width: 710upx;
		padding-left: 40upx;
	}
	.evaluate-item{
		border-bottom: solid 2upx #BFBFBF;
		display: flex;
		padding: 20upx 0;
	}
	.evaluate-itemlogo{
		width: 80upx;
		height: 80upx;
		border-radius: 40upx;
	}
	.evaluate-item-content{
		width:590upx;
		padding-left: 20upx;
		font-size: 28upx;
	}
	.evaluate-item-content-user{
		display: flex;
		flex-direction: column;
		height: 80upx;
		justify-content: space-between;
	}
	.evaluate-item-content-user .title{
		font-size: 28upx;
	}
	.evaluate-item-content-user .date{
		font-size: 24upx;
	}
	.evaluate-item-content-text{
		padding-top: 20upx;
	}

	.evaluateLayer{
		z-index:5;
		position: fixed;
		width:600upx;
		height:570upx;
		background-color: #fff;
		border-radius:10upx ;
		top:25vh;
		left: 75upx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-around;
		font-size: 28upx;
	}
	.evaluateLayer text{
		margin-top: 20upx;
	}
	.evaluateLayer textarea{
		width: 560upx;
		padding: 20upx;
		border: solid 1upx #111A34;
		border-radius: 10upx;
	}
	.evaluateLayerBtn{
		width: 560upx;
		display: flex;
		justify-content:space-around;
	}
	.evaluateLayerBtn .confirm{
		width: 200upx;
		height: 80upx;
		background-color: #5193ff;
		color: #FFF;
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 10upx;
	}
	.evaluateLayerBtn .cancel{
		width: 200upx;
		height: 80upx;
		background-color: #cccccc;
		color: #FFF;
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 10upx;
	}
	.categroy{
		width:750upx;
		padding: 20upx 20upx;
		display:flex;
		flex-wrap: wrap;
	}
	.categroy-item{
		width: 174.5upx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		margin-top: 50upx;
	}
	.categroy-item-img{
		width: 80upx;
		height:80upx;
	}
	.categroy-item-text{
		margin-top: 20upx;
	}


</style>
<style lang="scss">
	/* 商品列表 */
	.goods-list{
		display:flex;
		flex-wrap:wrap;
		padding: 30upx 30upx 0;
		background: #fff;
		.goods-item{
			display:flex;
			flex-direction: column;
			width: 48%;
			padding-bottom: 40upx;
			&:nth-child(2n+1){
				margin-right: 4%;
			}
		}
		.image-wrapper{
			width: 100%;
			height: 330upx;
			border-radius: 3px;
			overflow: hidden;
			image{
				width: 100%;
				height: 100%;
				opacity: 1;
			}
		}
		.title{
			font-size: $font-lg;
			color: $font-color-dark;
			line-height: 80upx;
		}
		.price-box{
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding-right: 10upx;
			font-size: 24upx;
			color: $font-color-light;
		}
		.price{
			font-size: $font-lg;
			color: $uni-color-primary;
			line-height: 1;
			&:before{
				content: '￥';
				font-size: 26upx;
			}
		}
	}
</style>
