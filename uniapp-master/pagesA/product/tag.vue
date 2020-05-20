<template>
	<view>

		<uni-section title="商品标签" type="line"></uni-section>
		<view class="example-body">
			<view class="tag-view" v-for="(item, index) in goodsTagList" :key="index" @click="navToList(item.id)">
				<uni-tag :text="item.name" type="success"  />
			</view>

		</view>

		<uni-section title="会员标签" type="line"></uni-section>
		<view class="example-body" >
			<view class="tag-view" v-for="(item, index) in memberTagList" :key="index" @click="navToList(item.id)">
				<uni-tag :circle="true" :text="item.name" type="warning" />
			</view>
		</view>

		<uni-section title="内容标签" type="line"></uni-section>
		<view class="example-body">
			<view class="tag-view" v-for="(item, index) in cmsTagList" :key="index" @click="navToList(item.id)">
				<uni-tag :circle="true" :inverted="inverted" :text="item.name" type="primary" @click="setInverted" />
			</view>
		</view>


	</view>
</template>

<script>
import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
	import uniTag from '@/components/uni-tag/uni-tag.vue';
	import uniSection from '@/components/uni-section/uni-section.vue'
	export default {
		components: {
			uniTag,
			uniSection
		},
		data() {
			return {
				type: 'default',
				goodsTagList:[],
				memberTagList:[],
				cmsTagList:[],
				inverted: false
			};
		},
		onLoad(options) {
        		this.loadGoodsData();
        		this.loadMemberData();
        		this.loadCmsData();
        	},
		methods: {
		    async loadGoodsData() {
        			let params = {type:2,status:1};
        			let list = await Api.apiCall('get', Api.member.memberTagList, params);
        			this.goodsTagList = list.records;
        		},
        		async loadMemberData() {
                        		let params = {type:1,status:1};
                        			let list = await Api.apiCall('get', Api.member.memberTagList, params);
                        			this.memberTagList = list.records;
                        		},
                        		async loadCmsData() {
                                        		let params = {type:3,status:1};
                                        			let list = await Api.apiCall('get', Api.member.memberTagList, params);
                                        			this.cmsTagList = list.records;
                                        		},
			setType() {
				let types = ['default', 'primary', 'success', 'warning', 'error'];
				let index = types.indexOf(this.type);
				types.splice(index, 1);
				let randomIndex = Math.floor(Math.random() * 4);
				this.type = types[randomIndex];
			},
			setInverted() {
				this.inverted = !this.inverted;
			},
			navToList(tagId) {
            			uni.navigateTo({
            				url: `../../pagesA/product/tagGoodslist?tagId=${tagId}`
            			});
            		}
		}
	};
</script>

<style>
	/* 头条小程序组件内不能引入字体 */
	/* #ifdef MP-TOUTIAO */
	@font-face {
		font-family: uniicons;
		font-weight: normal;
		font-style: normal;

	}

	/* #endif */

	/* #ifndef APP-NVUE */
	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #efeff4;
		min-height: 100%;
		height: auto;
	}

	view {
		font-size: 14px;
		line-height: inherit;
	}

	.example {
		padding: 0 15px 15px;
	}

	.example-info {
		padding: 15px;
		color: #3b4144;
		background: #ffffff;
	}

	.example-body {
		flex-direction: row;
		flex-wrap: wrap;
		justify-content: center;
		padding: 0;
		font-size: 14px;
		background-color: #ffffff;
	}

	/* #endif */
	.example {
		padding: 0 15px;
	}

	.example-info {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		padding: 15px;
		color: #3b4144;
		background-color: #ffffff;
		font-size: 14px;
		line-height: 20px;
	}

	.example-info-text {
		font-size: 14px;
		line-height: 20px;
		color: #3b4144;
	}


	.example-body {
		flex-direction: column;
		padding: 15px;
		background-color: #ffffff;
	}

	.word-btn-white {
		font-size: 18px;
		color: #FFFFFF;
	}

	.word-btn {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: center;
		border-radius: 6px;
		height: 48px;
		margin: 15px;
		background-color: #007AFF;
	}

	.word-btn--hover {
		background-color: #4ca2ff;
	}


	.example-body {
		/* #ifndef APP-PLUS-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		justify-content: flex-start;
		flex-wrap: wrap;
		padding: 20rpx;
	}

	.tag-view {
		/* #ifndef APP-PLUS-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		margin: 10rpx 15rpx;
		justify-content: center;
	}
</style>