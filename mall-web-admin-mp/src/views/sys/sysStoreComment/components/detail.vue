<template> 
    <el-card class="form-container" shadow="never">
        <el-form :model="sysStoreComment" :rules="rules" ref="SysStoreCommentFrom" label-width="150px">


                    <el-form-item
                            label="id"
                            prop="id">
                        <el-input v-model="sysStoreComment.id" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="名称"
                            prop="name">
                        <el-input v-model="sysStoreComment.name" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="创建时间"
                            prop="createTime">
                        <el-input v-model="sysStoreComment.createTime" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="图片"
                            prop="pic">
                        <el-input v-model="sysStoreComment.pic" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="排序"
                            prop="sort">
                        <el-input v-model="sysStoreComment.sort" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="备注"
                            prop="memo">
                        <el-input v-model="sysStoreComment.memo" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="storeId"
                            prop="storeId">
                        <el-input v-model="sysStoreComment.storeId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="memberId"
                            prop="memberId">
                        <el-input v-model="sysStoreComment.memberId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="等级"
                            prop="star">
                        <el-input v-model="sysStoreComment.star" style="width: 370px;"/>
                    </el-form-item>


            <el-form-item>
                <el-button type="primary" @click="onSubmit('SysStoreCommentFrom')">提交</el-button>
                <el-button v-if="!isEdit" @click="resetForm('SysStoreCommentFrom')">重置</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>
<script>
    import {createSysStoreComment, getSysStoreComment, updateSysStoreComment} from '@/api/sys/sysStoreComment'
    import SingleUpload from '@/components/Upload/singleUpload'

    const defaultSysStoreComment = {
        name: ''
    };
    export default {
        name: 'SysStoreCommentDetail',
        components: {SingleUpload},
        props: {
            isEdit: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
            sysStoreComment:
            Object.assign({},
        defaultSysStoreComment),
            rules: {
                name: [
                    {required: true, message: '请输入品牌名称', trigger: 'blur'},
                    {min: 2, max: 140, message: '长度在 2 到 140 个字符', trigger: 'blur'}
                ],
                    logo
            :
                [
                    {required: true, message: '请输入品牌logo', trigger: 'blur'}
                ],
                    sort
            :
                [
                    {type: 'number', message: '排序必须为数字'}
                ],
            }
        }
        },
        created() {
            if (this.isEdit) {
                getSysStoreComment(this.$route.query.id).then(response => {
                    this.sysStoreComment = response.data;
            })
                ;
            } else {
                this.sysStoreComment = Object.assign({},
            defaultSysStoreComment)
                ;
            }
        },
        methods: {
            onSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if(valid) {
                        this.$confirm('是否提交数据', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            if(this.isEdit
                    )
                        {
                            updateSysStoreComment(this.$route.query.id, this.sysStoreComment).then(response => {
                                if(response.code == 200
                        )
                            {
                                this.$refs[formName].resetFields();
                                this.$message({
                                    message: '修改成功',
                                    type: 'success',
                                    duration: 1000
                                });
                                this.$router.back();
                            }
                        else
                            {
                                this.$message({
                                    message: response.msg,
                                    type: 'error',
                                    duration: 1000
                                });
                            }

                        })
                            ;
                        }
                    else
                        {
                            createSysStoreComment(this.sysStoreComment).then(response => {
                                if(response.code == 200
                        )
                            {
                                this.$refs[formName].resetFields();
                                this.sysStoreComment = Object.assign({},
                            defaultSysStoreComment)
                                ;
                                this.$message({
                                    message: '提交成功',
                                    type: 'success',
                                    duration: 1000
                                });
                                this.$router.back();
                            }
                        else
                            {
                                this.$message({
                                    message: response.msg,
                                    type: 'error',
                                    duration: 1000
                                });
                            }

                        })
                            ;
                        }
                    })
                        ;

                    } else {
                        this.$message({
                            message: '验证失败',
                            type: 'error',
                            duration: 1000
                        });
                return false;
            }
            })
                ;
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.sysStoreComment = Object.assign({},
            defaultSysStoreComment)
                ;
            }
        }
    }
</script>
<style>
</style>


