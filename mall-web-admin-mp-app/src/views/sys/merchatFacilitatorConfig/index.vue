<template>
	<div class="app-container">
		<el-card class="filter-container" shadow="never">
			<div>
				<i class="el-icon-search"></i>
				<span>筛选搜索</span>
				<el-button style="float: right" @click="searchMerchatFacilitatorConfigList()" type="primary" size="small">查询结果</el-button>
			</div>
			<div style="margin-top: 15px">
				<el-form :inline="true" :model="listQuery" size="small" label-width="140px">
					<el-form-item label="输入搜索："><el-input style="width: 203px" v-model="listQuery.keyword" placeholder="类型名称/关键字"></el-input></el-form-item>
				</el-form>
			</div>
		</el-card>
		<el-card class="operate-container" shadow="never">
			<i class="el-icon-tickets"></i>
			<span>数据列表</span>
			<el-button class="btn-add" @click="addMerchatFacilitatorConfig()" size="mini">添加</el-button>
		</el-card>
		<div class="table-container">
			<el-table ref="merchatFacilitatorConfigTable" :data="list" style="width: 100%" @selection-change="handleSelectionChange" v-loading="listLoading" border>
				<el-table-column type="selection" width="60" align="center"></el-table-column>

				<el-table-column prop="id" label="服务商信息主键">
					<template slot-scope="scope">
						{{ scope.row.id }}
					</template>
				</el-table-column>

				<el-table-column prop="apiv3key" label="用于证书解密的密钥">
					<template slot-scope="scope">
						{{ scope.row.apiv3key }}
					</template>
				</el-table-column>

				<el-table-column prop="mchId" label="商户号">
					<template slot-scope="scope">
						{{ scope.row.mchId }}
					</template>
				</el-table-column>

				<el-table-column prop="apiclientCert" label="商户证书路径">
					<template slot-scope="scope">
						{{ scope.row.apiclientCert }}
					</template>
				</el-table-column>

				<el-table-column prop="privateKeyPath" label="商户私钥路径">
					<template slot-scope="scope">
						{{ scope.row.privateKeyPath }}
					</template>
				</el-table-column>

				<el-table-column prop="apiclientCertP12" label="商户证书路径">
					<template slot-scope="scope">
						{{ scope.row.apiclientCertP12 }}
					</template>
				</el-table-column>

				<el-table-column prop="publicKeyPath" label="微信支付平台证书路径">
					<template slot-scope="scope">
						{{ scope.row.publicKeyPath }}
					</template>
				</el-table-column>

				<el-table-column prop="createBy" label="创建人">
					<template slot-scope="scope">
						{{ scope.row.createBy }}
					</template>
				</el-table-column>

				<el-table-column prop="createTime" label="创建时间">
					<template slot-scope="scope">
						{{ scope.row.createTime }}
					</template>
				</el-table-column>

				<el-table-column prop="updateBy" label="更新人">
					<template slot-scope="scope">
						{{ scope.row.updateBy }}
					</template>
				</el-table-column>

				<el-table-column prop="updateTime" label="更新时间">
					<template slot-scope="scope">
						{{ scope.row.updateTime }}
					</template>
				</el-table-column>

				<el-table-column label="操作" width="200" align="center">
					<template slot-scope="scope">
						<el-button size="mini" @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
						<el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div class="batch-operate-container"></div>
		<div class="pagination-container">
			<el-pagination
				background
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
				layout="total, sizes,prev, pager, next,jumper"
				:page-size="listQuery.pageSize"
				:page-sizes="[5, 10, 15]"
				:current-page.sync="listQuery.pageNum"
				:total="total"
			></el-pagination>
		</div>
	</div>
</template>
<script>
//将$都替换为$
import { fetchList, deleteMerchatFacilitatorConfig } from '@/api/merchat/merchatFacilitatorConfig';
import { formatDate } from '@/utils/date';

export default {
	name: 'merchatFacilitatorConfigList',
	data() {
		return {
			operates: [],
			operateType: null,
			listQuery: {
				keyword: null,
				pageNum: 1,
				pageSize: 10
			},
			list: null,
			total: null,
			listLoading: true,
			multipleSelection: []
		};
	},
	created() {
		this.getList();
	},
	filters: {
		formatCreateTime(time) {
			let date = new Date(time);
			return formatDate(date, 'yyyy-MM-dd hh:mm:ss');
		},

		formatStatus(value) {
			if (value === 1) {
				return '未开始';
			} else if (value === 2) {
				return '活动中';
			} else if (value === 3) {
				return '已结束';
			} else if (value === 4) {
				return '已失效';
			}
		}
	},
	methods: {
		getList() {
			this.listLoading = true;
			fetchList(this.listQuery).then(response => {
				this.listLoading = false;
				this.list = response.data.records;
				this.total = response.data.total;
				this.totalPage = response.data.pages;
				this.pageSize = response.data.size;
			});
		},
		handleSelectionChange(val) {
			this.multipleSelection = val;
		},
		handleUpdate(index, row) {
			this.$router.push({ path: '/merchat/updateMerchatFacilitatorConfig', query: { id: row.id } });
		},
		handleDelete(index, row) {
			this.$confirm('是否要删除该类型', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				deleteMerchatFacilitatorConfig(row.id).then(response => {
					this.$message({
						message: '删除成功',
						type: 'success',
						duration: 1000
					});
					this.getList();
				});
			});
		},

		handleSizeChange(val) {
			this.listQuery.pageNum = 1;
			this.listQuery.pageSize = val;
			this.getList();
		},
		handleCurrentChange(val) {
			this.listQuery.pageNum = val;
			this.getList();
		},
		searchMerchatFacilitatorConfigList() {
			this.listQuery.pageNum = 1;
			this.getList();
		},
		handleBatchOperate() {
			console.log(this.multipleSelection);
			if (this.multipleSelection < 1) {
				this.$message({
					message: '请选择一条记录',
					type: 'warning',
					duration: 1000
				});
				return;
			}
			let showStatus = 0;
			if (this.operateType === 'showMerchatFacilitatorConfig') {
				showStatus = 1;
			} else if (this.operateType === 'hideMerchatFacilitatorConfig') {
				showStatus = 0;
			} else {
				this.$message({
					message: '请选择批量操作类型',
					type: 'warning',
					duration: 1000
				});
				return;
			}
			let ids = [];
			for (let i = 0; i < this.multipleSelection.length; i++) {
				ids.push(this.multipleSelection[i].id);
			}
			let data = new URLSearchParams();
			data.append('ids', ids);
			data.append('showStatus', showStatus);
			updateShowStatus(data).then(response => {
				this.getList();
				this.$message({
					message: '修改成功',
					type: 'success',
					duration: 1000
				});
			});
		},
		addMerchatFacilitatorConfig() {
			//手动将router,改成$router
			this.$router.push({ path: '/merchat/addMerchatFacilitatorConfig' });
		}
	}
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped></style>
