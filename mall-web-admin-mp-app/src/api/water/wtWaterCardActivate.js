import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/water/wtWaterCardActivate/list',
        method: 'get',
        params: params
    })
}
export function validNumber(params) {
    return request({
        url: '/water/wtWaterCardCreate/list',
        method: 'get',
        params: params
    })
}
export function createWtWaterCardActivate(data) {
    return request({
        url: '/water/wtWaterCardActivate/create',
        method: 'post',
        data: data
    })
}

export function deleteWtWaterCardActivate(id) {
    return request({
        url: '/water/wtWaterCardActivate/delete/' + id,
        method: 'get',
    })
}

export function getWtWaterCardActivate(id) {
    return request({
        url: '/water/wtWaterCardActivate/' + id,
        method: 'get',
    })
}

export function updateWtWaterCardActivate(id, data) {
    return request({
        url: '/water/wtWaterCardActivate/update/' + id,
        method: 'post',
        data: data
    })
}

