import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/water/wtWaterCard/list',
method: 'get',
params: params
})
}
export function createWtWaterCard(data) {
return request({
url: '/water/wtWaterCard/create',
method: 'post',
data: data
})
}

export function deleteWtWaterCard(id) {
return request({
url: '/water/wtWaterCard/delete/' + id,
method: 'get',
})
}

export function getWtWaterCard(id) {
return request({
url: '/water/wtWaterCard/' + id,
method: 'get',
})
}

export function updateWtWaterCard(id, data) {
return request({
url: '/water/wtWaterCard/update/' + id,
method: 'post',
data: data
})
}

