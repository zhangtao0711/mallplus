<template>
	<view class="content"><diyIndex :data="data"></diyIndex></view>
</template>

<script>
import diyIndex from '../diy/index.vue';
export default {
	components: {
		diyIndex
	},
	data() {
		return {
			data: ''
		};
	},
	onLoad() {
		this.getList();
		
	},
	methods: {
		getList() {
			uni.request({
				url: 'http://192.168.1.122:8085/sms/smsDiyPage/list',
				header: {
					Authorization:
						'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE1OTA0NTM5ODE3NjEsImV4cCI6MTU5MTA1ODc4MX0.H0smE7P0aUwpoXjo8HNqzK28lsJfVPp9hXk-pk2IxRCH4riuM0sF-gYlN0UjazNbzDYTrqJqqg_K-8PNZ4j2vg'
				},
				data: {
					pageNum: 1,
					pageSize: 10,
					title: '我的'
				},
				method: 'GET',
				success: res => {
					// var data = JSON.parse(res.data.data.records[0].datas)
					this.data = JSON.parse(res.data.data.records[0].datas).currentTemplate
					uni.setNavigationBarTitle({
						title: res.data.data.records[0].title
					});
				}
			});
		},
	}
};
</script>

<style lang="scss"></style>
