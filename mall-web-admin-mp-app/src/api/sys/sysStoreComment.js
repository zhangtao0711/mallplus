import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/sys/sysStoreComment/list',
method: 'get',
params: params
})
}
export function createSysStoreComment(data) {
return request({
url: '/sys/sysStoreComment/create',
method: 'post',
data: data
})
}

export function deleteSysStoreComment(id) {
return request({
url: '/sys/sysStoreComment/delete/' + id,
method: 'get',
})
}

export function getSysStoreComment(id) {
return request({
url: '/sys/sysStoreComment/' + id,
method: 'get',
})
}

export function updateSysStoreComment(id, data) {
return request({
url: '/sys/sysStoreComment/update/' + id,
method: 'post',
data: data
})
}

