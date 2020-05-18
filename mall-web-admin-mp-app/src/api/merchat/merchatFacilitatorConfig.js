import request from '@/utils/request'

export function fetchList(params) {
	return request({
		url: '/merchat/merchatFacilitatorConfig/list',
		method: 'get',
		params: params
	})
}
export function createMerchatFacilitatorConfig(data) {
	return request({
		url: '/merchat/merchatFacilitatorConfig/create',
		method: 'post',
		data: data
	})
}

export function deleteMerchatFacilitatorConfig(id) {
	return request({
		url: '/merchat/merchatFacilitatorConfig/delete/' + id,
		method: 'get',
	})
}

export function getMerchatFacilitatorConfig(id) {
	return request({
		url: '/merchat/merchatFacilitatorConfig/' + id,
		method: 'get',
	})
}

export function updateMerchatFacilitatorConfig(id, data) {
	return request({
		url: '/merchat/merchatFacilitatorConfig/update/' + id,
		method: 'post',
		data: data
	})
}
