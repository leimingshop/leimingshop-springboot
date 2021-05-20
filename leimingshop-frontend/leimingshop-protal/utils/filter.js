/*
 * 过滤器
 */

const vueFilter = {
    //时间戳处理
    formDate: function (value, format) {
        var days = parseInt(value / 1000 / 60 / 60 / 24, 10); //计算剩余的天数
        var hours = parseInt(value / 1000 / 60 / 60 % 24, 10); //计算剩余的小时
        var minutes = parseInt(value / 1000 / 60 % 60, 10); //计算剩余的分钟
        var seconds = parseInt(value / 1000 % 60, 10); //计算剩余的秒数
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

    filterImgUrl: function (value) {
        if (!value) {
            return "http://leimingshop/static/category/goodslist-blockimg.png";
        }
        if (/http/.test(value) || /data:image/.test(value)) {
            //  如果是绝对地址，不用加前缀
        } else {
            value = "http://leimingshop/" + value; // ssr暂时修改地址
        }
        return value;
    },

    filterhrefUrl: function (value) {
        if (value && value.id) {
            if (value.specId) {
                return window.SITE_CONFIG['h5Url'] + "/#/pagesA/product/product?id=" + value.id + "&specId=" + value.specId;
            } else {
                return window.SITE_CONFIG['h5Url'] + "/#/pagesA/product/product?id=" + value.id;
            }
        } else {
            return window.SITE_CONFIG['h5Url'] + "/#/";
        }
    },

    filterPrice: function (value, leftSize = 14, centerSize = 20, rightSize = 12) {
        let tempArr = value.toString().split('.')
        tempArr[1] ? '' : tempArr[1] = '00'
        let decimals = tempArr[1].length == 1 ? `${tempArr[1]}0` : tempArr[1].slice(0, 2)
        return `
            <span style="font-size: ${leftSize}px;line-height: ${leftSize}px;">￥</span>
            <span style="font-size: ${centerSize}px;line-height: ${centerSize}px;font-weight:600;margin:0 -4px">${tempArr[0]}</span>
            <span style="font-size: ${rightSize}px;line-height:15px;font-weight:600">.${decimals}</span>
        `
    },

    filterCountDown(endtime, ele, format) {
        if (!document.querySelector(ele)) return

        countDown()

        function countDown() {
            if (!document.querySelector(ele)) return

            var nowtime = new Date();
            var lefttime = parseInt((endtime.getTime() - nowtime.getTime()) / 1000);
            var d = parseInt(lefttime / (24 * 60 * 60)).toString().padStart(2, '0');
            var h = parseInt(lefttime / (60 * 60) % 24).toString().padStart(2, '0');
            var m = parseInt(lefttime / 60 % 60).toString().padStart(2, '0');
            var s = parseInt(lefttime % 60).toString().padStart(2, '0');

            document.querySelector(ele).innerHTML = formatTime(d, h, m, s)

            if (lefttime <= 0) {
                document.querySelector(ele).innerHTML = "活动已结束";
                return;
            }
            setTimeout(countDown, 1000);
        }

        function formatTime(d, h, m, s) {
            let laterFormat
            switch (format) {
                case 'hh时mm分ss秒':
                    laterFormat = `${h}时${m}分${s}秒`
                    break;

                default:
                    d = d == '00' ? '' : `${d}天`
                    h = h == '00' ? '' : `${h}时`
                    laterFormat = `${d}${h}${m}分${s}秒`
            };
            return laterFormat;
        }
    },

    //订单列表活动标签 start
    filterOrderLabel: function (value) {
        switch (value) {
            case 0:
                return ""
                break;
            case 1:
                return "优惠券"
                break;
            case 2:
                return "满减"
                break;
            case 3:
                return "秒杀"
                break;
            case 4:
                return "拼团"
                break;
            case 5:
                return "限时购"
        }
    }
}
export default vueFilter
