import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/weixin/accountWechats/list',
        method: 'get',
        params: params
    })
}

// 添加公众号
export function createAccount(data) {
    return request({
        url: '/weixin/accountWechats/create',
        method: 'post',
        data: data
    })
}

// 修改公众号
export function updateAccount(id, data) {
    return request({
        url: '/weixin/accountWechats/update/' + id,
        method: 'post',
        data: data
    })
}

// 获取token
export function authRefreshToken() {
    return request({
        url: '/weixin/accountWechats/getToken',
        method: 'get'
    })
}

// 查询公众号明细
export function getAccount(id) {
    return request({
        url: '/weixin/accountWechats/'+id,
        method: 'get',
    })
}

// 添加小程序
export function createWxapp(data) {
    return request({
        url: '/wxminiapp/accountWxapp/create',
        method: 'post',
        data: data
    })
}

// 修改小程序
export function updateWxapp(id, data) {
    return request({
        url: '/wxminiapp/accountWxapp/update/' + id,
        method: 'post',
        data: data
    })
}

// 查询小程序明细
export function getWxapp(id) {
    return request({
        url: '/wxminiapp/accountWxapp/'+id,
        method: 'get',
    })
}

// 添加小程序版本
export function createWxappV(data) {
    return request({
        url: '/wxminiapp/accountWxappVersion/create',
        method: 'post',
        data: data
    })
}

// 修改小程序版本
export function updateWxappV(id, data) {
    return request({
        url: '/wxminiapp/accountWxappVersion/update/' + id,
        method: 'post',
        data: data
    })
}

// 查询小程序版本明细
export function getWxappV(id) {
    return request({
        url: '/wxminiapp/accountWxappVersion/'+id,
        method: 'get',
    })
}