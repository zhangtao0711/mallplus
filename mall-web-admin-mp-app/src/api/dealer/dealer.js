import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/water/wtEquipmentWarterCard/list',
        method: 'get',
        params: params
    })
}

// 添加经销商
export function createDealer(data) {
    return request({
        url: '/sys/sysUser/create',
        method: 'post',
        data: data
    })
}

export function createRecharge(data) {
    return request({
        url: '/sys/sysDealerRechargeRecord/create',
        method: 'post',
        data: data
    })
}

// 修改应用权限
export function updateDealer(id, data) {
    return request({
        url: '/sys/sysUser/update/' + id,
        method: 'post',
        data: data
    })
}

export function deleteAdmin(id) {
    return request({
      url:'/sys/sysUser/delete/'+id,
      method:'get',
    })
  }

// 添加应用权限
export function createDealerUse(data) {
    return request({
        url: '/sys/sysDealerUse/create',
        method: 'post',
        data: data
    })
}

// 查询应用权限
export function getDealerUse(id) {
    return request({
        url: '/sys/sysDealerUse/' + id,
        method: 'get',
    })
}

// 获取地区数据
export function getAreaList(params) {
    return request({
        url: '/sys/SysArea/list',
        method: 'get',
        params: params
    })
}

export function getUserInfo(id) {
    return request({
        url: '/sys/sysUser/' + id,
        method: 'get',
    })
}

// 修改应用权限
export function updateDealerUse(id, data) {
    return request({
        url: '/sys/sysDealerUse/update/' + id,
        method: 'post',
        data: data
    })
}

// 监测上级账号的企业付款到零钱功能
export function monitorFirmPay(id) {
    return request({
        url: '/ums/SysAppletSet/monitorFirmPay?parentUserId=' + id,
        method: 'post',
    })
}

// 监测上级账号的企业付款到零钱功能
export function monitorWeChantPay(data) {
    return request({
        url: '/ums/SysAppletSet/monitorWeChantPay',
        method: 'post',
        data: data
    })
}

export function getOriginByUniacid(id) {
    return request({
        url: '/sys/sysUser/getOriginByUniacid?uniacid=' + id,
        method: 'get'
    })
}

// 监测分账功能
export function monitorProfitShare(data) {
    return request({
        url: '/ums/SysAppletSet/monitorProfitShare',
        method: 'post',
        data: data
    })
}

export function lastDealer(params) {
    return request({
        url: '/ums/SysAppletSet/getMonitorFirm',
        method: 'get',
        params: params
    })
}