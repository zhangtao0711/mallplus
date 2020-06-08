import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/water/wtWaterCardRecharge/list',
        method: 'get',
        params: params
    })
}
// 批量充值
export function createWtWaterCardRecharge(data) {
    return request({
        url: '/water/wtWaterCardRecharge/create',
        method: 'post',
        data: data
    })
}
// 后台普通充值
export function createSingle(data) {
    return request({
        url: '/water/wtWaterCardRecharge/createSingle',
        method: 'post',
        data: data
    })
}
// 后台套餐充值
export function createSinglePackage(data) {
    return request({
        url: '/water/wtWaterCardRecharge/createSinglePackage',
        method: 'post',
        data: data
    })
}
// 批量导入充值
export function createImportExcel(data) {
    return request({
        url: '/water/wtWaterCardRecharge/importExcelCreate',
        method: 'post',
        data: data
    })
}

export function deleteWtWaterCardRecharge(id) {
    return request({
        url: '/water/wtWaterCardRecharge/delete/' + id,
        method: 'get',
    })
}

// 查询充值明细
export function getWtWaterCardRecharge(id) {
    return request({
        url: '/water/wtWaterCardRecharge/' + id,
        method: 'get',
    })
}

// 更新充值
export function updateWtWaterCardRecharge(id, data) {
    return request({
        url: '/water/wtWaterCardRecharge/update/' + id,
        method: 'post',
        data: data
    })
}

