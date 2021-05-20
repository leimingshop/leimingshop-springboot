<template>
    <div>

        <!-- 选择规格 -->
        <h4 style="padding-left: 50px;margin-top: 20px;">商品规格:</h4>
        <el-form-item label="商品规格:" >
            <!-- 遍历一级 -->
            <div v-for="(item,index) in specLevel_1" style="margin-bottom:28px;">
                <div> 
                    <el-checkbox v-model="item.checked"  @change.native="composeTableData(1,index)"></el-checkbox>
                    <span v-if="!item.inputVisible"  @dblclick="dbEditName(index)" class="clickSpan"> {{item.name}} </span>
                    <el-input
                        class="input-new-tag"
                        v-if="item.inputVisible"
                        v-model="item.name"
                        ref="saveTagInput"
                        size="small"
                        @blur="handleInputConfirm(index)"
                    ></el-input>
                </div>
                <!-- 遍历二级 -->
                <span v-for="(item2,index2) in specLevel_2[index]" style="margin-left:20px;">
                    <el-checkbox v-model="item2.checked" @change.native="composeTableData(2,index,index2)"></el-checkbox>
                    
                    <span  v-if="!item2.inputVisible" @dblclick="dbEditName(index,index2)" class="clickSpan"> {{item2.name}}  </span>
                    <el-input
                        class="input-new-tag"
                        v-if="item2.inputVisible"
                        v-model="item2.name"
                        ref="saveTagInput"
                        size="small"
                        @blur="handleInputConfirm(index,index2)"
                    ></el-input>
                </span>
                <!-- 新增二级按钮 -->
                <el-input
                    class="input-new-tag"
                    v-if="inputVisible && currentIndex==index"
                    v-model="inputValue"
                    ref="saveTagInput"
                    size="small"
                    @blur="addInputConfirm(index)"
                    ></el-input>
                <el-button type="primary" plain v-else-if="specLevel_2.length<10" @click="addSepcFn(index)" size="mini"  style="margin-left:16px;">+自定义</el-button >
            </div>
            <el-button type="primary"  v-if="specLevel_1.length<3" @click="addSepcGroupFn()">添加规格组</el-button>
            <!-- <el-button type="primary" plain  @click="lookConsole()">看日志</el-button> -->
            </el-form-item>

            <el-form-item label="商品设置:" >
               <goodsTableData :dataForm="dataForm" :specLevel_1="specLevel_1"  :specLevel_2="specLevel_2"></goodsTableData>
            </el-form-item>

            <h4 style="padding-left: 50px;margin-top: 20px;">上传图片:</h4>
            <el-form-item >
                <imageListDefault 
                  ref="imageListDefaultCompon"
                  @setGoodsSpecSaveDTOList="setGoodsSpecSaveDTOList"
                  :goodsSpecSaveDTOList="goodsSpecSaveDTOList" 
                  :dataForm="dataForm" 
                  :specLevel_1="specLevel_1"  
                  :specLevel_2="specLevel_2"
                  @setSpecAttributePictureSaveDTOList="setSpecAttributePictureSaveDTOList"
                  >
                </imageListDefault>
            
               <imageList 
                  ref="imageListCompon"
                  @setGoodsSpecSaveDTOList="setGoodsSpecSaveDTOList"
                  :goodsSpecSaveDTOList="goodsSpecSaveDTOList" 
                  :dataForm="dataForm" 
                  :specLevel_1="specLevel_1"  
                  :specLevel_2="specLevel_2"
                  @setSpecAttributePictureSaveDTOList="setSpecAttributePictureSaveDTOList"
                  >
                </imageList>
            </el-form-item>

    </div>
</template>

<script>

import cloneDeep from 'lodash/cloneDeep'
import goodsTableData from "./goodsTableData.vue"
import imageList from "./imageList.vue"
import imageListDefault from "./imageListDefault.vue"
import { constants } from 'crypto';

export default {
  data () {
    return {
        specLevel_1:[
            // {name:'颜色',checked:false},
            // {name:'尺寸',checked:false},
            // {name:'其他',checked:false},
        ],//商品规格第一层级(最多三个)
        specLevel_2:[
            // [
            //     {name:'红色',checked:false},
            //     {name:'蓝色',checked:false},
            //     {name:'粉色',checked:false},
            // ],
            // [
            //     {name:'s',checked:false},
            //     {name:'m',checked:false},
            //     {name:'x',checked:false},
            //     {name:'l',checked:false},
            // ],
            // [
            //   {name:'自义定文案1',checked:false},
            //   {name:'自义定文案2',checked:false},
            // ]
        ],//商品规格第第二层级
        currentIndex:-1,//点击的是哪一个
        inputVisible:false,
        inputValue:'',
        goodsSpecSaveDTOList:[],
        noSepcGoodsSpecSaveDTOList:[],
    }
  },
  props:["dataForm"],
  components: {
      goodsTableData,
      imageList,
      imageListDefault
  },
   watch: {
    'specLevel_1.length' (val) {
        // this.judgeIsHaveSpe();
    }
  },

  created () {
  },
  methods: {
    //判断是否有规格
      // judgeIsHaveSpe(){
      //     if(this.specLevel_1.length == 0 ){

      //     }
      // },
       // 添加二级规格
      addSepcFn(index){
          this.currentIndex = index;
          this.inputVisible = true;
      },
      // 添加二级规格回调
      addInputConfirm(index){
           this.specLevel_2[index].push(
              {
                name:this.inputValue,
                checked:false,
                addType:1,
                specId:this.specLevel_1[index].specId,
                id:new Date().getTime(),
              },
           )
          this.inputVisible = false;
          this.currentIndex = -1;
          this.inputValue = "";
        //         console.log("specLevel_1,>>>>>>specLevel_2");
        // console.log(this.specLevel_1);
        // console.log(this.specLevel_2);
      },
      // 添加规格组，添加一级规则
      addSepcGroupFn(){
        // 添加第一级
        var len = this.specLevel_1.length?this.specLevel_1.length:new Date().getTime()+1000000;
        this.specLevel_1.push(
            {
              name:'双击编辑',
              checked:false,
              specId:len,
              addType:2
            },
        );
        // 添加第二级
        this.specLevel_2.push([
            {
              name:'双击编辑',
              checked:false,
              specId:len,
              id:new Date().getTime(),
              addType:2
            },
        ]);
        // console.log("specLevel_1,,,,,specLevel_2");
        // console.log(this.specLevel_1);
        // console.log(this.specLevel_2);
      },
      //双击编辑规格名称
      dbEditName(index,index2){
          // alert([index,index2])
           if(index2 || index2==0){//编辑第二级名称
            // console.log("您编辑的是第二级 ");
             this.specLevel_2[index][index2].inputVisible = true;
             this.specLevel_2 = [].concat(this.specLevel_2);
           }else if(index || index==0){//编辑第一级名称
            //   console.log("您编辑的是第一级 ");
              this.specLevel_1[index].inputVisible = true;
              this.specLevel_1 = [].concat(this.specLevel_1);
           }
      },
      // 编辑完成后，回车或者光标失去焦点表示确认
      handleInputConfirm(index,index2){
          if(index2 || index2==0){//编辑第二级名称
            //   console.log("您确认的是第二级 ");
              this.specLevel_2[index][index2].inputVisible = false;
              this.specLevel_2 = [].concat(this.specLevel_2);
           }else if(index || index==0){//编辑第一级级名称
            //  console.log("您确认的是第一级 ");
              this.specLevel_1[index].inputVisible = false;
              this.specLevel_1 = [].concat(this.specLevel_1);
           }
          //  编辑完，表格里面的数据也要实时改变
           this.composeTableDataDelay();
      },  
      composeTableData(type,_index1,_index2){
        // 父复选框取消选择了，子复选框要全部取消选择
         if(type==1 && !this.specLevel_1[_index1].checked){
              this.specLevel_2[_index1].forEach((item,index)=>{
                  item.checked = false;
              })
         }else if(type==2){//子复选框选了，父复选框也要选
              let  isHavechecked = false;
              this.specLevel_2[_index1].forEach((item,index)=>{
                  if(item.checked) {isHavechecked = true}
              })
              this.specLevel_1[_index1].checked = isHavechecked;
              console.log(this.specLevel_1[_index1]);
              this.$set(this.specLevel_1,_index1,this.specLevel_1[_index1]);
         }

          setTimeout(()=>{
            this.composeTableDataDelay();
          },10)
      },
      // lookConsole(){
      //     console.log(this.goodsSpecSaveDTOList);
      // },
      composeTableDataDelay(){
          let that = this;
          var goodsSpecSaveDTOList = [];
          var length = this.specLevel_2.length;
          // 遍历第一组
          length>0 && this.specLevel_2[0].forEach((item1,index)=>{
              if(item1.checked){
                  if(length>1){//有第二组
                      var isHaveChoose2 = false;
                      this.specLevel_2[1].forEach((item2,index2)=>{ // 遍历第二组
                          if(item2.checked){
                              isHaveChoose2 = true;
                              if(length>2){//有第三组
                                  var isHaveChoose3 = false;
                                  this.specLevel_2[2].forEach((item3,index3)=>{ // 遍历第三组
                                    if(item3.checked){ 
                                      getArguObj(item1,item2,item3) 
                                      isHaveChoose3 = true;
                                    }
                                  })
                                  // 如果没选第三组
                                  if(!isHaveChoose3){ 
                                    getArguObj(item1,item2) 
                                  }
                              }else{
                                // 没有第三组
                                getArguObj(item1,item2)
                              }
                          }
                      })
                      // 如果没选第二组
                      if(!isHaveChoose2){ 
                        //  //   // getArguObj(item1) 
                          if(length>2){//有第三组
                              var isHaveChoose3 = false;
                              this.specLevel_2[2].forEach((item3,index3)=>{ // 遍历第三组
                                if(item3.checked){ 
                                  getArguObj(item1,"",item3) 
                                  isHaveChoose3 = true;
                                }
                              })
                              // 如果没选第三组
                              if(!isHaveChoose3){ 
                                getArguObj(item1) 
                              }
                          }else{
                            // 没有第三组
                            getArguObj(item1)
                          }
                      }
                  }else{
                    // 没有第二组
                    getArguObj(item1)
                  }
              }
          })
          // push表格数据
          function getArguObj(item1,item2,item3){
              var level="";
              // var  id = "";
              // var name ="";
              // var specId = ""
              if(item3){
                  level = 2;
                  // id = item3.id
                  // name = item3.name;
                  // specId = item3.specId
              }else if(item2){
                  level = 1;
                  // id = item2.id
                  // name = item2.name;
                  // specId = item2.specId
              }else{
                  level = 0;
                  // id = item1.id
                  // name = item1.name;
                  // specId = item1.specId
              }
              var specId = item1.specId  //主规格的规格值
              var id = item1.id //主规格的规格属性值
              var name1 = item1?item1.name:'';
              var name2 = item2?","+item2.name:'';
              var name3 = item3?","+item3.name:'';
              

              var idPath1 = item1?item1.id:'';
              var idPath2 = item2?","+item2.id:'';
              var idPath3 = item3?","+item3.id:'';


              var level = item3?2:(item2?1:0);

              var obj =  {
                    "goodsId":null,// 商品ID ,
                    // "id":0,//主键id ,
                    "specAttrName":name1+""+name2+""+name3,//商品规格属性值名称（中间用逗号隔开） ,//"红色，s,qita"
                    "idPath":idPath1+""+idPath2+""+idPath3,//商品规格属性值名称（中间用逗号隔开） ,//"红色，s,qita"
                    "specAttributeRelationSaveDTOList":[//在函数里joinSpecAttributeRelationSaveDTOList再拼接一次
                      // {
                      //   "isMain":0,
                      //   "specAttrId":0,
                      //   "specAttrValueId":0
                      // }
                    ],
                    // "specAttrId":item1.specId,
                    "specCostPrice":0,//商品成本价
                    "specMainPicture":"",
                    "specName":that.dataForm.goodsName+" "+name1,//商品规格名称 ,//"手机 红",
                    "specName1":name1,
                    "specSellPrice":0,//商品售价
                    "specSerial": Math.ceil(Math.random()*10000000000)+"",//商品编号
                    "specStorage":0,//商品库存
                    "level":level,//
                    "img_use_id":id,//规格属性值id   如红色  主要是让imageList.vue文件用
                    "img_use_specId":specId,//规格属性id 如颜色 主要是让imageList.vue文件用
              }
              goodsSpecSaveDTOList.push(cloneDeep(obj));
          }
          
          console.log("that.goodsSpecSaveDTOList的值是111：");
          console.log(that.goodsSpecSaveDTOList);
          // 把以前的数据重新显示到页面
          goodsSpecSaveDTOList.forEach((item,index)=>{
              that.goodsSpecSaveDTOList.forEach((item2,index2)=>{
                console.log("检测商品主图");
                  console.log([item.specAttrName,item2.specAttrName,item.specAttrName == item2.specAttrName,index,index2]);
                  if(item.specAttrName == item2.specAttrName){
                    console.log([index, item2.specCostPrice,item2.specCostPrice]);
                      if(item2.specMainPicture) item.specMainPicture = item2.specMainPicture;
                      item.specCostPrice = item2.specCostPrice;
                      item.specSellPrice = item2.specSellPrice;
                      item.specStorage = item2.specStorage;
                      item.specSerial = item2.specSerial;
                  }
              })
          })
      console.log("that.goodsSpecSaveDTOList的值是222：");
       console.log(that.goodsSpecSaveDTOList);
      that.goodsSpecSaveDTOList = cloneDeep(goodsSpecSaveDTOList);
     console.log(that.goodsSpecSaveDTOList);
          // 拼接表格 内部第二层嵌套的sku值，（后端需要用）
          that.joinSpecAttributeRelationSaveDTOList();  
      },
      joinSpecAttributeRelationSaveDTOList(){
          let that = this;
          // console.log("三剑客");
          // console.log(that.goodsSpecSaveDTOList);
          // console.log(this.specLevel_1);
          // console.log(this.specLevel_2);
          
          that.goodsSpecSaveDTOList.forEach((item,index)=>{
           
              item.specAttributeRelationSaveDTOList = [];
              this.specLevel_1.forEach((item2,index2)=>{
                   this.specLevel_2[index2].forEach((item3,index3)=>{
                      var idPath = item.idPath.split(",")
                      console.log([item3.checked, idPath.indexOf(item3.id+""),idPath,item3.id,idPath.indexOf(item3.id)!=-1]);
                       if(item3.checked && (idPath.indexOf(item3.id+"")!=-1) || (idPath.indexOf(item3.id)!=-1)){
                           console.log(['表格第二层内部数据.............',index,item3.id,item3.specId]);
                          // if(!item.specId) item.specId = item3.specId
                          item.specAttributeRelationSaveDTOList.push(
                            {
                              "isMain":index2==0?1:0,
                              "specAttrId":item3.specId,
                              "specAttrValueId":item3.id
                            }
                          )
                       }
                  })
              })
          })
          console.log("this.specLevel_2");
          console.log(this.specLevel_2);
          console.log("-----------------------that.goodsSpecSaveDTOList");
          console.log(that.goodsSpecSaveDTOList);
          // console.log([item,index]);
          this.setGoodsSpecSaveDTOList();
      },
      setGoodsSpecSaveDTOList(){
           this.$emit("setGoodsSpecSaveDTOList",this.goodsSpecSaveDTOList);
      },
      setSpecAttributePictureSaveDTOList(specAttributePictureSaveDTOList) {
          this.$emit("setSpecAttributePictureSaveDTOList",specAttributePictureSaveDTOList);
      }
  
  }
}
</script>
<style lang="scss" scoped>

.input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
  
  </style>