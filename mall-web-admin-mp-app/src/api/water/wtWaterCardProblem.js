import request from '@/utils/request'
export function fetchList(params) {
    return request({
        url: '/water/wtWaterCard/listProblem',
        method: 'get',
        params: params
    })
}

export function updateStateRemove(id) {
    return request({
        url: '/water/wtWaterCard/updateStateRemove/' + id,
        method: 'post',
    })
}

export function updateWtWaterCardProblem(id, data) {
    return request({
        url: '/water/wtWaterCard/updateState/' + id,
        method: 'post',
        data: data
    })
}

