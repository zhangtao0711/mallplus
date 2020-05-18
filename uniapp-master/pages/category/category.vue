<template>
	<view class="content" style="display: flex;flex-direction: column;">
		<!-- #ifdef MP-WEIXIN -->
		<view style="width: 100%;position: fixed;top: 0;z-index: 99;background: #FFFFFF;" :style="{'height':statusBarHeight+'px' }"></view>
		<view class="getPosition" style="height: 90upx;position: fixed;z-index: 99;width: 100%" :style="{'top':statusBarHeight+'px' }">
			<text>分类</text>
		</view>
		<view class="" @click="search()" style="background: #FFFFFF;height: 80upx;display: flex;justify-content: center;align-items: center;" :style="[{'margin-top': statusBarHeight+45+'px'}]">
			<input class="" type="text" value="" placeholder="输入关键字搜索" style="font-size: 28upx;background: #F5F5F5;height: 60upx;width: 90%;border-radius: 50upx;text-align: center;" />
		</view>
		<!-- 占位 -->
		<!-- <view class="" :style="[{'height': statusBarHeight+45+'px'}]"></view> -->
		<!-- #endif -->

		<!-- <view class="" style="background: #FFFFFF;height: 80upx;display: flex;align-items: center;justify-content: center;">
			<view style="display: flex;align-items: center;justify-content: center; height:60upx;width: 90%;background: #F5F5F5;border-radius: 50upx;">
				<input type="text" value="" placeholder="输入关键字搜索" class="input" style="font-size: 28upx;text-align: center;"/>
			</view>
		</view> -->
		<view class="" :style="{height: height + 'px'}" style="display: flex;flex-direction: row;">
			<scroll-view scroll-y class="left-aside">
				<view v-for="item in flist" :key="item.id" class="f-item b-b" :class="{ active: item.id === currentId }" @click="tabtap(item)">{{ item.name }}</view>
			</scroll-view>
			<scroll-view scroll-with-animation scroll-y class="right-aside" @scroll="asideScroll" :scroll-top="tabScrollTop">
				<view v-for="item in slist" :key="item.id" class="s-list" :id="'main-' + item.id">
					<text class="s-item" @click="navToList(item.id)">{{ item.name }}</text>
					<view class="t-list">
						<view @click="navToDetailPage(titem)" v-if="titem.pid === item.id" class="t-item" v-for="titem in tlist" :key="titem.id">
							<image :src="titem.pic"></image>
						</view>
					</view>
				</view>
			</scroll-view>
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
			sizeCalcState: false,
			tabScrollTop: 0,
			currentId: 1,
			flist: [],
			slist: [],
			tlist: [],

			height: '',
			statusBarHeight: '',
		};
	},
	onLoad() {
		this.statusBarHeight = uni.getSystemInfoSync().statusBarHeight
		this.height = uni.getSystemInfoSync().windowHeight - (this.statusBarHeight+80)

		this.loadData();
	},
	methods: {
		 loadData() {
			
			let list = this.$db.get('categoryList' )
			//await this.$api.json('cateList');
			if(list){
				list.forEach(item => {
				
					if (item.level==0) {
						this.flist.push(item); //pid为父级id, 没有pid或者pid=0是一级分类
					} else if (item.level==1) {
						this.slist.push(item); //没有价格的是2级分类
					} else {
						this.tlist.push(item); //3级分类
					}
				});
			}
			
		},
		//一级分类点击
		tabtap(item) {
			if (!this.sizeCalcState) {
				this.calcSize();
			}

			this.currentId = item.id;
			console.log(item.id);
			let index = this.slist.findIndex(sitem => sitem.pid === item.id);
			if(index>=0){
			 this.tabScrollTop = this.slist[index].top;
			}

		},
		//右侧栏滚动
		asideScroll(e) {
			if (!this.sizeCalcState) {
				this.calcSize();
			}
			let scrollTop = e.detail.scrollTop;
			let tabs = this.slist.filter(item => item.top <= scrollTop).reverse();
			if (tabs.length > 0) {
				this.currentId = tabs[0].pid;
			}
		},
		//计算右侧栏每个tab的高度等信息
		calcSize() {
			let h = 0;
			this.slist.forEach(item => {
				let view = uni.createSelectorQuery().select('#main-' + item.id);
				view.fields(
					{
						size: true
					},
					data => {
						item.top = h;
						h += data.height;
						item.bottom = h;
					}
				).exec();
			});
			this.sizeCalcState = true;
		},
		navToList(sid) {
			uni.navigateTo({
				url: `../../pagesA/product/list?sid=${sid}`
			});
		},
		//详情页
		navToDetailPage(item) {
			//测试数据没有写id，用title代替
			let id = item.id;
			uni.navigateTo({
				url: `../../pagesA/product/product?id=${id}`
			});
		},
	}
};
</script>

<style lang="scss">
page,
.content {
	height: 100%;
	background-color: #f8f8f8;
}

.content {
	display: flex;
}
.left-aside {
	flex-shrink: 0;
	width: 200upx;
	height: 100%;
	background-color: #fff;
}
.f-item {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
	height: 100upx;
	font-size: 28upx;
	color: $font-color-base;
	position: relative;
	&.active {
		color: $base-color;
		background: #f8f8f8;
		&:before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			height: 36upx;
			width: 8upx;
			background-color: $base-color;
			border-radius: 0 4px 4px 0;
			opacity: 0.8;
		}
	}
}

.right-aside {
	flex: 1;
	overflow: hidden;
	padding-left: 20upx;
}
.s-item {
	display: flex;
	align-items: center;
	height: 70upx;
	padding-top: 8upx;
	font-size: 28upx;
	color: $font-color-dark;
}
.t-list {
	display: flex;
	flex-wrap: wrap;
	width: 100%;
	background: #fff;
	padding-top: 12upx;
	&:after {
		content: '';
		flex: 99;
		height: 0;
	}
}
.t-item {
	flex-shrink: 0;
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;
	width: 176upx;
	font-size: 26upx;
	color: #666;
	padding-bottom: 20upx;

	image {
		width: 140upx;
		height: 140upx;
	}
}
.getPosition{
		height: 100upx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 32upx;
		background-color: #FFF;
	}
</style>
