<template> 
    <el-card class="form-container" shadow="never">
        <el-form :model="smsWaterPage" :rules="rules" ref="SmsWaterPageFrom" label-width="150px">


                    <el-form-item
                            label="主键"
                            prop="id">
                        <el-input v-model="smsWaterPage.id" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="水费"
                            prop="price">
                        <el-input v-model="smsWaterPage.price" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="名称"
                            prop="name">
                        <el-input v-model="smsWaterPage.name" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="经销商id"
                            prop="dealerId">
                        <el-input v-model="smsWaterPage.dealerId" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="创建时间"
                            prop="createTime">
                        <el-input v-model="smsWaterPage.createTime" style="width: 370px;"/>
                    </el-form-item>


                    <el-form-item
                            label="所属店铺"
                            prop="storeId">
                        <el-input v-model="smsWaterPage.storeId" style="width: 370px;"/>
                    </el-form-item>


            <el-form-item>
                <el-button type="primary" @click="onSubmit('SmsWaterPageFrom')">提交</el-button>
                <el-button v-if="!isEdit" @click="resetForm('SmsWaterPageFrom')">重置</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>
<script>
    import {createSmsWaterPage, getSmsWaterPage, updateSmsWaterPage} from '@/api/sms/smsWaterPage'
    import SingleUpload from '@/components/Upload/singleUpload'

    const defaultSmsWaterPage = {
        name: ''
    };
    export default {
        name: 'SmsWaterPageDetail',
        components: {SingleUpload},
        props: {
            isEdit: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
            smsWaterPage:
            Object.assign({},
        defaultSmsWaterPage),
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
                getSmsWaterPage(this.$route.query.id).then(response => {
                    this.smsWaterPage = response.data;
            })
                ;
            } else {
                this.smsWaterPage = Object.assign({},
            defaultSmsWaterPage)
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
                            updateSmsWaterPage(this.$route.query.id, this.smsWaterPage).then(response => {
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
                            createSmsWaterPage(this.smsWaterPage).then(response => {
                                if(response.code == 200
                        )
                            {
                                this.$refs[formName].resetFields();
                                this.smsWaterPage = Object.assign({},
                            defaultSmsWaterPage)
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
                this.smsWaterPage = Object.assign({},
            defaultSmsWaterPage)
                ;
            }
        }
    }
</script>
<style>
</style>


