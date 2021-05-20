<template>
    <div>
        <Bread :breaddata="breaddata"></Bread>
        <div>
            <el-tabs type="border-card">
                <el-tab-pane v-for="(item,index) in tabsList" :key="index">
                    <span slot="label">{{item}}</span>
                     <el-form ref="form" :model="form" label-width="80px">

                        <el-form-item label="消息内容：">
                            <el-input type="textarea"style="width:30%" v-model="form.desc" placeholder="请输入内容"></el-input>
                        </el-form-item>
                        <el-form-item label="站内发送：">
                            <el-radio-group v-model="form.resource">
                                <el-radio label="开启" value="0">开启</el-radio>
                                <el-radio label="关闭" value="1">关闭</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item>
                            <!--<el-button style="margin-top: 20px">取消</el-button>-->
                            <el-button type="primary" @click="onSubmit" v-if="$hasPermission('sys:message:model:save')">保存</el-button>

                        </el-form-item>
                    </el-form>
                </el-tab-pane>

            </el-tabs>
        </div>
    </div>
</template>
<script>
import Bread from "@/components/bread";
export default {
  data() {
    return {
         breaddata: ["网站管理", "消息模板设置", "信息设置"],
        tabsList:['已付款','商家买家已付款','退款成功','退款失败','买家退款','取消订单'],
        form:{}
    };
  },
  methods: {
      onSubmit(){
          console.log('提交')
      }
  },
  components: { Bread },
  created() {}
};
</script>
<style scoped>
.el-textarea{width:30%;}
</style>
