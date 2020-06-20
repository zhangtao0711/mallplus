import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/water/wtFilterElementType/list',
method: 'get',
params: params
})
}
export function createWtFilterElementType(data) {
return request({
url: '/water/wtFilterElementType/create',
method: 'post',
data: data
})
}

export function deleteWtFilterElementType(id) {
return request({
url: '/water/wtFilterElementType/delete/' + id,
method: 'get',
})
}

export function getWtFilterElementType(id) {
return request({
url: '/water/wtFilterElementType/' + id,
method: 'get',
})
}

export function updateWtFilterElementType(id, data) {
return request({
url: '/water/wtFilterElementType/update/' + id,
method: 'post',
data: data
})
}

