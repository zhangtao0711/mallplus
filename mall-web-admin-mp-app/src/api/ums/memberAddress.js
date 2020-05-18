import request from '@/utils/request'
export function fetchList() {
  return request({
    url:'/ums/UmsMemberReceiveAddress/list',
    method:'get'
  })
}
