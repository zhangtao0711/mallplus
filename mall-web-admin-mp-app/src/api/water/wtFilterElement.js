import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/water/wtFilterElement/list',
        method: 'get',
        params: params
    })
}
export function createWtFilterElement(data) {
    return request({
        url: '/water/wtFilterElement/create',
        method: 'post',
        data: data
    })
}

export function deleteWtFilterElement(id) {
    return request({
        url: '/water/wtFilterElement/delete/' + id,
        method: 'get',
    })
}

export function getWtFilterElement(id) {
    return request({
        url: '/water/wtFilterElement/' + id,
        method: 'get',
    })
}

export function updateWtFilterElement(id, data) {
    return request({
        url: '/water/wtFilterElement/update/' + id,
        method: 'post',
        data: data
    })
}

