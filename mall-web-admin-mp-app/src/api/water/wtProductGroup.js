import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/water/wtProductGroup/list',
method: 'get',
params: params
})
}
export function createWtProductGroup(data) {
return request({
url: '/water/wtProductGroup/create',
method: 'post',
data: data
})
}

export function deleteWtProductGroup(id) {
return request({
url: '/water/wtProductGroup/delete/' + id,
method: 'get',
})
}

export function getWtProductGroup(id) {
return request({
url: '/water/wtProductGroup/' + id,
method: 'get',
})
}

export function updateWtProductGroup(id, data) {
return request({
url: '/water/wtProductGroup/update/' + id,
method: 'post',
data: data
})
}

