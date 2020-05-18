<template>
		<view class="container">
			<view class="list-cell b-b m-t" @click="upImg" :hover-stay-time="50" style="display: flex;align-items: center;">
				<text class="cell-tit">我的头像</text>
				<view class="" style="display: flex;align-items: center;justify-content: center;height: 80upx;line-height: 80upx;">
					<image v-if="userInfos && userInfos.icon" :src="userInfos.icon" style="border-radius: 50%;width: 100upx;height: 100upx;"></image>
					<text class="cell-more yticon icon-you"></text>
				</view>
			</view>

			<view class="list-cell b-b m-t" @click="inputShowModal('nickname')" hover-class="cell-hover" :hover-stay-time="50">
				<text class="cell-tit" v-if="userInfos && userInfos.nickname">修改昵称:{{userInfos.nickname|| ''}}</text>
				<text class="cell-more yticon icon-you"></text>
			</view>
			<view class="list-cell b-b" @click="genderShowModal" hover-class="cell-hover" :hover-stay-time="50">
				<text class="cell-tit">修改性别</text>
				<text class="cell-more yticon icon-you"></text>
			</view>

			<neil-modal :show="inputShow" @close="cancel" title="编辑" @cancel="cancel" @confirm="confirm">
				<input v-model="inputContent" style="margin:20upx" placeholder="请输入..." />
			</neil-modal>
			<neil-modal :show="genderShow" @close="cancel" title="选择性别" @cancel="cancel" @confirm="confirmGender">
				<view>
					<radio-group style="text-align:center" @change="genderRadioChange">
						<label v-for="(item, index) in genders" :key="item.value">
							<radio :value="item.value" :checked="index === current" style="margin:10upx" />
							{{ item.name }}
						</label>
					</radio-group>
				</view>
			</neil-modal>
		</view>
</template>

<script>
	import mallplusCopyright from '@/components/mall-copyright/mallplusCopyright.vue';
import Api from '@/common/api';
import neilModal from '@/components/neil-modal.vue';
import { mapState, mapMutations } from 'vuex';
export default {
	components: {
		neilModal
	},
	data() {
		return {
			inputShow: false,
			feild: undefined,
			inputContent: '',
			genderShow: false,
			gender: undefined,
			genders: [{ name: '保密', value: 0 }, { name: '男', value: 1 }, { name: '女', value: 2 }],

			userInfos: {},
			sourceTypeIndex: 0,
			sourceType: ['拍照', '相册', '拍照或相册'],
			sizeTypeIndex: 0,
			sizeType: ['压缩', '原图', '压缩或原图'],
		};
	},
	async onShow() {
		 let params = {  };
		 let data = await Api.apiCall('get', Api.index.userSampleInfo, params);
		 this.userInfos=data;
		 console.log(this.userInfos);
	},
	async onLoad() {
		let params = {  };
		let data = await Api.apiCall('get', Api.index.userSampleInfo, params);
		this.userInfos=data;
		console.log(this.userInfos);
	},
	methods: {
		upImg(){
			this.$otherApi.uploadFiles(res => {
				if (res.code == 200) {
					this.userInfos.icon = res.data;
					let obj = {
						id:1,
						icon: res.data
					};
					Api.apiCall('post', Api.member.updateMember, obj);
					that.$api.msg('修改成功');
				} else {
					this.$common.errorToShow(res.msg)
				}
			})

		},
		cancel() {
			this.inputShow = false;
			this.genderShow = false;
		},
		inputShowModal(feild) {
			this.feild = feild;
			this.inputShow = true;
			this.inputContent = '';
		},
		genderShowModal() {
			this.genderShow = true;
			this.gender = undefined;
		},
		confirm() {
			const that = this;
			if (!that.inputContent) {
				that.$api.msg('输入不能为空');
				return;
			}
			let obj = {	id:1};
			obj[that.feild] = that.inputContent;
			Api.apiCall('post', Api.member.updateMember, obj);
			that.$api.msg('修改成功');
			that.userInfos[that.feild] = that.inputContent

		},
		genderRadioChange(e) {
			this.gender = parseInt(e.detail.value);
		},
		confirmGender() {
			const that = this;
			if (that.gender === undefined) {
				that.$api.msg('请选择性别');
				return;
			}
			let obj = {
				id:1,
				gender: that.gender
			};
			Api.apiCall('post', Api.member.updateMember, obj);
			that.$api.msg('修改成功');

		}
	}
};
</script>

<style lang="scss">
page {
	background: $page-color-base;
}

page {
	background: $page-color-base;
}
.list-cell {
	display: flex;
	align-items: baseline;
	padding: 20upx;
	line-height: 60upx;
	position: relative;
	background: #fff;
	justify-content: center;
	&.log-out-btn {
		margin-top: 40upx;
		.cell-tit {
			color: $uni-color-primary;
			text-align: center;
			margin-right: 0;
		}
	}
	&.cell-hover {
		background: #fafafa;
	}
	&.b-b:after {
		left: 30upx;
	}
	&.m-t {
		margin-top: 16upx;
	}
	.cell-more {
		align-self: baseline;
		font-size: $font-lg;
		color: $font-color-light;
		margin-left: 10upx;
	}
	.cell-tit {
		flex: 1;
		font-size: $font-base + 2upx;
		color: $font-color-dark;
		margin-right: 10upx;
	}
	.cell-tip {
		font-size: $font-base;
		color: $font-color-light;
	}
	switch {
		transform: translateX(16upx) scale(0.84);
	}
}
</style>
