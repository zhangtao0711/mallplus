import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/ums/UmsMemberTag/list',
method: 'get',
params: params
})
}
export function createUmsMemberTag(data) {
return request({
url: '/ums/UmsMemberTag/create',
method: 'post',
data: data
})
}

export function deleteUmsMemberTag(id) {
return request({
url: '/ums/UmsMemberTag/delete/' + id,
method: 'get',
})
}

export function getUmsMemberTag(id) {
return request({
url: '/ums/UmsMemberTag/' + id,
method: 'get',
})
}

export function updateUmsMemberTag(id, data) {
return request({
url: '/ums/UmsMemberTag/update/' + id,
method: 'post',
data: data
})
}
export function updateShowStatus(data) {
  return request({
    url:'/ums/UmsMemberTag/update/showStatus',
    method:'post',
    data:data
  })
}
