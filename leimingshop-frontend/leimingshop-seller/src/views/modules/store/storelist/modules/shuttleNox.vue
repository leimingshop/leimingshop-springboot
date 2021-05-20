<template>
    <div >
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <div style="margin: 15px 0;"></div>
        <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
            <el-checkbox v-for="city in cities" :label="city" :key="city">{{city}}</el-checkbox>
        </el-checkbox-group>

         <!-- <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
            <el-checkbox v-for="city in cities" :label="city" :key="city">{{city}}</el-checkbox>
        </el-checkbox-group>


        <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
            <el-checkbox v-for="city in cities" :label="city" :key="city">{{city}}</el-checkbox>
        </el-checkbox-group> -->



        <div style="display:flex;">
            <div style="min-width:200px;max-width:500px">
                <h5>第一级</h5>
                <!-- <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange" style="display: flex;flex-direction: column;"> -->
                    <div v-for="(item,index) in dataList" @change="chooseLevel1(item)" :class="levelIndex1==index?'chooseItem':''"  :key="index" style="display:flex;cursor: pointer;">
                        <el-checkbox  :label="item.id">
                           <span> </span>
                        </el-checkbox>
                         <div @click="changeLevelData2(item,index)" style="font-size:14px">{{item.gcName}}{{item.checked}}</div>
                    </div>
                <!-- </el-checkbox-group> -->
            </div>

            <div  style="min-width:200px;max-width:500px">
                <h5>第二级</h5>
                <!-- <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange" style="display: flex;flex-direction: column;"> -->
                    <div v-for="(item,index) in levelData2"  :class="levelIndex2==index?'chooseItem':''"  :key="index" style="display:flex;cursor: pointer;">
                        <el-checkbox  :label="item.id" v-model="item.checked" :checked="checked">
                           <span> </span>
                        </el-checkbox>
                        <div @click="changeLevelData3(item,index)" style="font-size:14px">{{item.gcName}}{{item.checked}} {{typeof item.checked}}</div>
                     </div>
                <!-- </el-checkbox-group> -->
            </div>

            <div  style="min-width:200px;max-width:500px">
                <h5>第三级</h5>
                <!-- <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange" style="display: flex;flex-direction: column;"> -->
                    <div v-for="(item,index) in levelData3"   :key="index" style="display:flex;cursor: pointer;">
                        <el-checkbox  :label="item.id" v-model="item.checked"  :checked="item.checked">
                           <span> </span>
                        </el-checkbox>
                        <div style="font-size:14px">{{item.gcName}}{{item.checked}}</div>
                     </div>
                <!-- </el-checkbox-group> -->
            </div>
        </div>


    </div>
</template>

<script>
  import { allGoodsclass  } from  "@/api/api.js"
  import { format } from 'path';
    export default {
        data(){
            return {
                checked:false,
                checkAll: false,
                checkedCities: ['上海', '北京'],
                cities: ['上海', '北京', '广州', '深圳'],
                isIndeterminate: false,
                // 数据
                dataList:[
                    {
                        id:1,
                        name:1,
                        isIndeterminate:false,
                        children:[
                            {
                                id:11,
                                name:11,
                                isIndeterminate:false,
                                children:{
                                    id:111,
                                    name:222,
                                    isIndeterminate:false,
                                }
                            }
                        ],
                    },
                     {
                        id:2,
                        name:2,
                        isIndeterminate:false,
                        children:[
                            {
                                id:21,
                                name:21,
                                isIndeterminate:false,
                                children:{
                                    id:211,
                                    name:211,
                                    isIndeterminate:false,
                                }
                            }
                        ],
                    },
                ],
                levelData2:[],//第三级数据
                levelData3:[],//第二级数据
                levelIndex1:-1,
                levelIndex2:-1,
            };
        },
        components:{
          
        },
        mounted(){
            this.getAllGoodsClassFn();
        },
        methods:{
           handleCheckAllChange(val) {
                this.checkedCities = val ? cityOptions : [];
                this.isIndeterminate = false;
            },
            handleCheckedCitiesChange(value) {
                let checkedCount = value.length;
                this.checkAll = checkedCount === this.cities.length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length;
            },
            // 商品分类（三级联动）
            getAllGoodsClassFn(arguId,item){
                var obj = {}
                allGoodsclass(obj).then((res)=>{
                    console.log(res);
                    this.dataList = res.data;
                })
            },
            // 点击第一级文字
            changeLevelData2(item,index){
                this.levelIndex1 = index;
                this.levelData2 = item.children?item.children:[]
                this.levelData3 = [];
            },
            // 点击第二级文字
            changeLevelData3(item,index){
                this.levelIndex2 = index
                this.levelData3 = item.children?item.children:[]
            },
            // 勾选第一级
            chooseLevel1(item){
                let checked = true;
                if(item.checked){
                    checked = false;
                }
                item.checked = checked;
                item.children.forEach((item2,index2)=>{
                    item2.checked = checked;
                })
                this.levelData2 = [].concat(this.levelData2);
                this.levelData3 = [].concat(this.levelData3);
                console.log(this.levelData2);
                console.log(this.levelData2);
            },
            // 勾选第二级
            chooseLevel2(){

            },
            // 勾选第三级
            chooseLevel3(){

            },
           
        }
    }
</script>

<style  lang="scss" scoped>
.el-checkbox+.el-checkbox {
    margin-left: 0px !important;
}
.chooseItem{
    background: #a0ceff;
}
</style>
