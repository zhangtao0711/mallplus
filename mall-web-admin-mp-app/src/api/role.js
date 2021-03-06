import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/sys/sysRole/list',
    method:'get',
    params:params
  })
}
export function rolePermission(id) {
  return request({
    url:'/sys/sysRole/permission/'+id,
    method:'get'
  })
}
export function createRole(data) {
  return request({
    url:'/sys/sysRole/create',
    method:'post',
    data:data
  })
}
export function updateShowStatus(data) {
  return request({
    url:'/sys/sysRole/update/showStatus',
    method:'post',
    data:data
  })
}

export function updateFactoryStatus(data) {
  return request({
    url:'/sys/sysRole/update/factoryStatus',
    method:'post',
    data:data
  })
}

export function deleteRole(id) {
  return request({
    url:'/sys/sysRole/delete/'+id,
    method:'get',
  })
}

export function getRole(id) {
  return request({
    url:'/sys/sysRole/'+id,
    method:'get',
  })
}

export function updateRole(id,data) {
  return request({
    url:'/sys/sysRole/update/'+id,
    method:'post',
    data:data
  })
}

