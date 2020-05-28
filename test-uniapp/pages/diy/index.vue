<template>
	<view>
		<!-- <view v-for="item in data"> -->
			<tpl></tpl>
		<!-- </view> -->
	</view>
</template>

<script>
import tpl from './template/tel.vue'
export default {
	props: {
		data: {
			type: Array,
			default: []
		}
	},
	components: {
		tpl
	},
	data() {
		return {};
	},
	onLoad() {},
	methods: {
		get(){
			this.$emit("getData");
		}
	}
};
</script>

<style></style>
