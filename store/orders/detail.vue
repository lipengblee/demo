<template>
	<s-layout title="门店管理">
		<view class="order-detail">
			<!-- 订单状态 -->
			<view class="status-section">
				<view class="status-header">
					<text class="status-text" :class="getStatusClass(orderInfo.printStatus || orderInfo.status)">
						{{ getStatusText(orderInfo.printStatus || orderInfo.status) }}
					</text>
					<text class="order-no">订单号: {{ orderInfo.no }}</text>
				</view>
				<view class="progress-bar" v-if="orderInfo.printStatus === 'printing'">
					<view class="progress-fill" :style="{ width: printProgress + '%' }"></view>
				</view>
				<text v-if="orderInfo.printStatus === 'printing'" class="progress-text">
					打印进度: {{ printProgress }}%
				</text>
			</view>

			<!-- 客户信息 -->
			<view class="info-section">
				<view class="section-title">客户信息</view>
				<view class="info-row">
					<text class="label">姓名:</text>
					<text class="value">{{ orderInfo.receiverName }}</text>
				</view>
				<view class="info-row">
					<text class="label">电话:</text>
					<text class="value" @click="callCustomer">{{ orderInfo.receiverMobile }}</text>
				</view>
				<view class="info-row" v-if="orderInfo.receiverDetailAddress">
					<text class="label">地址:</text>
					<text class="value">{{ orderInfo.receiverAreaName }}{{ orderInfo.receiverDetailAddress }}</text>
				</view>
			</view>

			<!-- 商品信息 -->
			<view class="info-section">
				<view class="section-title">商品信息</view>
				<view class="product-list">
					<view class="product-item" v-for="item in orderInfo.items" :key="item.id">
						<image class="product-image" :src="item.picUrl" mode="aspectFill"></image>
						<view class="product-info">
							<text class="product-name">{{ item.spuName }}</text>
							<text class="product-spec" v-if="item.properties">{{ item.properties }}</text>
							<view class="product-meta">
								<text class="quantity">x{{ item.count }}</text>
								<view style="display: flex; justify-content: flex-end; align-items: center;">
									<text class="c_text">单价：</text>
									<text class="price">¥{{ (item.price / 100).toFixed(2) }}</text>
								</view>
							</view>
						</view>

						<!-- 打印文档信息 -->
						<view v-if="item.printDocument" class="print-document-section">
							<view class="document-header">
								<text class="document-title">{{ item.printDocument.title }}</text>
							</view>
							<view class="document-actions">
								<button class="preview-btn" @click="previewDocument(item.printDocument)">预览</button>
								<button class="download-btn" @click="downloadDocument(item.printDocument)">下载</button>
							</view>
						</view>
					</view>
				</view>
			</view>

			<!-- 订单金额 -->
			<view class="info-section">
				<view class="section-title">订单金额</view>
				<view class="price-row">
					<text class="label">商品总价:</text>
					<text class="value">¥{{ (orderInfo.totalPrice / 100).toFixed(2) }}</text>
				</view>
				<view class="price-row" v-if="orderInfo.discountPrice > 0">
					<text class="label">优惠金额:</text>
					<text class="value discount">-¥{{ (orderInfo.discountPrice / 100).toFixed(2) }}</text>
				</view>
				<view class="price-row" v-if="orderInfo.deliveryPrice > 0">
					<text class="label">运费:</text>
					<text class="value">¥{{ (orderInfo.deliveryPrice / 100).toFixed(2) }}</text>
				</view>
				<view class="price-row total">
					<text class="label">实付款:</text>
					<text class="value">¥{{ (orderInfo.payPrice / 100).toFixed(2) }}</text>
				</view>
			</view>

			<!-- 打印设备信息 -->
			<view class="info-section" v-if="assignedDevice">
				<view class="section-title">打印设备</view>
				<view class="device-card">
					<view class="device-info">
						<text class="device-name">{{ assignedDevice.name }}</text>
						<text class="device-type">{{ assignedDevice.type }}</text>
					</view>
					<view class="device-status">
						<view class="status-indicator" :class="assignedDevice.status"></view>
						<text>{{ getDeviceStatusText(assignedDevice.status) }}</text>
					</view>
				</view>
			</view>

			<!-- 订单时间线 -->
			<view class="info-section">
				<view class="section-title">订单跟踪</view>
				<view class="timeline">
					<view class="timeline-item" v-for="(log, index) in orderLogs" :key="index">
						<view class="timeline-dot" :class="{ active: index === 0 }"></view>
						<view class="timeline-content">
							<text class="timeline-title">{{ log.title }}</text>
							<text class="timeline-time">{{ formatDateTime(log.createTime) }}</text>
							<text v-if="log.content" class="timeline-desc">{{ log.content }}</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 操作按钮 -->
			<view class="action-bar">
				<button v-if="orderInfo.printStatus === 'pending'" @click="showAssignPrinter" class="primary-btn">
					指派打印
				</button>
				<button v-if="orderInfo.printStatus === 'printing'" @click="pausePrinting" class="warning-btn">
					暂停打印
				</button>
				<button v-if="orderInfo.printStatus === 'paused'" @click="resumePrinting" class="primary-btn">
					继续打印
				</button>
				<button v-if="orderInfo.printStatus === 'completed'" @click="markAsDelivered" class="success-btn">
					标记发货
				</button>
			</view>

			<!-- 指派打印机弹窗 -->
			<uni-popup ref="assignPopup" type="bottom">
				<view class="assign-popup">
					<view class="popup-header">
						<text class="popup-title">选择打印设备</text>
						<text class="popup-close" @click="closeAssignPopup">×</text>
					</view>
					<view class="device-list">
						<view class="device-option" v-for="device in availableDevices" :key="device.id"
							@click="assignToPrinter(device)">
							<view class="device-info">
								<view class="device-name">{{ device.name }}</view>
								<view class="device-type">{{ device.type }}</view>
							</view>
							<view class="device-status">
								<view class="status-indicator" :class="device.status"></view>
								<text>队列: {{ device.queueCount }}</text>
							</view>
						</view>
					</view>
				</view>
			</uni-popup>
		</view>


	</s-layout>
</template>

<script setup>
	import {
		ref,
		onMounted,
		onUnmounted
	} from 'vue'
	import sheep from '@/sheep'
	import StoreApi from '@/sheep/api/store'
	import uniPopup from '@dcloudio/uni-ui/lib/uni-popup/uni-popup.vue'

	// 响应式数据
	const orderId = ref('')
	const orderInfo = ref({
		items: []
	})
	const orderLogs = ref([])
	const assignedDevice = ref('')
	const availableDevices = ref([])
	const printProgress = ref(0)
	const currentDocument = ref(null)
	const refreshTimer = ref(null)
	const previewLoading = ref(false)
	const assignPopup = ref(null)

	const props = defineProps({
		id: {
			type: Number,
			required: true
		}
	});

	// 生命周期
	onMounted(() => {
		orderId.value = props.id
		loadOrderDetail()
		loadDevices()
		startProgressPolling()
	})

	onUnmounted(() => {
		refreshTimer.value && clearInterval(refreshTimer.value)
	})

	// 方法
	const loadOrderDetail = async () => {
		try {
			const res = await StoreApi.getOrderDetail(`${orderId.value}`)
			if (res.code === 0) {
				orderInfo.value = res.data
				orderLogs.value = res.data.logs || []
				assignedDevice.value = res.data.assignedDevice
				printProgress.value = res.data.printProgress || 0
			}
		} catch (error) {
			console.error('获取订单详情失败:', error)
			uni.showToast({
				title: '获取订单详情失败',
				icon: 'none'
			})
		}
	}

	const loadDevices = async () => {
		try {
			const res = await StoreApi.getDevices()
			if (res.code === 0) {
				availableDevices.value = res.data.filter(device => device.status === 'online')
			}
		} catch (error) {
			console.error('获取设备列表失败:', error)
		}
	}

	const startProgressPolling = () => {
		if (orderInfo.value.printStatus === 'printing') {
			refreshTimer.value = setInterval(() => updatePrintProgress(), 3000)
		}
	}

	const updatePrintProgress = async () => {
		try {
			const res = await sheep.$http.get(`/api/store/orders/${orderId.value}/progress`)
			if (res.data.code === 200) {
				printProgress.value = res.data.data.progress
				if (res.data.data.status !== 'printing') {
					clearInterval(refreshTimer.value)
					loadOrderDetail()
				}
			}
		} catch (error) {
			console.error('获取打印进度失败:', error)
		}
	}

	const callCustomer = () => {
		uni.makePhoneCall({
			phoneNumber: orderInfo.value.receiverMobile
		})
	}

	const showAssignPrinter = () => {
		if (assignPopup.value) {
			assignPopup.value.open();
		} else {
			console.error('assignPopup 实例未找到');
			setTimeout(() => assignPopup.value?.open(), 300);
		}
	}

	const closeAssignPopup = () => {
		assignPopup.value.close()
	}

	const assignToPrinter = async (device) => {
		try {
			const res = await StoreApi.assignOrderToDevice({
				orderId: orderId.value,
				deviceId: device.id
			})
			if (res.code === 0) {
				uni.showToast({
					title: '指派成功',
					icon: 'success'
				})
				closeAssignPopup()
				loadOrderDetail()
				startProgressPolling()
			}
		} catch (error) {
			console.error('指派打印机失败:', error)
			uni.showToast({
				title: '指派失败',
				icon: 'none'
			})
		}
	}

	const pausePrinting = () => {
		uni.showModal({
			title: '确认暂停',
			content: '确定要暂停打印这个订单吗？',
			success: async (res) => {
				if (res.confirm) {
					try {
						await sheep.$http.post(`/api/store/orders/${orderId.value}/pause`)
						uni.showToast({
							title: '已暂停打印',
							icon: 'success'
						})
						clearInterval(refreshTimer.value)
						loadOrderDetail()
					} catch (error) {
						uni.showToast({
							title: '暂停失败',
							icon: 'none'
						})
					}
				}
			}
		})
	}

	const resumePrinting = async () => {
		try {
			await sheep.$http.post(`/api/store/orders/${orderId.value}/resume`)
			uni.showToast({
				title: '已继续打印',
				icon: 'success'
			})
			loadOrderDetail()
			startProgressPolling()
		} catch (error) {
			uni.showToast({
				title: '操作失败',
				icon: 'none'
			})
		}
	}

	const markAsDelivered = () => {
		uni.showModal({
			title: '确认发货',
			content: '确定要标记此订单为已发货吗？',
			success: async (res) => {
				if (res.confirm) {
					try {
						await sheep.$http.post(`/api/store/orders/${orderId.value}/deliver`)
						uni.showToast({
							title: '已标记发货',
							icon: 'success'
						})
						loadOrderDetail()
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

	const downloadDocument = async (document) => {
		try {
			uni.showLoading({
				title: '下载中...'
			})
			uni.downloadFile({
				url: document.url,
				success: (res) => {
					if (res.statusCode === 200) {
						uni.openDocument({
							filePath: res.tempFilePath,
							success: () => uni.hideLoading(),
							fail: () => {
								uni.hideLoading()
								uni.showToast({
									title: '打开文档失败',
									icon: 'none'
								})
							}
						})
					}
				},
				fail: () => {
					uni.hideLoading()
					uni.showToast({
						title: '下载失败',
						icon: 'none'
					})
				}
			})
		} catch (error) {
			uni.hideLoading()
			console.error('下载文档失败:', error)
		}
	}

	const getStatusClass = (status) => {
		const statusMap = {
			'pending': 'status-pending',
			'printing': 'status-printing',
			'completed': 'status-completed',
			'delivered': 'status-delivered',
			'cancelled': 'status-cancelled',
			'paused': 'status-paused'
		}
		return statusMap[status] || 'status-default'
	}

	const getStatusText = (status) => {
		const statusMap = {
			'pending': '等待处理',
			'assigned': '已分配打印',
			'printing': '正在打印',
			'completed': '打印完成',
			'delivered': '已发货',
			'cancelled': '已取消',
			'paused': '暂停中'
		}
		return statusMap[status] || '未知状态'
	}

	const getDeviceStatusText = (status) => {
		const statusMap = {
			'online': '在线',
			'offline': '离线',
			'busy': '忙碌',
			'error': '故障'
		}
		return statusMap[status] || '未知'
	}

	const formatDateTime = (dateTime) => {
		if (!dateTime) return ''
		const date = new Date(dateTime)
		return `${date.getFullYear()}/${String(date.getMonth() + 1).padStart(2, '0')}/${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
	}

	const previewDocument = (document) => {
		if (!document || !document.url) {
			uni.showToast({
				title: '文档信息不完整',
				icon: 'none'
			});
			return;
		}
		sheep.$router.go('/pages/print/preview', {
			title: document.title,
			url: document.url,
		});
	}
</script>

<style scoped>
	.uni-popup {
		z-index: 9999 !important;
	}

	.order-detail {
		background-color: #f5f5f5;
		min-height: 100vh;
		padding-bottom: 120rpx;
	}

	/* 状态区域 */
	.status-section {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 40rpx 30rpx;
		color: white;
		border-radius: 0 0 24rpx 24rpx;
	}

	.status-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.status-text {
		font-size: 36rpx;
		font-weight: bold;
	}

	.order-no {
		font-size: 24rpx;
		opacity: 0.8;
	}

	.progress-bar {
		height: 8rpx;
		background: rgba(255, 255, 255, 0.3);
		border-radius: 4rpx;
		margin-bottom: 16rpx;
		overflow: hidden;
	}

	.progress-fill {
		height: 100%;
		background: white;
		border-radius: 4rpx;
		transition: width 0.3s ease;
	}

	.progress-text {
		font-size: 24rpx;
		opacity: 0.9;
	}

	/* 信息区块通用样式 */
	.info-section {
		background: white;
		margin: 20rpx;
		border-radius: 20rpx;
		padding: 30rpx;
		box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.06);
	}

	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 30rpx;
		padding-bottom: 20rpx;
		border-bottom: 2px solid #f0f0f0;
	}

	/* 信息行样式 */
	.info-row,
	.price-row {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		font-size: 28rpx;
	}

	.info-row:last-child,
	.price-row:last-child {
		margin-bottom: 0;
	}

	.label {
		color: #666;
		flex-shrink: 0;
		width: 160rpx;
	}

	.value {
		color: #333;
		flex: 1;
		text-align: right;
	}

	.price-row.total {
		border-top: 2px solid #f0f0f0;
		padding-top: 20rpx;
		margin-top: 20rpx;
		font-weight: bold;
		font-size: 32rpx;
	}

	.discount {
		color: #f44336;
	}

	/* 商品列表 */
	.product-list {
		margin-top: 20rpx;
	}

	.product-item {
		display: flex;
		flex-wrap: wrap;
		margin-bottom: 30rpx;
		padding-bottom: 30rpx;
		border-bottom: 1px solid #f0f0f0;
	}

	.product-item:last-child {
		margin-bottom: 0;
		padding-bottom: 0;
		border-bottom: none;
	}

	.product-image {
		width: 120rpx;
		height: 120rpx;
		border-radius: 12rpx;
		margin-right: 24rpx;
		flex-shrink: 0;
	}

	.product-info {
		flex: 1;
	}

	.product-name {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
		display: block;
		margin-bottom: 12rpx;
		line-height: 1.4;
	}

	.product-spec {
		font-size: 24rpx;
		color: #999;
		display: block;
		margin-bottom: 16rpx;
	}

	.product-meta {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx dashed #eee;
	}

	.quantity {
		font-size: 24rpx;
		color: #666;
	}

	.c_text {
		font-size: 24rpx;
		color: #666;
	}

	.price {
		font-size: 28rpx;
		font-weight: bold;
		color: #f44336;
	}

	/* 打印文档区域 */
	.print-document-section {
		width: 100%;
		margin-top: 20rpx;
		padding: 24rpx;
		background: #f8fafc;
		border-radius: 16rpx;
		border-left: 4rpx solid #667eea;
		display: flex;
		justify-content: space-between;
		align-items: center;
		flex-wrap: wrap;
	}

	.document-header {
		display: flex;
		align-items: center;
		flex: 1;
		margin-right: 30rpx;
	}

	.document-title {
		font-size: 26rpx;
		color: #4a5568;
		font-weight: 500;
	}

	.document-actions {
		display: flex;
		gap: 12px;
		justify-content: flex-end;
	}

	.preview-btn,
	.download-btn {
		border: none;
		font-size: 24rpx;
		transition: all 0.2s ease;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.preview-btn {
		background: #e6f7ff;
		color: #1890ff;
		border: 1rpx solid #91d5ff;
	}

	.download-btn {
		background: #f6ffed;
		color: #52c41a;
		border: 1rpx solid #b7eb8f;
	}

	/* 设备信息 */
	.device-card {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 24rpx;
		background: #f8f9fa;
		border-radius: 12rpx;
	}

	.device-info {
		flex: 1;
	}

	.device-name {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.device-type {
		font-size: 24rpx;
		color: #666;
	}

	.device-status {
		display: flex;
		align-items: center;
		font-size: 24rpx;
		color: #666;
	}

	.status-indicator {
		width: 16rpx;
		height: 16rpx;
		border-radius: 50%;
		margin-right: 12rpx;
	}

	.status-indicator.online {
		background: #4caf50;
	}

	.status-indicator.offline {
		background: #f44336;
	}

	.status-indicator.busy {
		background: #ff9800;
	}

	.status-indicator.error {
		background: #e91e63;
	}

	/* 时间线 */
	.timeline {
		position: relative;
		padding-left: 40rpx;
	}

	.timeline::before {
		content: '';
		position: absolute;
		left: 16rpx;
		top: 0;
		bottom: 0;
		width: 2rpx;
		background: #e0e0e0;
	}

	.timeline-item {
		position: relative;
		margin-bottom: 40rpx;
	}

	.timeline-item:last-child {
		margin-bottom: 0;
	}

	.timeline-dot {
		position: absolute;
		left: -32rpx;
		top: 8rpx;
		width: 16rpx;
		height: 16rpx;
		border-radius: 50%;
		background: #e0e0e0;
		border: 4rpx solid white;
		z-index: 1;
	}

	.timeline-dot.active {
		background: #667eea;
	}

	.timeline-content {
		margin-left: 20rpx;
	}

	.timeline-title {
		font-size: 26rpx;
		font-weight: bold;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.timeline-time {
		font-size: 22rpx;
		color: #999;
		display: block;
		margin-bottom: 8rpx;
	}

	.timeline-desc {
		font-size: 24rpx;
		color: #666;
		line-height: 1.4;
	}

	/* 操作按钮 */
	.action-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: white;
		padding: 30rpx 30rpx;
		box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.1);
		display: flex;
		gap: 20rpx;
	}

	.action-bar button {
		flex: 1;
		height: 80rpx;
		border-radius: 44rpx;
		border: none;
		font-size: 28rpx;
		font-weight: bold;
		z-index: 9999 !important;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.12);
		transition: all 0.2s ease;
	}

	.primary-btn {
		background: #667eea;
		color: white;
	}

	.warning-btn {
		background: #ff9800;
		color: white;
	}

	.success-btn {
		background: #4caf50;
		color: white;
	}

	/* 指派弹窗 */
	.assign-popup {
		background: white;
		border-radius: 20rpx 20rpx 0 0;
		max-height: 80vh;
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
	}

	.popup-close {
		font-size: 48rpx;
		color: #999;
		line-height: 1;
	}

	.device-list {
		padding: 20rpx;
		max-height: 60vh;
		overflow-y: auto;
	}

	.device-option {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		margin-bottom: 16rpx;
		background: #f8f9fa;
		border-radius: 12rpx;
	}

	.device-option:active {
		background: #e3f2fd;
	}

	/* 预览弹窗 */
	.preview-popup {
		background: white;
		width: 90vw;
		height: 80vh;
		border-radius: 16rpx;
		overflow: hidden;
		display: flex;
		flex-direction: column;
	}

	.preview-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 30rpx;
		border-bottom: 1px solid #eee;
		background: #f8f9fa;
	}

	.preview-title {
		font-size: 32rpx;
		font-weight: bold;
		flex: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		margin-right: 20rpx;
	}

	.preview-actions {
		display: flex;
		align-items: center;
	}

	.download-btn-mini {
		background: transparent;
		border: none;
		padding: 8rpx 16rpx;
		margin-right: 16rpx;
		font-size: 32rpx;
		border-radius: 12rpx;
		transition: all 0.2s ease;
	}

	.preview-close {
		font-size: 48rpx;
		color: #999;
		line-height: 1;
		padding: 8rpx;
	}

	.preview-content {
		flex: 1;
		overflow: auto;
		display: flex;
		justify-content: center;
		align-items: center;
		position: relative;
	}

	.loading-section {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100%;
	}

	.loading-text {
		font-size: 28rpx;
		color: #666;
	}

	.preview-image {
		max-width: 100%;
		max-height: 100%;
	}

	.pdf-viewer {
		width: 100%;
		height: 100%;
	}

	.document-placeholder {
		text-align: center;
		color: #666;
		padding: 40rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.document-type {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 10rpx;
	}

	.document-hint {
		font-size: 24rpx;
		color: #999;
		margin-bottom: 30rpx;
	}

	.download-btn-large {
		background: #667eea;
		color: white;
		border: none;
		border-radius: 40rpx;
		padding: 20rpx 40rpx;
		font-size: 28rpx;
	}

	/* 交互效果 */
	@media (hover: hover) {
		.preview-btn:hover {
			background: #bae7ff;
		}

		.download-btn:hover {
			background: #d9f7be;
		}
	}

	.action-bar button:active {
		transform: translateY(4rpx);
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.16);
	}

	.download-btn-mini:active {
		background: #f0f0f0;
	}

	.download-btn-large:active {
		transform: translateY(4rpx);
	}
</style>