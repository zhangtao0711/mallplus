import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/water/wtWaterCardLimit/list',
        method: 'get',
        params: params
    })
}
export function createWtWaterCardLimit(data) {
    return request({
        url: '/water/wtWaterCardLimit/create',
        method: 'post',
        data: data
    })
}
export function createWtWaterCardLimitBatch(data) {
    return request({
        url: '/water/wtWaterCardLimit/createList',
        method: 'post',
        data: data
    })
}

export function deleteWtWaterCardLimit(id) {
    return request({
        url: '/water/wtWaterCardLimit/delete/' + id,
        method: 'get',
    })
}

export function getWtWaterCardLimit(id) {
    return request({
        url: '/water/wtWaterCardLimit/' + id,
        method: 'get',
    })
}

export function updateWtWaterCardLimit(id, data) {
    return request({
        url: '/water/wtWaterCardLimit/update/' + id,
        method: 'post',
        data: data
    })
}

