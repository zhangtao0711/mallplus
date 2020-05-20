import request from '@/utils/request'
export function orderStatic() {
  return request({
    url:'/home/orderStatic',
    method:'get'
  })
}

export function goodsStatic() {
  return request({
    url:'/home/goodsStatic',
    method:'get'
  })
}

export function userStatic() {
  return request({
    url:'/home/userStatic',
    method:'get'
  })
}
export function goodsSort(params) {
  return request({
    url:'/home/goodsSort',
    method:'get',
     params: params
  })
}
export function goodsCollect(params) {
  return request({
    url:'/home/goodsCollect',
    method:'get',
     params: params
  })
}
export function orderStatusStatics(params) {
  return request({
    url:'/home/orderStatusStatics',
    method:'get',
     params: params
  })
}
export function memberMonthStatic(params) {
  return request({
    url:'/home/memberMonthStatic',
    method:'get',
     params: params
  })
}
// export function acceptRedPacket(id) {
//   return request({
//     url:'/sms/SmsRedPacket/accept/'+id,
//     method:'get',
//   })
// }
// export function list() {
//   return request({
//     url:'/sms/SmsRedPacket/list/',
//     method:'get',
//   })
// }
