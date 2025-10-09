<template>
	<s-layout :title="device.name || '设备详情'" :back="true">
		<view class="device-detail">
			<!-- 设备基本信息 -->
			<view class="device-info-card">
				<view class="device-header">
					<view class="device-avatar">
						<image class="device-icon" :src="sheep.$url.static('/static/common/device_manager.png')" />
					</view>
					<view class="device-basic">
						<text class="device-name">{{ device.name }}</text>
						<view class="device-status-badge" :class="device.status">
							<view class="status-dot"></view>
							<text>{{ getStatusText(device.status) }}</text>
						</view>
						<text class="device-type">{{ device.type }} | {{ device.model }}</text>
						<text class="device-location" v-if="device.location">📍 {{ device.location }}</text>
					</view>
				</view>
			</view>

			<!-- 实时状态 -->
			<view class="status-section">
				<view class="section-header">
					<text class="section-title">实时状态</text>
					<text class="last-update">{{ formatTime(device.lastConnectTime) }}</text>
				</view>

				<view class="status-grid">
					<view class="status-item">
						<view class="status-icon online" v-if="device.status === 'online'">✓</view>
						<view class="status-icon offline" v-else>×</view>
						<text class="status-label">连接状态</text>
						<text class="status-value">{{ getStatusText(device.status) }}</text>
					</view>

					<view class="status-item">
						<view class="status-icon" :class="getPaperStatusClass(device.paperStatus)">📄</view>
						<text class="status-label">纸张状态</text>
						<text class="status-value" :class="getPaperStatusClass(device.paperStatus)">
							{{ getPaperStatusText(device.paperStatus) }}
						</text>
					</view>

					<view class="status-item">
						<view class="status-icon" :class="getInkStatusClass(device.inkLevel)">💧</view>
						<text class="status-label">墨水水平</text>
						<text class="status-value" :class="getInkStatusClass(device.inkLevel)">
							{{ device.inkLevel }}%
						</text>
					</view>

					<view class="status-item">
						<view class="status-icon queue">📋</view>
						<text class="status-label">打印队列</text>
						<text class="status-value">{{ device.queueCount || 0 }} 个任务</text>
					</view>
				</view>
			</view>

			<!-- 打印统计 -->
			<view class="stats-section">
				<view class="section-header">
					<text class="section-title">打印统计</text>
					<text class="stats-period">今日数据</text>
				</view>

				<view class="stats-grid">
					<view class="stat-card">
						<text class="stat-number">{{ device.todayCount || 0 }}</text>
						<text class="stat-label">今日完成</text>
						<view class="stat-trend up" v-if="todayGrowth > 0">
							<text>+{{ todayGrowth }}%</text>
						</view>
					</view>

					<view class="stat-card">
						<text class="stat-number">{{ device.successRate || 0 }}%</text>
						<text class="stat-label">成功率</text>
						<view class="stat-trend" :class="device.successRate >= 90 ? 'up' : 'down'">
							<text>{{ device.successRate >= 90 ? '良好' : '需关注' }}</text>
						</view>
					</view>

					<view class="stat-card">
						<text class="stat-number">{{ estimatedSpeed }}</text>
						<text class="stat-label">打印速度</text>
						<text class="stat-unit">页/分钟</text>
					</view>

					<view class="stat-card">
						<text class="stat-number">{{ device.totalPrinted || 0 }}</text>
						<text class="stat-label">累计打印</text>
						<text class="stat-unit">页</text>
					</view>
				</view>
			</view>

			<!-- 连接信息 -->
			<view class="connection-section">
				<view class="section-header">
					<text class="section-title">连接信息</text>
				</view>

				<view class="connection-info">
					<view class="info-item">
						<text class="info-label">连接类型</text>
						<text class="info-value">{{ getConnectionTypeText(device.connectionType) }}</text>
					</view>
					<view class="info-item">
						<text class="info-label">连接地址</text>
						<text class="info-value">{{ device.address }}</text>
					</view>
					<view class="info-item" v-if="device.port">
						<text class="info-label">端口</text>
						<text class="info-value">{{ device.port }}</text>
					</view>
					<view class="info-item">
						<text class="info-label">最后连接</text>
						<text class="info-value">{{ formatTime(device.lastConnectTime) }}</text>
					</view>
				</view>
			</view>

			<!-- 当前队列预览 -->
			<view class="queue-section" v-if="device.queueCount > 0">
				<view class="section-header">
					<text class="section-title">当前队列 ({{ device.queueCount }})</text>
					<text class="view-all" @click="viewFullQueue">查看全部</text>
				</view>

				<view class="queue-preview">
					<view class="queue-item" v-for="(item, index) in queuePreview" :key="item.id">
						<view class="queue-index">{{ index + 1 }}</view>
						<view class="queue-info">
							<text class="queue-title">{{ item.documentTitle }}</text>
							<text class="queue-customer">客户: {{ item.customerName }}</text>
							<text class="queue-pages">{{ item.pages }} 页 × {{ item.copies }} 份</text>
						</view>
						<view class="queue-status" :class="item.status">
							<text>{{ getQueueStatusText(item.status) }}</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 操作按钮 -->
			<view class="actions-section">
				<button class="action-btn primary" @click="testPrint"
					v-if="device.status === 'online' && device.queueCount === 0">
					<text class="btn-icon">🧪</text>
					<text>测试打印</text>
				</button>

				<button class="action-btn warning" @click="pauseDevice"
					v-if="device.status === 'busy' || device.status === 'online'">
					<text class="btn-icon">⏸️</text>
					<text>暂停设备</text>
				</button>

				<button class="action-btn success" @click="resumeDevice" v-if="device.status === 'paused'">
					<text class="btn-icon">▶️</text>
					<text>恢复设备</text>
				</button>

				<button class="action-btn info" @click="reconnectDevice" v-if="device.status === 'offline'">
					<text class="btn-icon">🔄</text>
					<text>重新连接</text>
				</button>

				<button class="action-btn secondary" @click="clearQueue" v-if="device.queueCount > 0">
					<text class="btn-icon">🗑️</text>
					<text>清空队列</text>
				</button>

				<button class="action-btn secondary" @click="editDevice">
					<text class="btn-icon">✏️</text>
					<text>编辑设备</text>
				</button>
			</view>

			<!-- 删除设备 -->
			<view class="danger-section">
				<button class="danger-btn" @click="deleteDevice">
					<text class="btn-icon">❌</text>
					<text>删除设备</text>
				</button>
			</view>
		</view>
	</s-layout>
</template>

<script setup>
	import {
		ref,
		reactive,
		computed,
		onMounted,
		onUnmounted
	} from 'vue';
	import sheep from '@/sheep';
	import StoreApi from '@/sheep/api/store';

	// 响应式数据
	const device = reactive({
		id: null,
		name: '',
		type: '',
		model: '',
		status: 'offline',
		location: '',
		address: '',
		connectionType: '',
		port: null,
		paperStatus: 'sufficient',
		inkLevel: 100,
		queueCount: 0,
		todayCount: 0,
		successRate: 100,
		totalPrinted: 0,
		lastConnectTime: null
	});

	const props = defineProps({
		id: {
			type: Number,
			required: true
		}
	});

	const queuePreview = ref([]);
	const loading = ref(true);
	const refreshTimer = ref(null);

	// 计算属性
	const todayGrowth = computed(() => {
		// 模拟增长率计算
		return Math.floor(Math.random() * 20);
	});

	const estimatedSpeed = computed(() => {
		// 根据设备类型估算打印速度
		const speedMap = {
			'激光打印机': 20,
			'喷墨打印机': 10,
			'热敏打印机': 30,
			'针式打印机': 5
		};
		return speedMap[device.type] || 15;
	});

	// 生命周期
	onMounted(() => {
		const deviceId = props.id;
		if (deviceId) {
			loadDeviceDetail(deviceId);
			startAutoRefresh();
		}
	});

	onUnmounted(() => {
		stopAutoRefresh();
	});
	
	const getPrintQueue = async (deviceId) => {
		const queueRes = await StoreApi.getPrintQueue({
				deviceId: deviceId,
				pageSize: 3,
				current: 1
		});
		if (queueRes.code === 0 && queueRes.data.list) {
			queuePreview.value = queueRes.data.list;
		}
	}

	// 方法
	const loadDeviceDetail = async (deviceId) => {
		try {
			loading.value = true;

			// 加载设备详情
			const deviceRes = await StoreApi.getDeviceDetail(deviceId);
			if (deviceRes.code === 0) {
				Object.assign(device, deviceRes.data);
			}

			// 加载队列预览（前3个任务）
			if (device.queueCount > 0) {
				await getPrintQueue(deviceId);
			}

		} catch (error) {
			console.error('加载设备详情失败:', error);
			uni.showToast({
				title: '加载失败',
				icon: 'none'
			});
		} finally {
			loading.value = false;
		}
	};
	
	

	const startAutoRefresh = () => {
		// 每30秒自动刷新一次
		refreshTimer.value = setInterval(() => {
			loadDeviceDetail(device.id);
		}, 30000);
	};

	const stopAutoRefresh = () => {
		if (refreshTimer.value) {
			clearInterval(refreshTimer.value);
			refreshTimer.value = null;
		}
	};

	const testPrint = async () => {
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
						// 刷新设备状态
						setTimeout(() => {
							loadDeviceDetail(device.id);
						}, 1000);
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

	const pauseDevice = async () => {
		try {
			await sheep.api.post(`/api/store/devices/${device.id}/pause`);
			uni.showToast({
				title: '设备已暂停',
				icon: 'success'
			});
			device.status = 'paused';
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			});
		}
	};

	const resumeDevice = async () => {
		try {
			await sheep.api.post(`/api/store/devices/${device.id}/resume`);
			uni.showToast({
				title: '设备已恢复',
				icon: 'success'
			});
			device.status = 'online';
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			});
		}
	};

	const reconnectDevice = async () => {
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
			loadDeviceDetail(device.id);
		} catch (error) {
			uni.hideLoading();
			uni.showToast({
				title: '重连失败',
				icon: 'none'
			});
		}
	};

	const clearQueue = () => {
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
						device.queueCount = 0;
						queuePreview.value = [];
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

	const editDevice = () => {
		uni.navigateTo({
			url: `/pages/store/devices/edit?id=${device.id}`
		});
	};

	const deleteDevice = () => {
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
						// 返回上一页
						setTimeout(() => {
							uni.navigateBack();
						}, 1500);
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

	const viewFullQueue = () => {
		uni.navigateTo({
			url: `/pages/store/queue/index?deviceId=${device.id}`
		});
	};

	// 工具方法
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

	const getConnectionTypeText = (type) => {
		const typeMap = {
			'TCP': 'TCP网络连接',
			'USB': 'USB连接',
			'Bluetooth': '蓝牙连接'
		};
		return typeMap[type] || type;
	};

	const getQueueStatusText = (status) => {
		const statusMap = {
			'waiting': '等待中',
			'printing': '打印中',
			'completed': '已完成',
			'failed': '失败',
			'paused': '暂停',
			'cancelled': '已取消'
		};
		return statusMap[status] || status;
	};

	const formatTime = (time) => {
		if (!time) return '未知';

		const now = new Date();
		const target = new Date(time);
		const diff = Math.floor((now - target) / 1000);

		if (diff < 60) return '刚刚';
		if (diff < 3600) return `${Math.floor(diff / 60)}分钟前`;
		if (diff < 86400) return `${Math.floor(diff / 3600)}小时前`;
		return `${Math.floor(diff / 86400)}天前`;
	};
</script>

<style scoped>
	.device-detail {
		background-color: #f8f9fa;
		min-height: 100vh;
		padding: 20rpx;
	}

	.device-info-card {
		background: white;
		border-radius: 16rpx;
		padding: 40rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.device-header {
		display: flex;
		align-items: center;
	}

	.device-avatar {
		width: 120rpx;
		height: 120rpx;
		background: #f0f2f5;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 30rpx;
	}

	.device-icon {
		width: 70rpx;
		height: 70rpx;
	}

	.device-basic {
		flex: 1;
	}

	.device-name {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 12rpx;
		display: block;
	}

	.device-status-badge {
		display: inline-flex;
		align-items: center;
		padding: 5rpx 16rpx;
		border-radius: 20rpx;
		font-size: 22rpx;
		color: white;
		margin-bottom: 12rpx;
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

	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 24rpx;
	}

	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.last-update,
	.stats-period {
		font-size: 24rpx;
		color: #999;
	}

	.view-all {
		font-size: 26rpx;
		color: #667eea;
	}

	.status-section {
		background: white;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.status-grid {
		display: grid;
		grid-template-columns: 1fr 1fr;
		gap: 24rpx;
	}

	.status-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 30rpx 20rpx;
		background: #f8f9fa;
		border-radius: 12rpx;
	}

	.status-icon {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16rpx;
		font-size: 24rpx;
		color: white;
	}

	.status-icon.online {
		background: #4caf50;
	}

	.status-icon.offline {
		background: #f44336;
	}

	.status-icon.status-good {
		background: #4caf50;
	}

	.status-icon.status-warning {
		background: #ff9800;
	}

	.status-icon.status-error {
		background: #f44336;
	}

	.status-icon.queue {
		background: #667eea;
	}

	.status-label {
		font-size: 24rpx;
		color: #666;
		margin-bottom: 8rpx;
	}

	.status-value {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
	}

	.status-value.status-good {
		color: #4caf50;
	}

	.status-value.status-warning {
		color: #ff9800;
	}

	.status-value.status-error {
		color: #f44336;
	}

	.stats-section {
		background: white;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.stats-grid {
		display: grid;
		grid-template-columns: 1fr 1fr;
		gap: 20rpx;
	}

	.stat-card {
		padding: 30rpx 20rpx;
		background: #f8f9fa;
		border-radius: 12rpx;
		text-align: center;
		position: relative;
	}

	.stat-number {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.stat-label {
		font-size: 24rpx;
		color: #666;
		display: block;
		margin-bottom: 8rpx;
	}

	.stat-unit {
		font-size: 20rpx;
		color: #999;
	}

	.stat-trend {
		position: absolute;
		top: 16rpx;
		right: 16rpx;
		font-size: 20rpx;
		padding: 4rpx 8rpx;
		border-radius: 8rpx;
	}

	.stat-trend.up {
		background: #e8f5e8;
		color: #4caf50;
	}

	.stat-trend.down {
		background: #ffeaa7;
		color: #e17055;
	}

	.connection-section {
		background: white;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.connection-info {}

	.info-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 0;
		border-bottom: 1px solid #f0f0f0;
	}

	.info-item:last-child {
		border-bottom: none;
	}

	.info-label {
		font-size: 28rpx;
		color: #666;
	}

	.info-value {
		font-size: 28rpx;
		color: #333;
	}

	.queue-section {
		background: white;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.queue-preview {}

	.queue-item {
		display: flex;
		align-items: center;
		padding: 20rpx 0;
		border-bottom: 1px solid #f0f0f0;
	}

	.queue-item:last-child {
		border-bottom: none;
	}

	.queue-index {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background: #667eea;
		color: white;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		font-size: 24rpx;
		font-weight: bold;
	}

	.queue-info {
		flex: 1;
	}

	.queue-title {
		font-size: 28rpx;
		color: #333;
		font-weight: bold;
		display: block;
		margin-bottom: 8rpx;
	}

	.queue-customer {
		font-size: 24rpx;
		color: #666;
		display: block;
		margin-bottom: 4rpx;
	}

	.queue-pages {
		font-size: 22rpx;
		color: #999;
	}

	.queue-status {
		padding: 8rpx 16rpx;
		border-radius: 16rpx;
		font-size: 22rpx;
		color: white;
	}

	.queue-status.waiting {
		background: #ff9800;
	}

	.queue-status.printing {
		background: #2196f3;
	}

	.queue-status.completed {
		background: #4caf50;
	}

	.queue-status.failed {
		background: #f44336;
	}

	.queue-status.paused {
		background: #9e9e9e;
	}

	.actions-section {
		background: white;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.action-btn {
		width: 100%;
		height: 88rpx;
		border-radius: 44rpx;
		border: none;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16rpx;
	}

	.action-btn:last-child {
		margin-bottom: 0;
	}

	.action-btn.primary {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: white;
	}

	.action-btn.success {
		background: #4caf50;
		color: white;
	}

	.action-btn.warning {
		background: #ff9800;
		color: white;
	}

	.action-btn.info {
		background: #2196f3;
		color: white;
	}

	.action-btn.secondary {
		background: #f5f5f5;
		color: #666;
	}

	.btn-icon {
		margin-right: 12rpx;
	}

	.danger-section {
		padding: 30rpx;
	}

	.danger-btn {
		width: 100%;
		height: 88rpx;
		border-radius: 44rpx;
		border: 2rpx solid #f44336;
		background: white;
		color: #f44336;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>