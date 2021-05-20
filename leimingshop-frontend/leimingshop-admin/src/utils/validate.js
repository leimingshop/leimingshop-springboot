/**
 * 邮箱
 * @param {*} s
 */
export function isEmail (s) {
 //return /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s)
 return /^[a-zA-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/.test(s)
 //return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.+-](\.[a-zA-Z]{2,4})$/.test(s)
}

/**
 * 手机号码
 * @param {*} s
 */
export function isMobile (s) {
 // return /^1[3|4|5|7|8|9][0-9]\d{4,8}$/.test(s)
  return /^1[34578]\d{9}$/.test(s)
}

/**
 * 电话号码
 * @param {*} s
 */
export function isPhone (s) {
  return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s)
}

/**
 * URL地址
 * @param {*} s
 */
export function isURL (s) {
  return /^http[s]?:\/\/.*/.test(s)
}

/**
 * 注册资金
 * @param {*} s
 */
export function isPrice (s) {
  return /^\d{0,6}$/.test(s)
}

/**
 * 整数校验
 */
export function isNumber (s) {
  return /^[1-9]\d*$/.test(s)
}


// /**
//  * 店铺名称
//  * @param {*} s
//  */
// export function storeName (s) {
//   return /^\d{0,6}$/.test(s)
// }
