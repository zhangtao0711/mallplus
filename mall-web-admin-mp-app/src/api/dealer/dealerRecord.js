import request from '@/utils/request'

// 查询消费记录
export function getConsumption(params) {
    return request({
        url: '/sys/sysDealerRechargeRecord/getPurchaseHistory',
        method: 'get',
        params: params
    })
}

// 查询充值记录
export function getChargeRecord(params) {
    return request({
        url: '/sys/sysDealerRechargeRecord/list',
        method: 'get',
        params: params
    })
}

// 查询设备列表
// export function getChargeRecord(params) {
//     return request({
//         url: '/sys/sysDealerRechargeRecord/chargeRecord',
//         method: 'get',
//         params: params
//     })
// }
