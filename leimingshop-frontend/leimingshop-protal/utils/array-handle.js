export function handleGroup(array, subGroupLength) {
    let index = 0; // 下标
    let count = 0; // 切割次数
    let newArray = [];
    while(index < array.length) {
        ++count
        newArray.push(array.slice(index, index += subGroupLength));
    }
    return {newArray, count};
}

export function handleMergeMultiArr(arr, key, result = []){
    let tempJSON = arr[0]
    let tempArr = []
    var surplus = []  // 剩余数组
    arr.forEach(item => {
       if ( item[key] == tempJSON[key] ) {
           tempArr.push(item)
       } else {
           surplus.push(item)
       }
    })

    result.push(tempArr)

    if( surplus.length != 0 ) {
         handleMergeMultiArr(surplus, key, result)
    }

    return result
}

export function handleArrRemove(arr, val) {

    if(arr.length == 0 ) return arr

    var index = arr.indexOf(val);
    if (index > -1) {
        arr.splice(index, 1);
        return arr
    }
};