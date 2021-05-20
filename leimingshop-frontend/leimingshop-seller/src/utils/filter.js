/*
	* 过滤器
*/

const vueFilter ={
	//时间戳处理
 	formDate: function (value, format) {
        var days = parseInt(value / 1000 / 60 / 60 / 24  , 10); //计算剩余的天数
        var hours = parseInt(value / 1000 / 60 / 60 % 24 , 10); //计算剩余的小时
        var minutes = parseInt(value / 1000 / 60 % 60, 10);//计算剩余的分钟
        var seconds = parseInt(value / 1000 % 60, 10);//计算剩余的秒数
        days = checkTime(days);
        hours = checkTime(hours);
        minutes = checkTime(minutes);
        seconds = checkTime(seconds);
        function checkTime(i) { //将0-9的数字前面加上0，例1变为01
          if (i < 10) {
            i = "0" + i;
          }
          return i;
        };
        return `${days}天-${hours}小时-${minutes}分`;
  },
  filterImgUrl:function(value){
        if(!value){return "@/assets/images/default.png"; }
        if(/http/.test(value)|| /data:image/.test(value)){
        //  如果是绝对地址，不用加前缀
        }else{
              value = window.SITE_CONFIG['imgURL'] + "" + value;
        }
        return value;
  },
  filterhrefUrl:function(value){
    console.log(value);
    if(value && value.id){
      if(value.specId){
        return window.SITE_CONFIG['h5Url']+"/#/pagesA/product/product?id="+value.id+"&specId="+value.specId;
      }else{
        return window.SITE_CONFIG['h5Url']+"/#/pagesA/product/product?id="+value.id;
      }
    }else{
      return window.SITE_CONFIG['h5Url']+"/#/";
    }
  },
}
export default vueFilter