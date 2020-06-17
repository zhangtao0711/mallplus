import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/water/wtWarterCardEmpower/list',
        method: 'get',
        params: params
    })
}
export function createWtWarterCardEmpower(data) {
    return request({
        url: '/water/wtWarterCardEmpower/create',
        method: 'post',
        data: data
    })
}

export function deleteWtWarterCardEmpower(id) {
    return request({
        url: '/water/wtWarterCardEmpower/delete/' + id,
        method: 'get',
    })
}

export function getWtWarterCardEmpower(id) {
    return request({
        url: '/water/wtWarterCardEmpower/' + id,
        method: 'get',
    })
}

export function updateWtWarterCardEmpower(id, data) {
    return request({
        url: '/water/wtWarterCardEmpower/update/' + id,
        method: 'post',
        data: data
    })
}

