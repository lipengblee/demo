<template>
	<s-layout title="队列管理">
		<view class="print-queue">
			<!-- 队列统计 -->
			<view class="queue-stats">
				<view class="stat-card">
					<text class="stat-number">{{ queueStats.waiting }}</text>
					<text class="stat-label">等待中</text>
				</view>
				<view class="stat-card">
					<text class="stat-number">{{ queueStats.printing }}</text>
					<text class="stat-label">打印中</text>
				</view>
				<view class="stat-card">
					<text class="stat-number">{{ queueStats.completed }}</text>
					<text class="stat-label">已完成</text>
				</view>
			</view>

			<!-- 设备选择和操作 -->
			<view class="control-bar">
				<picker @change="onDeviceChange" :value="selectedDeviceIndex" :range="deviceNames">
					<view class="device-selector">
						<text>🖨️ {{ deviceNames[selectedDeviceIndex] || '选择设备' }}</text>
						<text class="arrow">▼</text>
					</view>
				</picker>
				<view class="queue-actions">
					<button @click="pauseQueue" class="pause-btn" :disabled="!canPause">暂停</button>
					<button @click="resumeQueue" class="resume-btn" :disabled="!canResume">继续</button>
					<button @click="clearQueue" class="clear-btn">清空</button>
				</view>
			</view>

			<!-- 状态筛选 -->
			<view class="status-filter">
				<text class="filter-item" :class="{ active: currentStatus === item.value }"
					v-for="item in statusFilters" :key="item.value" @click="changeStatus(item.value)">
					{{ item.label }}
				</text>
			</view>

			<!-- 队列列表 -->
			<scroll-view scroll-y class="queue-list" :refresher-enabled="true" @refresherrefresh="onRefresh"
				:refresher-triggered="refreshing" @scrolltolower="loadMore">
				<view class="queue-item" v-for="(item, index) in filteredQueue" :key="item.id">
					<view class="item-header">
						<view class="order-info">
							<text class="order-number">#{{ item.orderNo }}</text>
							<text class="queue-number">队列 {{ item.queueIndex }}</text>
						</view>
						<view class="item-status" :class="getStatusClass(item.status)">
							{{ getStatusText(item.status) }}
						</view>
					</view>

					<view class="item-content">
						<view class="document-info">
							<text class="document-title">📄 {{ item.documentTitle }}</text>
							<text class="document-pages">{{ item.pages }}页</text>
						</view>

						<view class="print-details">
							<text class="customer">客户: {{ item.customerName }}</text>
							<text class="copies">份数: {{ item.copies }}</text>
						</view>

						<view class="time-info">
							<text class="create-time">创建: {{ formatTime(item.createTime) }}</text>
							<text v-if="item.startTime" class="start-time">开始: {{ formatTime(item.startTime) }}</text>
							<text v-if="item.completeTime" class="complete-time">完成:
								{{ formatTime(item.completeTime) }}</text>
						</view>

						<!-- 进度条 -->
						<view v-if="item.status === 'printing'" class="progress-section">
							<view class="progress-bar">
								<view class="progress-fill" :style="{ width: item.progress + '%' }"></view>
							</view>
							<text class="progress-text">{{ item.progress }}%</text>
						</view>

						<!-- 错误信息 -->
						<view v-if="item.status === 'failed' && item.errorMessage" class="error-section">
							<text class="error-message">❌ {{ item.errorMessage }}</text>
						</view>
					</view>

					<view class="item-actions">
						<button v-if="item.status === 'waiting'" @click="moveToTop(item, index)" class="priority-btn">
							置顶
						</button>
						<button v-if="item.status === 'printing'" @click="pauseItem(item)" class="pause-item-btn">
							暂停
						</button>
						<button v-if="item.status === 'paused'" @click="resumeItem(item)" class="resume-item-btn">
							继续
						</button>
						<button v-if="item.status === 'failed'" @click="retryItem(item)" class="retry-btn">
							重试
						</button>
						<button v-if="['waiting', 'paused', 'failed'].includes(item.status)" @click="cancelItem(item)"
							class="cancel-btn">
							取消
						</button>
						<button @click="viewDetails(item)" class="detail-btn">详情</button>
					</view>
				</view>

				<view v-if="loading" class="loading-more">
					<text>加载中...</text>
				</view>

				<view v-if="!hasMore && filteredQueue.length > 0" class="no-more">
					<text>没有更多数据了</text>
				</view>

				<view v-if="filteredQueue.length === 0 && !loading" class="empty-state">
					<text>暂无{{ getCurrentStatusText() }}任务</text>
				</view>
			</scroll-view>
		</view>
	</s-layout>
	<!-- 模板部分保持不变 -->
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
	const devices = ref([])
	const selectedDeviceIndex = ref(0)
	const selectedDevice = ref(null)
	const queueList = ref([])
	const queueStats = reactive({
		waiting: 0,
		printing: 0,
		completed: 0
	})
	const currentStatus = ref('all')
	const statusFilters = ref([{
			label: '全部',
			value: 'all'
		},
		{
			label: '等待',
			value: 'waiting'
		},
		{
			label: '打印中',
			value: 'printing'
		},
		{
			label: '已完成',
			value: 'completed'
		},
		{
			label: '失败',
			value: 'failed'
		}
	])
	const refreshing = ref(false)
	const loading = ref(false)
	const hasMore = ref(true)
	const currentPage = ref(1)
	const pageSize = ref(20)
	const pollTimer = ref(null)

	// 计算属性
	const deviceNames = computed(() => {
		return devices.value.map(device => device.name)
	})

	const filteredQueue = computed(() => {
		if (currentStatus.value === 'all') {
			return queueList.value
		}
		return queueList.value.filter(item => item.status === currentStatus.value)
	})

	const canPause = computed(() => {
		return selectedDevice.value && selectedDevice.value.status === 'busy'
	})

	const canResume = computed(() => {
		return selectedDevice.value && selectedDevice.value.status === 'paused'
	})

	const props = defineProps({
		deviceId: {
			type: Number,
			required: true
		}
	});

	// 生命周期
	onMounted(() => {
		if (props.deviceId) {
			preSelectDevice(props.deviceId)
		}
		loadDevices()
		loadQueue(true)
		startPolling()
	})

	onUnmounted(() => {
		stopPolling()
	})

	// 方法
	const loadDevices = async () => {
		try {
			const res = await StoreApi.getDevices();
			if (res.data.code === 200) {
				devices.value = res.data.data
				if (devices.value.length > 0 && !selectedDevice.value) {
					selectedDevice.value = devices.value[0]
					selectedDeviceIndex.value = 0
				}
			}
		} catch (error) {
			console.error('获取设备列表失败:', error)
		}
	}

	const preSelectDevice = (deviceId) => {
		const index = devices.value.findIndex(d => d.id == deviceId)
		if (index !== -1) {
			selectedDeviceIndex.value = index
			selectedDevice.value = devices.value[index]
		}
	}

	const loadQueue = async (reset = false) => {
		if (loading.value) return

		loading.value = true

		if (reset) {
			currentPage.value = 1
			queueList.value = []
			hasMore.value = true
		}

		try {
			const params = {
				page: currentPage.value,
				pageSize: pageSize.value,
				deviceId: selectedDevice.value?.id,
				status: currentStatus.value !== 'all' ? currentStatus.value : undefined
			}
			const res = await StoreApi.getPrintQueue(params)
			if (res.code === 0) {
				const {
					list,
					total,
					stats
				} = res.data

				if (reset) {
					queueList.value = list
				} else {
					queueList.value.push(...list)
				}

				Object.assign(queueStats, stats)
				hasMore.value = queueList.value.length < total
				currentPage.value++
			}
		} catch (error) {
			console.error('获取打印队列失败:', error)
			uni.showToast({
				title: '获取队列失败',
				icon: 'none'
			})
		} finally {
			loading.value = false
			refreshing.value = false
		}
	}

	const startPolling = () => {
		pollTimer.value = setInterval(() => {
			loadQueue(true)
		}, 50000) // 每50秒刷新一次
	}

	const stopPolling = () => {
		if (pollTimer.value) {
			clearInterval(pollTimer.value)
		}
	}

	const onDeviceChange = (e) => {
		selectedDeviceIndex.value = e.detail.value
		selectedDevice.value = devices.value[e.detail.value]
		loadQueue(true)
	}

	const changeStatus = (status) => {
		currentStatus.value = status
		loadQueue(true)
	}

	const onRefresh = () => {
		refreshing.value = true
		loadQueue(true)
	}

	const loadMore = () => {
		if (hasMore.value && !loading.value) {
			loadQueue()
		}
	}

	const pauseQueue = async () => {
		if (!selectedDevice.value) return

		try {
			await uni.$http.post(`/api/store/devices/${selectedDevice.value.id}/pause`)
			uni.showToast({
				title: '队列已暂停',
				icon: 'success'
			})
			loadDevices()
			loadQueue(true)
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			})
		}
	}

	const resumeQueue = async () => {
		if (!selectedDevice.value) return

		try {
			await uni.$http.post(`/api/store/devices/${selectedDevice.value.id}/resume`)
			uni.showToast({
				title: '队列已恢复',
				icon: 'success'
			})
			loadDevices()
			loadQueue(true)
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			})
		}
	}

	const clearQueue = async () => {
		if (!selectedDevice.value) return

		uni.showModal({
			title: '确认清空',
			content: '确定要清空当前设备的打印队列吗？',
			success: async (res) => {
				if (res.confirm) {
					try {
						await uni.$http.post(
							`/api/store/devices/${selectedDevice.value.id}/clear-queue`)
						uni.showToast({
							title: '队列已清空',
							icon: 'success'
						})
						loadQueue(true)
					} catch (error) {
						uni.showToast({
							title: '操作失败',
							icon: 'none'
						})
					}
				}
			}
		})
	}

	const moveToTop = async (item, index) => {
		try {
			await uni.$http.post(`/api/store/print-queue/${item.id}/priority`)
			uni.showToast({
				title: '已置顶',
				icon: 'success'
			})
			loadQueue(true)
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			})
		}
	}

	const pauseItem = async (item) => {
		try {
			await uni.$http.post(`/api/store/print-queue/${item.id}/pause`)
			uni.showToast({
				title: '任务已暂停',
				icon: 'success'
			})
			loadQueue(true)
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			})
		}
	}

	const resumeItem = async (item) => {
		try {
			await uni.$http.post(`/api/store/print-queue/${item.id}/resume`)
			uni.showToast({
				title: '任务已恢复',
				icon: 'success'
			})
			loadQueue(true)
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			})
		}
	}

	const retryItem = async (item) => {
		try {
			await uni.$http.post(`/api/store/print-queue/${item.id}/retry`)
			uni.showToast({
				title: '任务已重试',
				icon: 'success'
			})
			loadQueue(true)
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			})
		}
	}

	const cancelItem = async (item) => {
		uni.showModal({
			title: '确认取消',
			content: '确定要取消这个打印任务吗？',
			success: async (res) => {
				if (res.confirm) {
					try {
						await uni.$http.post(`/api/store/print-queue/${item.id}/cancel`)
						uni.showToast({
							title: '任务已取消',
							icon: 'success'
						})
						loadQueue(true)
					} catch (error) {
						uni.showToast({
							title: '操作失败',
							icon: 'none'
						})
					}
				}
			}
		})
	}

	const viewDetails = (item) => {
		uni.navigateTo({
			url: `/pages/store/orders/detail?id=${item.orderId}`
		})
	}

	const getCurrentStatusText = () => {
		const filter = statusFilters.value.find(f => f.value === currentStatus.value)
		return filter ? filter.label.toLowerCase() : ''
	}

	const getStatusClass = (status) => {
		const classMap = {
			'waiting': 'status-waiting',
			'printing': 'status-printing',
			'completed': 'status-completed',
			'failed': 'status-failed',
			'paused': 'status-paused',
			'cancelled': 'status-cancelled'
		}
		return classMap[status] || 'status-default'
	}

	const getStatusText = (status) => {
		const textMap = {
			'waiting': '等待中',
			'printing': '打印中',
			'completed': '已完成',
			'failed': '失败',
			'paused': '已暂停',
			'cancelled': '已取消'
		}
		return textMap[status] || '未知'
	}

	const formatTime = (time) => {
		if (!time) return ''
		const date = new Date(time)
		return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
	}
</script>

<style scoped>
	
	/* width: unset或width: initial 可以重置元素的width属性，使其不再继承父元素的宽度设置 */
	uni-scroll-view{
		display: block;
		width: unset;
	}
	/* 样式保持不变 */
	.print-queue {
		background-color: #f5f5f5;
		min-height: 100vh;
	}

	.queue-stats {
		display: flex;
		background: white;
		margin: 20rpx;
		border-radius: 16rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
	}

	.stat-card {
		flex: 1;
		text-align: center;
		padding: 40rpx 20rpx;
		border-right: 1px solid #eee;
	}

	.stat-card:last-child {
		border-right: none;
	}

	.stat-number {
		font-size: 48rpx;
		font-weight: bold;
		color: #667eea;
		margin-bottom: 10rpx;
		display: block;
	}

	.stat-label {
		font-size: 24rpx;
		color: #999;
	}

	.control-bar {
		background: white;
		margin: 0 20rpx 20rpx;
		border-radius: 16rpx;
		padding: 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.device-selector {
		flex: 1;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx;
		background: #f8f9fa;
		border-radius: 12rpx;
		margin-right: 30rpx;
	}

	.device-selector text:first-child {
		font-size: 28rpx;
		color: #333;
	}

	.arrow {
		font-size: 20rpx;
		color: #666;
	}

	.queue-actions {
		display: flex;
		gap: 16rpx;
	}

	.queue-actions button {
		border-radius: 10rpx;
		border: none;
		font-size: 24rpx;
		min-width: 100rpx;
	}

	.pause-btn {
		background: #fff3e0;
		color: #ff9800;
	}

	.resume-btn {
		background: #e8f5e8;
		color: #4caf50;
	}

	.clear-btn {
		background: #fce4ec;
		color: #e91e63;
	}

	.queue-actions button:disabled {
		opacity: 0.5;
	}

	.status-filter {
		display: flex;
		background: white;
		margin: 0 20rpx 20rpx;
		border-radius: 16rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.filter-item {
		flex: 1;
		text-align: center;
		padding: 30rpx 20rpx;
		font-size: 26rpx;
		color: #666;
		position: relative;
	}

	.filter-item.active {
		color: #667eea;
		font-weight: bold;
	}

	.filter-item.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 50rpx;
		height: 4rpx;
		background: #667eea;
		border-radius: 2rpx;
	}

	.queue-list {
		height: calc(100vh - 400rpx);
		padding: 0 20rpx;
	}

	.queue-item {
		background: white;
		border-radius: 16rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}

	.item-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.order-info {
		flex: 1;
	}

	.order-number {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.queue-number {
		font-size: 22rpx;
		color: #999;
	}

	.item-status {
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
		font-size: 22rpx;
		color: white;
	}

	.status-waiting {
		background: #ff9800;
	}

	.status-printing {
		background: #2196f3;
	}

	.status-completed {
		background: #4caf50;
	}

	.status-failed {
		background: #f44336;
	}

	.status-paused {
		background: #9e9e9e;
	}

	.status-cancelled {
		background: #795548;
	}

	.item-content {
		margin-bottom: 24rpx;
	}

	.document-info {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16rpx;
		padding: 20rpx;
		background: #f8f9fa;
		border-radius: 12rpx;
	}

	.document-title {
		font-size: 26rpx;
		color: #333;
		flex: 1;
		margin-right: 20rpx;
	}

	.document-pages {
		font-size: 22rpx;
		color: #666;
	}

	.print-details {
		display: flex;
		justify-content: space-between;
		margin-bottom: 16rpx;
		font-size: 24rpx;
		color: #666;
	}

	.time-info {
		display: flex;
		gap: 30rpx;
		margin-bottom: 16rpx;
		font-size: 22rpx;
		color: #999;
	}

	.progress-section {
		margin-bottom: 16rpx;
	}

	.progress-bar {
		height: 8rpx;
		background: #e0e0e0;
		border-radius: 4rpx;
		margin-bottom: 12rpx;
		overflow: hidden;
	}

	.progress-fill {
		height: 100%;
		background: #2196f3;
		border-radius: 4rpx;
		transition: width 0.3s ease;
	}

	.progress-text {
		font-size: 22rpx;
		color: #2196f3;
		text-align: right;
	}

	.error-section {
		padding: 20rpx;
		background: #ffebee;
		border-radius: 12rpx;
		margin-bottom: 16rpx;
	}

	.error-message {
		font-size: 24rpx;
		color: #f44336;
		line-height: 1.4;
	}

	.item-actions {
		display: flex;
		gap: 12rpx;
		justify-content: flex-end;
		flex-wrap: wrap;
	}

	.item-actions button {
		border-radius: 10rpx;
		border: none;
		font-size: 22rpx;
		min-width: 100rpx;
	}

	.priority-btn {
		background: #e3f2fd;
		color: #2196f3;
	}

	.pause-item-btn {
		background: #fff3e0;
		color: #ff9800;
	}

	.resume-item-btn {
		background: #e8f5e8;
		color: #4caf50;
	}

	.retry-btn {
		background: #f3e5f5;
		color: #9c27b0;
	}

	.cancel-btn {
		background: #fce4ec;
		color: #e91e63;
	}

	.detail-btn {
		background: #f5f5f5;
		color: #666;
	}

	.loading-more,
	.no-more,
	.empty-state {
		text-align: center;
		padding: 40rpx;
		color: #999;
		font-size: 26rpx;
	}
</style>