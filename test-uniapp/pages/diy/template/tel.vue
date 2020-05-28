<template>
	<view>
		<tplMember></tplMember>
		<tplMyorder></tplMyorder>
		<tplCell></tplCell>
		<tplBtn></tplBtn>
	</view>
</template>
<!-- :tpe="type" :btnName="btnName" :size="size" :udPadding="udPadding" :lrPadding="lrPadding" -->
 <!-- :cellList="cellList" -->
<script>
import tplBtn from './tpl_btn.vue';
import tplMember from './tpl_member.vue';
import tplMyorder from './tpl_myorder.vue';
import tplCell from './tpl_cell.vue';
export default {
	components: {
		tplBtn,
		tplMember,
		tplMyorder,
		tplCell
	},
	data() {
		return {};
	},
	onLoad() {},
	methods: {}
};
</script>

<style></style>
