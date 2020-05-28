<template>
	<view style="text-align:center">
		<view :style="{ marginTop: udPadding + 'px', marginBottom: udPadding + 'px', marginLeft: lrPadding + 'px', marginRight: lrPadding + 'px' }">
			<u-button :type="type" :size="size">{{ btnName }}</u-button>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		type: {
			type: String,
			default: 'primary'
		},
		btnName: {
			type: String,
			default: '按钮'
		},
		size: {
			type: String,
			default: 'default'
		},
		udPadding: {
			type: String,
			default: '10'
		},
		lrPadding: {
			type: String,
			default: '10'
		},
	},
	data() {
		return {};
	},
	onLoad() {},
	methods: {}
};
</script>

<style lang="scss"></style>
