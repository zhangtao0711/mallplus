<template>
	<view class="blank" :style="{background:jdata.options.search_bg,height:jdata.options.udPadding*2+'rpx'}"></view>
</template>

<script>
export default {
	name: "jshopblank",
	props: {
		jdata:{
			// type: Array,
			required: true,
		}
	},
	methods: {
		
	}
}
</script>

<style>
</style>
