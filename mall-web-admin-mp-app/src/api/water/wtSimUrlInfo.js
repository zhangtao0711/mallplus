import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/water/wtSimUrlInfo/list',
method: 'get',
params: params
})
}
export function createWtSimUrlInfo(data) {
return request({
url: '/water/wtSimUrlInfo/create',
method: 'post',
data: data
})
}

export function deleteWtSimUrlInfo(id) {
return request({
url: '/water/wtSimUrlInfo/delete/' + id,
method: 'get',
})
}

export function getWtSimUrlInfo(id) {
return request({
url: '/water/wtSimUrlInfo/' + id,
method: 'get',
})
}

export function updateWtSimUrlInfo(id, data) {
return request({
url: '/water/wtSimUrlInfo/update/' + id,
method: 'post',
data: data
})
}

