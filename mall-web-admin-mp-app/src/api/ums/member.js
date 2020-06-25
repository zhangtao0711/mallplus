import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/ums/UmsMember/list',
    method:'get',
    params:params
  })
}

// 获取会员日志、备注
export function getLogList(params) {
  return request({
    url:'/ums/umsMemberLog/list',
    method:'get',
    params:params
  })
}

export function createNote(data) {
  return request({
    url:'/ums/umsMemberLog/create',
    method:'post',
    data:data
  })
}

export function createMember(data) {
  return request({
    url:'/ums/UmsMember/create',
    method:'post',
    data:data
  })
}
export function updateShowStatus(data) {
  return request({
    url:'/ums/UmsMember/update/showStatus',
    method:'post',
    data:data
  })
}

export function updateFactoryStatus(data) {
  return request({
    url:'/ums/UmsMember/update/factoryStatus',
    method:'post',
    data:data
  })
}

export function deleteMember(id) {
  return request({
    url:'/ums/UmsMember/delete/'+id,
    method:'get',
  })
}

export function getMember(id) {
  return request({
    url:'/ums/UmsMember/'+id,
    method:'get',
  })
}

export function updateMember(data) {
  return request({
    url:'/ums/UmsMember/update',
    method:'post',
    data:data
  })
}

// 积分管理
export function updateIntegration(data) {
  return request({
    url:'/ums/UmsMember/updateIntegration',
    method:'post',
    data:data
  })
}
// 黑名单
export function updateStatus(data) {
  return request({
    url:'/ums/UmsMember/updateStatus',
    method:'post',
    data:data
  })
}

// 挂失
export function updateWaterCard(data) {
  return request({
    url:'/ums/UmsMember/updateWaterCard',
    method:'post',
    data:data
  })
}

// 添加标签
export function addLabel(data) {
  return request({
    url:'/ums/UmsMember/addLabel',
    method:'post',
    data:data
  })
}

export function removeLabel(data) {
  return request({
    url:'/ums/UmsMember/removeLabel',
    method:'post',
    data:data
  })
}

export function updateLimit(id,data) {
  return request({
    url:'/water/wtWaterCard/updateLimit/'+id,
    method:'post',
    data:data
  })
}


export function updateMemberOrderInfo() {
  return request({
    url:'/ums/UmsMember/updateMemberOrderInfo',
    method:'post'
  })
}

export function fetchBlanceList(id) {
  return request({
    url:'/ums/UmsMember/fetchBlanceList/'+id,
    method:'get',
  })
}

export function handleEditBlance(data) {
  return request({
    url:'/ums/UmsMember/handleEditBlance',
    method:'post',
    data:data
  })
}

export function handleEditIntegration(data) {
  return request({
    url:'/ums/UmsMember/handleEditIntegration',
    method:'post',
    data:data
  })
}
