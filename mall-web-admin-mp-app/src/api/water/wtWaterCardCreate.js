import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/water/wtWaterCardCreate/list',
        method: 'get',
        params: params
    })
}
export function createWtWaterCardCreate(data) {
    return request({
        url: '/water/wtWaterCardCreate/create',
        method: 'post',
        data: data
    })
}

export function deleteWtWaterCardCreate(id) {
    return request({
        url: '/water/wtWaterCardCreate/delete/' + id,
        method: 'get',
    })
}

export function getWtWaterCardCreate(id) {
    return request({
        url: '/water/wtWaterCardCreate/' + id,
        method: 'get',
    })
}

export function updateDealerId(id, data) {
    return request({
        url: '/water/wtWaterCardCreate/updateDealerId/' + id,
        method: 'post',
        data: data
    })
}

export function updateWtWaterCardCreate(id, data) {
    return request({
        url: '/water/wtWaterCardCreate/update/' + id,
        method: 'post',
        data: data
    })
}

