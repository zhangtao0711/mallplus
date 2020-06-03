import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/water/wtWaterCardRecharge/list',
method: 'get',
params: params
})
}
export function createWtWaterCardRecharge(data) {
return request({
url: '/water/wtWaterCardRecharge/create',
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

export function getWtWaterCardRecharge(id) {
return request({
url: '/water/wtWaterCardRecharge/' + id,
method: 'get',
})
}

export function updateWtWaterCardRecharge(id, data) {
return request({
url: '/water/wtWaterCardRecharge/update/' + id,
method: 'post',
data: data
})
}

