import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/sys/sysStoreClass/list',
method: 'get',
params: params
})
}
export function createSysStoreClass(data) {
return request({
url: '/sys/sysStoreClass/create',
method: 'post',
data: data
})
}

export function deleteSysStoreClass(id) {
return request({
url: '/sys/sysStoreClass/delete/' + id,
method: 'get',
})
}

export function getSysStoreClass(id) {
return request({
url: '/sys/sysStoreClass/' + id,
method: 'get',
})
}

export function updateSysStoreClass(id, data) {
return request({
url: '/sys/sysStoreClass/update/' + id,
method: 'post',
data: data
})
}

