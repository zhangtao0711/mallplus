<template>
	<view class="video bottom-cell-group" >
		<video :src="jdata.options.video_link" :poster="jdata.options.video_img" controls></video>
	</view>
</template>

<script>
export default {
	name: "jshopvideo",
	props: {
		jdata:{
			type: Object,
			required: true,
		}
	},
	onLoad(){
		
	},
	methods: {
		
	}
}
</script>

<style>
.video video{
	width: 100%;
	min-height: 200upx;
}
</style>
