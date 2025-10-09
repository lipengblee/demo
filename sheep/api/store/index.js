import {
	baseUrl,
	apiPath,
	tenantId
} from '@/sheep/config';
import request, {
	getAccessToken
} from '@/sheep/request';

const StoreApi = {
	// 获取门店基础信息
	getStoreInfo: () => {
		return request({
			url: '/trade/store/info',
			method: 'GET',
			custom: {
				showLoading: false,
				auth: true,
			},
		});
	},
	// 获取订单统计
	getOrderStats: () => {
		return request({
			url: '/trade/store/order-stats',
			method: 'GET',
			custom: {
				showLoading: false,
				auth: true,
			},
		})
	},
	// 获取最近订单
	getRecentOrders: (params) => {
		return request({
			url: '/trade/store/recent-orders',
			method: 'GET',
			params,
			custom: {
				showLoading: false,
				auth: true,
			},
		})
	},
	// 获取门店设备列表
	getDevices: (params) => {
		return request({
			url: '/trade/store/devices',
			method: 'GET',
			params,
			custom: {
				showLoading: false,
				auth: true,
			},
		})
	},
	// 获取门店订单列表
	getOrders: (params) => {
		return request({
			url: '/trade/store/orders',
			method: 'GET',
			params,
			custom: {
				showLoading: false,
				auth: true,
			},
		})
	},
	// 指派打印机
	assignOrderToDevice: (params) => {
		return request({
			url: '/trade/store/assign-printer',
			method: 'GET',
			params,
			custom: {
				showLoading: true,
				auth: true,
			},
		})
	},
	// 获取订单详情
	getOrderDetail: (params) => {
		return request({
			url: '/trade/store/orders/' + params,
			method: 'GET',
			custom: {
				showLoading: false,
				auth: true,
			},
		})
	},
	// 获取设备详情
	getDeviceDetail: (params) => {
		return request({
			url: '/trade/store/devices/' + params,
			method: 'GET',
			custom: {
				showLoading: false,
				auth: true,
			},
		})
	},
	// 获取打印队列
	getPrintQueue: (params) => {
		return request({
			url: '/trade/store/print-queue',
			method: 'GET',
			params,
			custom: {
				showLoading: false,
				auth: true,
			},
		})
	},
	//添加打印机
	addPrint: (data) => {
		return request({
			url: data.id == null ? '/trade/store/devices' : '/trade/store/devices/' + data.id,
			method: 'POST',
			data,
			custom: {
				showLoading: true,
				auth: true,
			},
		})
	},
}

export default StoreApi;