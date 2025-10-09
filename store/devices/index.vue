<template>
	<s-layout title="门店管理">
		<view class="device-management">
			<!-- 设备统计 -->
			<view class="stats-container">
				<view class="stat-item">
					<text class="stat-number online">{{ deviceStats.online }}</text>
					<text class="stat-label">在线设备</text>
				</view>
				<view class="stat-item">
					<text class="stat-number busy">{{ deviceStats.busy }}</text>
					<text class="stat-label">忙碌设备</text>
				</view>
				<view class="stat-item">
					<text class="stat-number offline">{{ deviceStats.offline }}</text>
					<text class="stat-label">离线设备</text>
				</view>
				<view class="stat-item">
					<text class="stat-number total">{{ deviceStats.total }}</text>
					<text class="stat-label">总设备</text>
				</view>
			</view>

			<!-- 筛选标签 -->
			<view class="filter-tabs">
				<text class="filter-tab" :class="{ active: currentFilter === item.value }" v-for="item in filterTabs"
					:key="item.value" @click="changeFilter(item.value)">
					{{ item.label }}
				</text>
			</view>

			<!-- 设备列表 -->
			<scroll-view scroll-y class="device-list" :refresher-enabled="true" @refresherrefresh="onRefresh"
				:refresher-triggered="refreshing">
				<view class="device-card" v-for="device in filteredDevices" :key="device.id"
					@click="viewDeviceDetail(device)">
					<view class="device-header">
						<view class="device-basic-info">
							<view class="device-name-row">
								<text class="device-name">{{ device.name }}</text>
								<view class="device-status-badge" :class="device.status">
									<view class="status-dot"></view>
									<text>{{ getStatusText(device.status) }}</text>
								</view>
							</view>
							<text class="device-type">{{ device.type }} | {{ device.model }}</text>
							<text class="device-location" v-if="device.location">📍 {{ device.location }}</text>
						</view>
					</view>

					<view class="device-content">
						<view class="device-metrics">
							<view class="metric-item">
								<text class="metric-label">打印队列</text>
								<text class="metric-value">{{ device.queueCount || 0 }}</text>
							</view>
							<view class="metric-item">
								<text class="metric-label">今日完成</text>
								<text class="metric-value">{{ device.todayCount || 0 }}</text>
							</view>
							<view class="metric-item">
								<text class="metric-label">成功率</text>
								<text class="metric-value">{{ device.successRate || 0 }}%</text>
							</view>
						</view>

						<view class="device-health" v-if="device.status === 'online' || device.status === 'busy'">
							<view class="health-item">
								<text class="health-label">纸张状态:</text>
								<text class="health-value" :class="getPaperStatusClass(device.paperStatus)">
									{{ getPaperStatusText(device.paperStatus) }}
								</text>
							</view>
							<view class="health-item">
								<text class="health-label">墨水状态:</text>
								<text class="health-value" :class="getInkStatusClass(device.inkLevel)">
									{{ device.inkLevel }}%
								</text>
							</view>
						</view>
					</view>

					<view class="device-actions">
						<button v-if="device.status === 'online' && device.queueCount === 0"
							@click.stop="testPrint(device)" class="test-btn">
							测试打印
						</button>
						<button v-if="device.status === 'busy'" @click.stop="pauseDevice(device)" class="pause-btn">
							暂停
						</button>
						<button v-if="device.status === 'paused'" @click.stop="resumeDevice(device)" class="resume-btn">
							继续
						</button>
						<button v-if="device.status === 'offline'" @click.stop="reconnectDevice(device)"
							class="reconnect-btn">
							重连
						</button>
						<button @click.stop="showDeviceMenu(device)" class="more-btn">⋯</button>
					</view>
				</view>

				<view v-if="filteredDevices.length === 0" class="empty-state">
					<text>暂无{{ getCurrentFilterText() }}设备</text>
				</view>
			</scroll-view>

			<!-- 添加设备按钮 -->
			<view class="fab" @click="addDevice">
				<text>+</text>
			</view>

			<!-- 设备操作菜单 -->
			<uni-popup ref="deviceMenuPopup" type="bottom">
				<view class="menu-popup">
					<view class="menu-header">
						<text class="menu-title">{{ selectedDevice?.name }}</text>
						<text class="menu-close" @click="closeDeviceMenu">×</text>
					</view>
					<view class="menu-items">
						<view class="menu-item" @click="viewDeviceDetail(selectedDevice)">
							<image class="menu-icon" :src="sheep.$url.static('/static/common/chart.png')" />
							<text class="menu-text">查看详情</text>
						</view>
						<view class="menu-item" @click="editDevice(selectedDevice)">
							<image class="menu-icon" :src="sheep.$url.static('/static/common/device_edit.png')" />
							<text class="menu-text">编辑设备</text>
						</view>
						<view class="menu-item" @click="viewQueue(selectedDevice)">
							<image class="menu-icon" :src="sheep.$url.static('/static/common/show_queue.png')" />
							<text class="menu-text">查看队列</text>
						</view>
						<view class="menu-item" @click="clearQueue(selectedDevice)">
							<image class="menu-icon" :src="sheep.$url.static('/static/common/clear_queue.png')" />
							<text class="menu-text">清空队列</text>
						</view>
						<view class="menu-item danger" @click="deleteDevice(selectedDevice)">
							<image class="menu-icon" :src="sheep.$url.static('/static/common/error.png')" />
							<text class="menu-text">删除设备</text>
						</view>
					</view>
				</view>
			</uni-popup>

			<!-- 添加设备弹窗 -->
			<uni-popup ref="addDevicePopup" type="center">
				<view class="add-device-popup">
					<view class="popup-header">
						<text class="popup-title">添加打印设备</text>
						<text class="popup-close" @click="closeAddDevice">×</text>
					</view>
					<view class="popup-content">
						<view class="form-item">
							<text class="form-label">设备名称</text>
							<input class="form-input" v-model="newDevice.name" placeholder="请输入设备名称" />
						</view>
						<view class="form-item">
							<text class="form-label">设备类型</text>
							<picker @change="onDeviceTypeChange" :value="deviceTypeIndex" :range="deviceTypes">
								<view class="picker-view">
									{{ deviceTypes[deviceTypeIndex] || '请选择设备类型' }}
								</view>
							</picker>
						</view>
						<view class="form-item">
							<text class="form-label">设备型号</text>
							<input class="form-input" v-model="newDevice.model" placeholder="请输入设备型号" />
						</view>
						<view class="form-item">
							<text class="form-label">设备位置</text>
							<input class="form-input" v-model="newDevice.location" placeholder="请输入设备位置" />
						</view>
						<view class="form-item">
							<text class="form-label">连接类型</text>
							<input class="form-input" v-model="newDevice.connectionType" placeholder="TCP等" />
						</view>
						<view class="form-item">
							<text class="form-label">连接地址</text>
							<input class="form-input" v-model="newDevice.address" placeholder="IP地址或蓝牙地址" />
						</view>
						<view class="form-item">
							<text class="form-label">端口</text>
							<input class="form-input" v-model="newDevice.port" placeholder="端口" />
						</view>
					</view>
					<view class="popup-actions">
						<button class="cancel-btn" @click="closeAddDevice">取消</button>
						<button class="confirm-btn" @click="confirmAddDevice">添加</button>
					</view>
				</view>
			</uni-popup>
		</view>
	</s-layout>

</template>

<script setup>
	import {
		ref,
		reactive,
		computed,
		onMounted
	} from 'vue';
	import sheep from '@/sheep';
	import StoreApi from '@/sheep/api/store';
	import uniPopup from '@dcloudio/uni-ui/lib/uni-popup/uni-popup.vue'

	// 响应式数据
	const devices = ref([]);
	const deviceStats = reactive({
		online: 0,
		busy: 0,
		offline: 0,
		total: 0
	});
	const currentFilter = ref('all');
	const filterTabs = ref([{
			label: '全部',
			value: 'all'
		},
		{
			label: '在线',
			value: 'online'
		},
		{
			label: '忙碌',
			value: 'busy'
		},
		{
			label: '离线',
			value: 'offline'
		}
	]);
	const refreshing = ref(false);
	const selectedDevice = ref(null);

	// 引用
	const deviceMenuPopup = ref(null);
	const addDevicePopup = ref(null);

	// 计算属性
	const filteredDevices = computed(() => {
		if (currentFilter.value === 'all') {
			return devices.value;
		}
		return devices.value.filter(device => device.status === currentFilter.value);
	});

	// 生命周期
	onMounted(() => {
		loadDevices();
	});

	// 方法
	const loadDevices = async () => {
		try {
			const res = await StoreApi.getDevices();
			if (res.code === 0) {
				devices.value = res.data;
				calculateStats();
			}
		} catch (error) {
			console.error('获取设备列表失败:', error);
			uni.showToast({
				title: '获取设备列表失败',
				icon: 'none'
			});
		}
	};

	const calculateStats = () => {
		deviceStats.online = devices.value.filter(d => d.status === 'online').length;
		deviceStats.busy = devices.value.filter(d => d.status === 'busy').length;
		deviceStats.offline = devices.value.filter(d => d.status === 'offline').length;
		deviceStats.total = devices.value.length;
	};

	const refreshData = async () => {
		await loadDevices();
	};

	const onRefresh = () => {
		refreshing.value = true;
		refreshData().then(() => {
			refreshing.value = false;
		});
	};

	const changeFilter = (filter) => {
		currentFilter.value = filter;
	};

	const getCurrentFilterText = () => {
		const filterMap = {
			'all': '',
			'online': '在线',
			'busy': '忙碌',
			'offline': '离线'
		};
		return filterMap[currentFilter.value];
	};

	const viewDeviceDetail = (device) => {
		uni.navigateTo({
			url: `/pages/store/devices/detail?id=${device.id}`
		});
	};

	const testPrint = async (device) => {
		uni.showModal({
			title: '测试打印',
			content: '确定要发送测试页到此设备吗？',
			success: async (res) => {
				if (res.confirm) {
					try {
						await sheep.api.post(`/api/store/devices/${device.id}/test`);
						uni.showToast({
							title: '测试页已发送',
							icon: 'success'
						});
					} catch (error) {
						uni.showToast({
							title: '测试打印失败',
							icon: 'none'
						});
					}
				}
			}
		});
	};

	const pauseDevice = async (device) => {
		try {
			await sheep.api.post(`/api/store/devices/${device.id}/pause`);
			uni.showToast({
				title: '设备已暂停',
				icon: 'success'
			});
			loadDevices();
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			});
		}
	};

	const resumeDevice = async (device) => {
		try {
			await sheep.api.post(`/api/store/devices/${device.id}/resume`);
			uni.showToast({
				title: '设备已恢复',
				icon: 'success'
			});
			loadDevices();
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			});
		}
	};

	const reconnectDevice = async (device) => {
		uni.showLoading({
			title: '重新连接中...'
		});
		try {
			await sheep.api.post(`/api/store/devices/${device.id}/reconnect`);
			uni.hideLoading();
			uni.showToast({
				title: '重连成功',
				icon: 'success'
			});
			loadDevices();
		} catch (error) {
			uni.hideLoading();
			uni.showToast({
				title: '重连失败',
				icon: 'none'
			});
		}
	};

	const showDeviceMenu = (device) => {
		selectedDevice.value = device;
		deviceMenuPopup.value.open();
	};

	const closeDeviceMenu = () => {
		deviceMenuPopup.value.close();
		selectedDevice.value = null;
	};

	const editDevice = (device) => {
		closeDeviceMenu();
		uni.navigateTo({
			url: `/pages/store/devices/edit?id=${device.id}`
		});
	};

	const viewQueue = (device) => {
		closeDeviceMenu();
		uni.navigateTo({
			url: `/pages/store/queue/index?deviceId=${device.id}`
		});
	};

	const clearQueue = (device) => {
		closeDeviceMenu();
		uni.showModal({
			title: '确认清空',
			content: '确定要清空此设备的打印队列吗？',
			success: async (res) => {
				if (res.confirm) {
					try {
						await sheep.api.post(`/api/store/devices/${device.id}/clear-queue`);
						uni.showToast({
							title: '队列已清空',
							icon: 'success'
						});
						loadDevices();
					} catch (error) {
						uni.showToast({
							title: '操作失败',
							icon: 'none'
						});
					}
				}
			}
		});
	};

	const deleteDevice = (device) => {
		closeDeviceMenu();
		uni.showModal({
			title: '确认删除',
			content: `确定要删除设备"${device.name}"吗？此操作不可恢复。`,
			success: async (res) => {
				if (res.confirm) {
					try {
						await sheep.api.delete(`/api/store/devices/${device.id}`);
						uni.showToast({
							title: '设备已删除',
							icon: 'success'
						});
						loadDevices();
					} catch (error) {
						uni.showToast({
							title: '删除失败',
							icon: 'none'
						});
					}
				}
			}
		});
	};

	const addDevice = () => {
		uni.navigateTo({
			url: `/pages/store/devices/edit`
		});
	};

	const getStatusText = (status) => {
		const statusMap = {
			'online': '在线',
			'offline': '离线',
			'busy': '忙碌',
			'paused': '暂停',
			'error': '故障'
		};
		return statusMap[status] || '未知';
	};

	const getPaperStatusText = (status) => {
		const statusMap = {
			'sufficient': '充足',
			'low': '不足',
			'empty': '缺纸'
		};
		return statusMap[status] || '未知';
	};

	const getPaperStatusClass = (status) => {
		const classMap = {
			'sufficient': 'status-good',
			'low': 'status-warning',
			'empty': 'status-error'
		};
		return classMap[status] || 'status-default';
	};

	const getInkStatusClass = (level) => {
		if (level > 50) return 'status-good';
		if (level > 20) return 'status-warning';
		return 'status-error';
	};
</script>

<style scoped>
	/* 样式保持不变 */
	.device-management {
		background-color: #f8f9fa;
		min-height: 100vh;
		margin: 0 20rpx 20rpx;
	}

	.stats-container {
		display: flex;
		background: white;
		margin: 20rpx 0;
		border-radius: 16rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
	}

	.stat-item {
		flex: 1;
		text-align: center;
		padding: 40rpx 20rpx;
		border-right: 1px solid #eee;
	}

	.stat-item:last-child {
		border-right: none;
	}

	.stat-number {
		font-size: 48rpx;
		font-weight: bold;
		margin-bottom: 10rpx;
		display: block;
	}

	.stat-number.online {
		color: #4caf50;
	}

	.stat-number.busy {
		color: #ff9800;
	}

	.stat-number.offline {
		color: #f44336;
	}

	.stat-number.total {
		color: #667eea;
	}

	.stat-label {
		font-size: 24rpx;
		color: #999;
	}

	.filter-tabs {
		display: flex;
		background: white;
		margin: 0 0 20rpx;
		border-radius: 16rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.filter-tab {
		flex: 1;
		text-align: center;
		padding: 30rpx 20rpx;
		font-size: 28rpx;
		color: #666;
		position: relative;
	}

	.filter-tab.active {
		color: #667eea;
		font-weight: bold;
	}

	.filter-tab.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 60rpx;
		height: 4rpx;
		background: #667eea;
		border-radius: 2rpx;
	}

	.device-list {
		height: calc(100vh - 300rpx);
		/* padding: 0 20rpx; */
		/* margin: 0 20rpx 20rpx; */
	}

	.device-card {
		background: white;
		border-radius: 16rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.device-header {
		margin-bottom: 24rpx;
	}

	.device-name-row {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 12rpx;
	}

	.device-name {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		flex: 1;
	}

	.device-status-badge {
		display: flex;
		align-items: center;
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
		font-size: 22rpx;
		color: white;
	}

	.device-status-badge.online {
		background: #4caf50;
	}

	.device-status-badge.busy {
		background: #ff9800;
	}

	.device-status-badge.offline {
		background: #f44336;
	}

	.device-status-badge.paused {
		background: #9e9e9e;
	}

	.device-status-badge.error {
		background: #e91e63;
	}

	.status-dot {
		width: 12rpx;
		height: 12rpx;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.8);
		margin-right: 8rpx;
	}

	.device-type {
		font-size: 26rpx;
		color: #666;
		margin-bottom: 8rpx;
		display: block;
	}

	.device-location {
		font-size: 24rpx;
		color: #999;
	}

	.device-content {
		margin-bottom: 24rpx;
	}

	.device-metrics {
		display: flex;
		margin-bottom: 20rpx;
	}

	.metric-item {
		flex: 1;
		text-align: center;
		padding: 20rpx;
		background: #f8f9fa;
		border-radius: 12rpx;
		margin-right: 16rpx;
	}

	.metric-item:last-child {
		margin-right: 0;
	}

	.metric-label {
		font-size: 22rpx;
		color: #666;
		display: block;
		margin-bottom: 8rpx;
	}

	.metric-value {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
	}

	.device-health {
		background: #f8f9fa;
		border-radius: 12rpx;
		padding: 20rpx;
	}

	.health-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16rpx;
	}

	.health-item:last-child {
		margin-bottom: 0;
	}

	.health-label {
		font-size: 24rpx;
		color: #666;
	}

	.health-value {
		font-size: 24rpx;
		font-weight: bold;
	}

	.health-value.status-good {
		color: #4caf50;
	}

	.health-value.status-warning {
		color: #ff9800;
	}

	.health-value.status-error {
		color: #f44336;
	}

	.device-actions {
		display: flex;
		gap: 16rpx;
		justify-content: flex-end;
	}

	.device-actions button {
		border-radius: 8rpx;
		border: none;
		font-size: 22rpx;
		min-width: 120rpx;
	}

	.test-btn {
		background: #e3f2fd;
		color: #2196f3;
	}

	.pause-btn {
		background: #fff3e0;
		color: #ff9800;
	}

	.resume-btn {
		background: #e8f5e8;
		color: #4caf50;
	}

	.reconnect-btn {
		background: #fce4ec;
		color: #e91e63;
	}

	.more-btn {
		background: #f5f5f5;
		color: #666;
		min-width: 60rpx;
	}

	.empty-state {
		text-align: center;
		padding: 100rpx 40rpx;
		color: #999;
		font-size: 28rpx;
	}

	.fab {
		position: fixed;
		bottom: 40rpx;
		right: 40rpx;
		width: 120rpx;
		height: 120rpx;
		background: #667eea;
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
		z-index: 1000;
	}

	.fab text {
		font-size: 48rpx;
		color: white;
		font-weight: bold;
	}

	.menu-popup {
		background: white;
		border-radius: 20rpx 20rpx 0 0;
	}

	.menu-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		border-bottom: 1px solid #eee;
	}

	.menu-title {
		font-size: 32rpx;
		font-weight: bold;
		flex: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.menu-close {
		font-size: 48rpx;
		color: #999;
		line-height: 1;
		margin-left: 20rpx;
	}

	.menu-items {
		padding: 20rpx 0;
	}

	.menu-item {
		display: flex;
		align-items: center;
		padding: 30rpx;
	}

	.menu-item:active {
		background: #f8f9fa;
	}

	.menu-item.danger {
		color: #f44336;
	}

	.menu-icon {
		margin-right: 24rpx;
		width: 48rpx;
		height: 48rpx;
		text-align: center;
	}

	.menu-text {
		font-size: 28rpx;
		flex: 1;
	}

	.add-device-popup {
		background: white;
		width: 85vw;
		border-radius: 16rpx;
		overflow: hidden;
	}

	.popup-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		border-bottom: 1px solid #eee;
	}

	.popup-title {
		font-size: 32rpx;
		font-weight: bold;
		flex: 1;
	}

	.popup-close {
		font-size: 48rpx;
		color: #999;
		line-height: 1;
		margin-left: 20rpx;
	}

	.popup-content {
		padding: 30rpx;
		max-height: 60vh;
		overflow-y: auto;
	}

	.form-item {
		margin-bottom: 30rpx;
	}

	.form-item:last-child {
		margin-bottom: 0;
	}

	.form-label {
		font-size: 28rpx;
		color: #333;
		margin-bottom: 16rpx;
		display: block;
	}

	.form-input {
		width: 100%;
		height: 80rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 12rpx;
		padding: 0 24rpx;
		font-size: 28rpx;
		box-sizing: border-box;
	}

	.form-input:focus {
		border-color: #667eea;
	}

	.picker-view {
		height: 80rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 12rpx;
		padding: 0 24rpx;
		display: flex;
		align-items: center;
		font-size: 28rpx;
		color: #333;
	}

	.popup-actions {
		display: flex;
		gap: 20rpx;
		padding: 30rpx;
		border-top: 1px solid #eee;
	}

	.popup-actions button {
		flex: 1;
		height: 80rpx;
		border-radius: 20rpx;
		border: none;
		font-size: 28rpx;
	}

	.cancel-btn {
		background: #f5f5f5;
		color: #666;
	}

	.confirm-btn {
		background: #667eea;
		color: white;
	}
</style>