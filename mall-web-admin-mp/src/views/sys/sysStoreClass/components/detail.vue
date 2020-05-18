<template> 
    <el-card class="form-container" shadow="never">
        <el-form :model="sysStoreClass" :rules="rules" ref="SysStoreClassFrom" label-width="150px">



                    <el-form-item
                            label="名称"
                            prop="name">
                        <el-input v-model="sysStoreClass.name" style="width: 370px;"/>
                    </el-form-item>




                    <el-form-item
                            label="图片"
                            prop="pic">
                        <single-upload v-model="sysStoreClass.pic"></single-upload>
                    </el-form-item>


                    <el-form-item
                            label="排序"
                            prop="sort">
                        <el-input v-model="sysStoreClass.sort" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="备注"
                            prop="memo">
                        <el-input v-model="sysStoreClass.memo" style="width: 370px;"/>
                    </el-form-item>



            <el-form-item>
                <el-button type="primary" @click="onSubmit('SysStoreClassFrom')">提交</el-button>
                <el-button v-if="!isEdit" @click="resetForm('SysStoreClassFrom')">重置</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>
<script>
    import {createSysStoreClass, getSysStoreClass, updateSysStoreClass} from '@/api/sys/sysStoreClass'
    import SingleUpload from '@/components/Upload/singleUpload'

    const defaultSysStoreClass = {
        name: ''
    };
    export default {
        name: 'SysStoreClassDetail',
        components: {SingleUpload},
        props: {
            isEdit: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
            sysStoreClass:
            Object.assign({},
        defaultSysStoreClass),
            rules: {
                name: [
                    {required: true, message: '请输入品牌名称', trigger: 'blur'},
                    {min: 2, max: 140, message: '长度在 2 到 140 个字符', trigger: 'blur'}
                ],


            }
        }
        },
        created() {
            if (this.isEdit) {
                getSysStoreClass(this.$route.query.id).then(response => {
                    this.sysStoreClass = response.data;
            })
                ;
            } else {
                this.sysStoreClass = Object.assign({},
            defaultSysStoreClass)
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
                            updateSysStoreClass(this.$route.query.id, this.sysStoreClass).then(response => {
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
                            createSysStoreClass(this.sysStoreClass).then(response => {
                                if(response.code == 200
                        )
                            {
                                this.$refs[formName].resetFields();
                                this.sysStoreClass = Object.assign({},
                            defaultSysStoreClass)
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
                this.sysStoreClass = Object.assign({},
            defaultSysStoreClass)
                ;
            }
        }
    }
</script>
<style>
</style>


