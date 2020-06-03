import request from '@/utils/request'
export function fetchList(params) {
return request({
url: '/water/wtEquipmentWarterCard/list',
method: 'get',
params: params
})
}
export function createWtEquipmentWarterCard(data) {
return request({
url: '/water/wtEquipmentWarterCard/create',
method: 'post',
data: data
})
}

export function deleteWtEquipmentWarterCard(id) {
return request({
url: '/water/wtEquipmentWarterCard/delete/' + id,
method: 'get',
})
}

export function getWtEquipmentWarterCard(id) {
return request({
url: '/water/wtEquipmentWarterCard/' + id,
method: 'get',
})
}

export function updateWtEquipmentWarterCard(id, data) {
return request({
url: '/water/wtEquipmentWarterCard/update/' + id,
method: 'post',
data: data
})
}

