<template> 
    <div class="app-container">
        <el-card class="filter-container" shadow="never">
            <div>
                <i class="el-icon-search"></i>
                <span>筛选搜索</span>
                <el-button
                        style="float: right"
                        @click="searchUmsMemberTagList()"
                        type="primary"
                        size="small">
                    查询结果
                </el-button>
            </div>
            <div style="margin-top: 15px">
                <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
                    <el-form-item label="输入搜索：">
                        <el-input style="width: 203px" v-model="listQuery.keyword" placeholder="类型名称/关键字"></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
        <el-card class="operate-container" shadow="never">
            <i class="el-icon-tickets"></i>
            <span>数据列表</span>
            <el-button
                    class="btn-add"
                    @click="addUmsMemberTag()"
                    size="mini">
                添加
            </el-button>
        </el-card>
        <div class="table-container">
            <el-table ref="umsMemberTagTable"
                      :data="list"
                      style="width: 100%"
                      @selection-change="handleSelectionChange"
                      v-loading="listLoading"
                      border>
                <el-table-column type="selection" width="60" align="center"></el-table-column>



                            <el-table-column prop="id"
                                             label="编号">
                                <template slot-scope="scope">
                                    {{scope.row.id }}
                                </template>
                            </el-table-column>


                            <el-table-column prop="name"
                                             label="名称">
                                <template slot-scope="scope">
                                    {{scope.row.name }}
                                </template>
                            </el-table-column>


                            <el-table-column prop="type"
                                             label="类型">
                                <template slot-scope="scope">
                                    {{scope.row.type |formatTagType}}
                                </template>
                            </el-table-column>


                            <el-table-column prop="genType"
                                             label="方式">
                                <template slot-scope="scope">
                                    {{scope.row.genType |formatGenType}}
                                </template>
                            </el-table-column>



                            <el-table-column prop="pic"
                                             label="图片">
                               <template slot-scope="scope"><img style="height: 80px" :src="scope.row.pic"></template>
                            </el-table-column>

                            <el-table-column label="是否显示" width="100" align="center">
                              <template slot-scope="scope">
                                <el-switch
                                  @change="handleShowStatusChange(scope.$index, scope.row)"
                                  :active-value="1"
                                  :inactive-value="0"
                                  v-model="scope.row.status">
                                </el-switch>
                              </template>
                            </el-table-column>
                            <el-table-column prop="createTime"
                                             label="创建时间">
                                <template slot-scope="scope">
                                    {{scope.row.createTime |formatCreateTime}}
                                </template>
                            </el-table-column>

                <el-table-column label="操作"  align="center">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="handleUpdate(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="batch-operate-container">

        </div>
        <div class="pagination-container">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    layout="total, sizes,prev, pager, next,jumper"
                    :page-size="listQuery.pageSize"
                    :page-sizes="[10,15,50]"
                    :current-page.sync="listQuery.pageNum"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>
<script>
    //将$都替换为$
    import {fetchList, deleteUmsMemberTag,updateShowStatus} from '@/api/ums/umsMemberTag'
    import {formatDate} from '@/utils/date';

    export default {
        name: 'umsMemberTagList',
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
            }
        },
        created() {
            this.getList();
        },
        filters: {
            formatCreateTime(time) {
                let date = new Date(time);
                return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
            },

            formatTagType(value) {
                if (value === 1) {
                    return '会员标签';
                } else if (value === 2) {
                    return '商品标签';
                } else if (value === 3) {
                    return '文章标签';
                }
            },
             formatGenType(value) {
                            if (value === 1) {
                                return '自动标签';
                            } else if (value === 2) {
                                return '手动标签';
                            }
                        },
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
            })
                ;

            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleUpdate(index, row) {
                this.$router.push(
                    {path: '/setting/updateUmsMemberTag', query: {id: row.id}})
            },
            handleDelete(index, row) {
                this.$confirm('是否要删除该类型', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteUmsMemberTag(row.id
            ).
                then(response => {
                    this.$message({
                        message: '删除成功',
                        type: 'success',
                        duration: 1000
                    });
                this.getList();
            })
                ;

            })
                ;

            },

 handleShowStatusChange(index, row) {
        let data = new URLSearchParams();
        ;
        data.append("ids", row.id);
        if (row.status === 0) {
                    row.status = 1;
                  } else {
                    row.status = 0;
                  }
        data.append("status", row.status);
        updateShowStatus(data).then(response => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000
          });
        }).catch(error => {
          if (row.status === 0) {
            row.status = 1;
          } else {
            row.status = 0;
          }
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
            searchUmsMemberTagList() {
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
                if (this.operateType === 'showUmsMemberTag') {
                    showStatus = 1;
                } else if (this.operateType === 'hideUmsMemberTag') {
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
                data.append("ids", ids);
                data.append("showStatus", showStatus);
                updateShowStatus(data).then(response => {
                    this.getList();
                this.$message({
                    message: '修改成功',
                    type: 'success',
                    duration: 1000
                });
            })
                ;
            },
            addUmsMemberTag() {
                //手动将router,改成$router
                this.$router.push({path: '/setting/addUmsMemberTag'})
            }
        }
    }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>


</style>


