import axios from 'axios'
import Cookies from 'js-cookie'
// import router from '@/router'
import qs from 'qs'
import { state } from '../store'
// import router from "router";

import {
    clearLoginInfo
} from '@/utils'
import {
    isPlainObject,
    debounce
} from 'lodash'

import {
    Message
} from 'view-design';

let http = axios.create({
    timeout: 1000 * 180,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Accept': 'application/json, text/plain'
    }
})

const codeMessage = {
    200: '请求成功',
    201: '操作成功',
    202: '请求已经进入后台队列',
    204: '删除数据成功',
    400: '发出的请求有错误，服务器未进行操作',
    401: '权限不足（令牌、用户名、密码错误）',
    403: '该访问是被禁止的',
    404: '当前页有一个请求不存在',
    406: '请求结果无法解析',
    410: '请求的资源已被永久删除',
    422: '当创建一个对象时，发生一个验证错误',
    500: '服务器发生错误，请稍后重试',
    502: '网络错误，请稍后重试',
    503: '服务不可用，服务器暂时过载或维护',
    504: '请求超时，请稍后重试'
}

const msgError = debounce((message) => {
    Message.warning(message)
}, 500)

http.interceptors.request.use(config => {
    config.headers['Accept-Language'] = 'zh-CN'
    config.headers['ADMIN-TOKEN'] = ''

    // 默认参数
    var defaults = {}
    // 防止缓存，GET请求默认带_t参数
    if (config.method === 'get') {

        // 去掉对象中值为空的key:value
        if (config.params) {
            for (var key in config.params) {
                if (config.params[key] === '') {
                    delete config.params[key]
                }
            }
        }

        config.params = {
            ...config.params,
            ...{
                '_t': new Date().getTime()
            }
        }
    }
    if (isPlainObject(config.params)) {
        config.params = {
            ...defaults,
            ...config.params
        }
    }
    if (isPlainObject(config.data)) {
        config.data = {
            ...defaults,
            ...config.data
        }
        if (/^application\/x-www-form-urlencoded/.test(config.headers['content-type'])) {
            config.data = qs.stringify(config.data)
        }
    }
    return config
}, error => {
    return Promise.reject(error)
})

/**
 * 响应拦截
 */
http.interceptors.response.use(response => {
    if (response.data.code === 401) {
        clearLoginInfo()
        // this._vm.$nuxt._router.replace({ path:'/login'})
        $nuxt._router.push({ path:'/login'})
        // router.replace({
        //     name: 'Login'
        // })
        return Promise.reject(response.data.msg)
    }

    if (response.data.code !== 200) {
            console.log(response.config.params, 'response.config')
        // 某个接口返回状态码不是200，不需要弹提示的，请在参数中加 noPopupMessage: true
        // get 请求判断config.url，post请求判断config.data
        if (response.config.params && response.config.params.noPopupMessage && response.config.params.noPopupMessage == true) {
            return Promise.resolve(response);
        }
        if (response.config.data ?
            !JSON.parse(response.config.data).noPopupMessage :
            response.config.url.search('noPopupMessage=true') < 0
        ) {
            msgError(response.data.msg)
        }
    }

    // 有时候已经200了，但是data = null
    response.data = response.data || []
    return response

}, (error) => {

    // 请求失败, 某个接口报错不需要弹提示的，请在参数中加 noPopupMessage: true
    // get请求判断config.url，post请求判断config.data
    if (error.config.params.noPopupMessage && error.config.params.noPopupMessage == true) {
        return Promise.reject(error);
    }
    if (error.config.data ?
        !JSON.parse(error.config.data).noPopupMessage :
        error.config.url.search('noPopupMessage=true') < 0
    ) {
        // 请求超时
        if (error.request.readyState == 4 && error.request.status == 0) {
            msgError('请求失败，请刷新重试')
            return Promise.reject(error);
        }

        // 状态码为500
        if (error.response.status == 500) {
            codeMessage[error.response.status] = error.response.data.message
        }

        msgError(codeMessage[error.response.status])
    }

    return Promise.reject(error);
})

export function get(url, params, headers) {
    return new Promise((resolve, reject) => {
        http({ // 进行get请求
            method: 'get',
            url: url,
            params: params, // json转字符串 参数传递
            headers: {
                ...headers,
                'Authorization': Cookies.get('auth') || '',
                'memberSource': 0,
                'rediskey':  Cookies.get('rediskey') || ''
            },
        }).then(res => {
            resolve(res);
        }).catch(err => {
            reject(err)
        })
    });
};

// post方法，对应post请求
// @param {String} url [请求的url地址]
// @param {Object} params [请求时携带的参数]
export function post(url, params) {
    return new Promise((resolve, reject) => {
        // var p = qs.stringify(params) //QS化，用于适合'name=hehe&age=10'的传参
        http({ // 进行post请求
            method: 'post',
            url: url,
            data: params, // json转字符串 用于适合'{"name":"hehe","age":10}'的传参
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'Authorization': Cookies.get('auth') || '',
                'memberSource': 0,
                'rediskey':   Cookies.get('rediskey') || ''
            }
        })
            .then(res => {
                resolve(res);
            })
            .catch(err => {
                reject(err)
            })
    });
}

export function queryPost(url, params) { //登录注册专用post
    return new Promise((resolve, reject) => {
        http({
            method: 'post',
            url: url,
            params: params, // json转字符串 用于适合'{"name":"hehe","age":10}'的传参
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                'memberSource': 0,
                'rediskey': Cookies.get('rediskey') || ''
            }
        })
            // axios.post('/Api/Article/ArticleList',Qs.stringify({sign:'sign',ceshi:'ceshi'}))
            .then(res => {
                resolve(res);
            })
            .catch((response, status, xhr) => {
                reject(response, status, xhr)
            })
    });
}

export function put(url, params) {
    return new Promise((resolve, reject) => {
        http({ // 进行put请求
            method: 'put',
            url: url,
            data: params, // json转字符串 用于适合'{"name":"hehe","age":10}'的传参
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'Authorization': Cookies.get('auth') || '',
                'memberSource': 0,
                'rediskey':   Cookies.get('rediskey') || ''
            }
        })
            .then(res => {
                resolve(res);
            })
            .catch(err => {
                reject(err)
            })
    });
}

export function queryPut(url, params) {
    return new Promise((resolve, reject) => {
        http({ // 进行put请求
            method: 'put',
            url: url,
            params: params, // json转字符串 用于适合'{"name":"hehe","age":10}'的传参
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'Authorization': Cookies.get('auth') || '',
                'memberSource': 0,
                'rediskey':   Cookies.get('rediskey') || ''
            }
        })
            .then(res => {
                resolve(res);
            })
            .catch(err => {
                reject(err)
            })
    });
}

export function deleteFn(url, params) {
    return new Promise((resolve, reject) => {
        http({ // 进行delete请求
            method: 'delete',
            url: url,
            data: params, // json转字符串 用于适合['12313123123132213','12313123232133132']的传参
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'Authorization': Cookies.get('auth') || '',
                'memberSource': 0,
                'rediskey':   Cookies.get('rediskey') || ''
            }
        })
            .then(res => {
                resolve(res);
            })
            .catch(err => {
                reject(err)
            })
    });
};

export function deleteFnOther(url, params) {
    return new Promise((resolve, reject) => {
        http({ // 进行delete请求
            method: 'delete',
            url: url,
            params: params, // json转字符串 参数传递
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'Authorization': Cookies.get('auth') || '',
                'memberSource': 0,
                'rediskey':   Cookies.get('rediskey') || ''
            }
        }).then(res => {
            resolve(res);
        }).catch(err => {
            reject(err)
        })
    });
};
