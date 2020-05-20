<template> 
    <el-card class="form-container" shadow="never">
        <el-form :model="umsMemberTag" :rules="rules" ref="UmsMemberTagFrom" label-width="150px">
                    <el-form-item
                            label="名称"
                            prop="name">
                        <el-input v-model="umsMemberTag.name" style="width: 370px;"/>
                    </el-form-item>

                    <el-form-item label="类型：">
                        <el-radio-group v-model="umsMemberTag.type">
                          <el-radio :label="1">会员标签</el-radio>
                          <el-radio :label="2" che>商品标签</el-radio>
                            <el-radio :label="3">文章标签</el-radio>
                        </el-radio-group>
                      </el-form-item>


                    <el-form-item
                            label="pic"
                            prop="图片">
                            <single-upload v-model="umsMemberTag.pic"></single-upload>

                    </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="onSubmit('UmsMemberTagFrom')">提交</el-button>
                <el-button v-if="!isEdit" @click="resetForm('UmsMemberTagFrom')">重置</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>
<script>
    import {createUmsMemberTag, getUmsMemberTag, updateUmsMemberTag} from '@/api/ums/umsMemberTag'
    import SingleUpload from '@/components/Upload/singleUpload'

    const defaultUmsMemberTag = {
        name: '',
        type:2
    };
    export default {
        name: 'UmsMemberTagDetail',
        components: {SingleUpload},
        props: {
            isEdit: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
            umsMemberTag:
            Object.assign({},
        defaultUmsMemberTag),
            rules: {
                name: [
                    {required: true, message: '请输入名称', trigger: 'blur'},
                    {min: 2, max: 140, message: '长度在 2 到 140 个字符', trigger: 'blur'}
                ],
                    type
            :
                [
                    {required: true, message: '请勾选类型', trigger: 'blur'}
                ],

            }
        }
        },
        created() {
            if (this.isEdit) {
                getUmsMemberTag(this.$route.query.id).then(response => {
                    this.umsMemberTag = response.data;
            })
                ;
            } else {
                this.umsMemberTag = Object.assign({},
            defaultUmsMemberTag)
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
                            updateUmsMemberTag(this.$route.query.id, this.umsMemberTag).then(response => {
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
                            createUmsMemberTag(this.umsMemberTag).then(response => {
                                if(response.code == 200
                        )
                            {
                                this.$refs[formName].resetFields();
                                this.umsMemberTag = Object.assign({},
                            defaultUmsMemberTag)
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
                this.umsMemberTag = Object.assign({},
            defaultUmsMemberTag)
                ;
            }
        }
    }
</script>
<style>
</style>


