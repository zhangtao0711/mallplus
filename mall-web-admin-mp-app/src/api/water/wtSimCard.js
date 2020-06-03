import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/water/wtSimCard/list',
method: 'get',
params: params
})
}
export function createWtSimCard(data) {
return request({
url: '/water/wtSimCard/create',
method: 'post',
data: data
})
}

export function deleteWtSimCard(id) {
return request({
url: '/water/wtSimCard/delete/' + id,
method: 'get',
})
}

export function getWtSimCard(id) {
return request({
url: '/water/wtSimCard/' + id,
method: 'get',
})
}

export function updateWtSimCard(id, data) {
return request({
url: '/water/wtSimCard/update/' + id,
method: 'post',
data: data
})
}

