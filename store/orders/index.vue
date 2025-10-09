<template>
	<s-layout title="订单管理">
		<view class="orders-page">
			<!-- 搜索和筛选 -->
			<view class="search-container">
				<view class="search-header">
					<!-- 返回按钮 -->
					<uni-icons class="back-icon" type="arrowleft" size="24" color="#333"
						@click="navigateBack"></uni-icons>
					<view class="search-box">
						<uni-icons class="search-icon" type="search" size="18" color="#999"></uni-icons>
						<input class="search-input" placeholder="搜索订单号或客户姓名" v-model="searchKeyword"
							@confirm="searchOrders" />
						<!-- 清除按钮 -->
						<uni-icons v-if="searchKeyword" class="clear-icon" type="clear" size="18" color="#999"
							@click="clearSearch">
						</uni-icons>
						<text class="search-btn" @click="searchOrders">搜索</text>
					</view>
				</view>
				<scroll-view class="filter-tabs" scroll-x>
					<text class="filter-tab" :class="{ active: currentStatus === item.value }"
						v-for="item in statusTabs" :key="item.value" @click="changeStatus(item.value)">
						{{ item.label }}
					</text>
				</scroll-view>
			</view>

			<!-- 订单列表 -->
			<scroll-view scroll-y class="order-list" @scrolltolower="loadMore" :refresher-enabled="true"
				@refresherrefresh="onRefresh" :refresher-triggered="refreshing">
				<view class="order-item" v-for="order in orderList" :key="order.id" @click="viewOrderDetail(order)">
					<view class="order-header">
						<view class="order-info">
							<text class="order-no">订单号: {{ order.no }}</text>
							<text class="order-time">{{ formatDateTime(order.createTime) }}</text>
						</view>
						<text class="order-status" :class="getStatusClass(order.printStatus || order.status)">
							{{ getStatusText(order.printStatus || order.status) }}
						</text>
					</view>

					<view class="customer-info">
						<text class="customer-name">客户: {{ order.receiverName }}</text>
						<text class="customer-phone">{{ order.receiverMobile }}</text>
					</view>

					<view class="order-content">
						<view class="product-info">
							<view class="product-item" v-for="item in order.items" :key="item.id">
								<image class="product-image" :src="item.picUrl" mode="aspectFill"></image>
								<view class="product-details">
									<text class="product-name">{{ item.spuName }}</text>
									<text class="product-spec">{{ item.properties }}</text>
									<view class="product-meta">
										<text class="product-quantity">数量: {{ item.count }}</text>
										<!-- 显示打印文档信息 -->
										<!-- <text v-if="item.printDocument" class="print-document">
										文档: {{ item.printDocument.title }}
									</text> -->
									</view>
								</view>
							</view>
						</view>
					</view>

					<view class="order-footer">
						<view class="price-info">
							<text class="total-amount">合计: ¥{{ (order.payPrice / 100).toFixed(2) }}</text>
						</view>
						<view class="action-buttons">
							<button v-if="order.printStatus === 'pending'" @click.stop="showAssignPrinter(order)"
								class="assign-btn">
								指派打印
							</button>
							<button v-if="order.printStatus === 'printing'" @click.stop="viewPrintProgress(order)"
								class="progress-btn">
								查看进度
							</button>
							<button v-if="order.printStatus === 'printing'" @click.stop="pausePrinting(order)"
								class="pause-btn">
								暂停打印
							</button>
							<button v-if="order.printStatus === 'completed'" @click.stop="markAsDelivered(order)"
								class="deliver-btn">
								标记发货
							</button>
						</view>
					</view>
				</view>

				<view v-if="loading" class="loading-more">
					<uni-load-more status="loading" content="加载中..."></uni-load-more>
				</view>

				<view v-if="!hasMore && orderList.length > 0" class="no-more">
					<text>没有更多数据了</text>
				</view>

				<s-empty v-if="orderList.length === 0 && !loading" icon="/static/order-empty.png" text="暂无订单数据" />
			</scroll-view>

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
								<text class="queue-text">队列: {{ device.queueCount }}</text>
							</view>
						</view>
					</view>
					<view v-if="availableDevices.length === 0" class="no-device">
						<text>暂无可用设备</text>
					</view>
				</view>
			</uni-popup>
		</view>
	</s-layout>
</template>

<script>
	import sheep from '@/sheep';
	import StoreApi from '@/sheep/api/store';
	import uniPopup from '@dcloudio/uni-ui/lib/uni-popup/uni-popup.vue'
	export default {
		components: {
			uniPopup
		},
		data() {
			return {
				searchKeyword: '',
				currentStatus: 'all',
				statusTabs: [{
						label: '全部',
						value: 'all'
					},
					{
						label: '待处理',
						value: 'pending'
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
						label: '已发货',
						value: 'delivered'
					}
				],
				orderList: [],
				currentPage: 1,
				pageSize: 5,
				loading: false,
				refreshing: false,
				hasMore: true,
				selectedOrder: null,
				availableDevices: []
			}
		},

		onLoad(options) {
			if (options.status) {
				this.currentStatus = options.status
			}
			this.loadOrderList(true)
			this.loadDevices()
		},

		methods: {
			// 添加返回方法
			navigateBack() {
				sheep.$router.back()
			},
			// 清除搜索内容
			clearSearch() {
				this.searchKeyword = '';
				this.searchOrders(); // 清除后立即触发搜索更新列表
			},

			async loadOrderList(reset = false) {
				if (this.loading) return

				this.loading = true

				if (reset) {
					this.currentPage = 1
					this.orderList = []
					this.hasMore = true
				}

				try {
					const params = {
						page: this.currentPage,
						pageSize: this.pageSize,
						status: this.currentStatus === 'delivered' ? this.currentStatus : undefined,
						printStatus: (this.currentStatus !== 'all' && this.currentStatus !== 'delivered') ? this
							.currentStatus : undefined,
						keyword: this.searchKeyword || undefined
					}

					const res = await StoreApi.getOrders(params)

					if (res.code === 0) {
						const {
							list,
							total
						} = res.data

						if (reset) {
							this.orderList = list
						} else {
							this.orderList.push(...list)
						}

						this.hasMore = this.orderList.length < total
						this.currentPage++
					}
				} catch (error) {
					console.error('获取订单列表失败:', error)
					uni.showToast({
						title: '获取订单失败',
						icon: 'none'
					})
				} finally {
					this.loading = false
					this.refreshing = false
				}
			},

			async loadDevices() {
				try {
					const res = await StoreApi.getDevices()
					if (res.code === 0) {
						this.availableDevices = res.data.filter(device => device.status === 'online')
					}
				} catch (error) {
					console.error('获取设备列表失败:', error)
				}
			},

			changeStatus(status) {
				this.currentStatus = status
				this.loadOrderList(true)
			},

			searchOrders() {
				this.loadOrderList(true)
			},

			loadMore() {
				if (this.hasMore && !this.loading) {
					this.loadOrderList()
				}
			},

			onRefresh() {
				this.refreshing = true
				this.loadOrderList(true)
			},

			viewOrderDetail(order) {
				uni.navigateTo({
					url: `/pages/store/orders/detail?id=${order.id}`
				})
			},

			showAssignPrinter(order) {
				this.selectedOrder = order
				this.$refs.assignPopup.open()
			},

			closeAssignPopup() {
				this.$refs.assignPopup.close()
				this.selectedOrder = null
			},

			async assignToPrinter(device) {
				if (!this.selectedOrder) return

				try {
					const res = await StoreApi.assignOrderToDevice({
						orderId: this.selectedOrder.id,
						deviceId: device.id
					})

					if (res.code === 0) {
						uni.showToast({
							title: '指派成功',
							icon: 'success'
						})
						this.closeAssignPopup()
						this.loadOrderList(true)
					}
				} catch (error) {
					console.error('指派打印机失败:', error)
					uni.showToast({
						title: '指派失败',
						icon: 'none'
					})
				}
			},

			viewPrintProgress(order) {
				uni.navigateTo({
					url: `/pages/store/orders/progress?id=${order.id}`
				})
			},

			async pausePrinting(order) {
				uni.showModal({
					title: '确认暂停',
					content: '确定要暂停打印这个订单吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								await this.$http.post(`/api/store/orders/${order.id}/pause`)
								uni.showToast({
									title: '已暂停打印',
									icon: 'success'
								})
								this.loadOrderList(true)
							} catch (error) {
								uni.showToast({
									title: '暂停失败',
									icon: 'none'
								})
							}
						}
					}
				})
			},

			async markAsDelivered(order) {
				uni.showModal({
					title: '确认发货',
					content: '确定要标记此订单为已发货吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								await this.$http.post(`/api/store/orders/${order.id}/deliver`)
								uni.showToast({
									title: '已标记发货',
									icon: 'success'
								})
								this.loadOrderList(true)
							} catch (error) {
								uni.showToast({
									title: '操作失败',
									icon: 'none'
								})
							}
						}
					}
				})
			},

			getStatusClass(status) {
				const statusMap = {
					'pending': 'status-pending',
					'printing': 'status-printing',
					'completed': 'status-completed',
					'delivered': 'status-delivered',
					'cancelled': 'status-cancelled'
				}
				return statusMap[status] || 'status-default'
			},

			getStatusText(status) {
				const statusMap = {
					'pending': '待处理',
					'printing': '打印中',
					'completed': '打印完成',
					'delivered': '已发货',
					'cancelled': '已取消'
				}
				return statusMap[status] || '未知'
			},

			formatDateTime(dateTime) {
				if (!dateTime) return ''
				const date = new Date(dateTime)
				return `${date.getMonth() + 1}/${date.getDate()} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
			}


		}

	}
</script>

<style scoped>
	.orders-page {
		background-color: #f7f7f7;
		min-height: 100vh;
	}

	.search-container {
		background: white;
		padding: 20rpx 24rpx;
		position: sticky;
		top: 0;
		z-index: 10;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
	}

	.search-box {
		display: flex;
		align-items: center;
		background: #f5f5f5;
		border-radius: 12rpx;
		padding: 0 24rpx;
		margin-bottom: 24rpx;
		height: 80rpx;
	}

	.search-icon {
		margin-right: 12rpx;
	}

	.search-input {
		flex: 1;
		height: 80rpx;
		font-size: 28rpx;
		color: #333;
	}

	.search-btn {
		color: #2979ff;
		font-size: 28rpx;
		font-weight: 500;
		padding: 8rpx 0 8rpx 16rpx;
	}

	.filter-tabs {
		white-space: nowrap;
		width: 100%;
	}

	.filter-tab {
		display: inline-block;
		text-align: center;
		padding: 16rpx 28rpx;
		font-size: 26rpx;
		color: #666;
		position: relative;
		margin-right: 16rpx;
	}

	.filter-tab:last-child {
		margin-right: 0;
	}

	.filter-tab.active {
		color: #2979ff;
		font-weight: 500;
	}

	.filter-tab.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 40rpx;
		height: 4rpx;
		background: #2979ff;
		border-radius: 4rpx;
	}

	.order-list {
		height: calc(100vh - 180rpx);
		padding: 20rpx 24rpx;
		box-sizing: border-box;
	}

	.order-item {
		background: white;
		border-radius: 16rpx;
		margin-bottom: 24rpx;
		padding: 32rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
		transition: all 0.2s;
	}

	.order-item:active {
		background: #fafafa;
		transform: translateY(2rpx);
	}

	.order-header {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		margin-bottom: 24rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.order-info {
		flex: 1;
	}

	.order-no {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 12rpx;
	}

	.order-time {
		font-size: 24rpx;
		color: #999;
	}

	.order-status {
		font-size: 22rpx;
		padding: 6rpx 16rpx;
		border-radius: 20rpx;
		color: white;
		white-space: nowrap;
		font-weight: 500;
	}

	.status-pending {
		background: linear-gradient(45deg, #ff9800, #ffa726);
	}

	.status-printing {
		background: linear-gradient(45deg, #2196f3, #42a5f5);
	}

	.status-completed {
		background: linear-gradient(45deg, #4caf50, #66bb6a);
	}

	.status-delivered {
		background: linear-gradient(45deg, #9c27b0, #ab47bc);
	}

	.status-cancelled {
		background: linear-gradient(45deg, #f44336, #ef5350);
	}

	.customer-info {
		display: flex;
		justify-content: space-between;
		margin-bottom: 24rpx;
		padding: 20rpx;
		background: #f9f9f9;
		border-radius: 12rpx;
	}

	.customer-name {
		font-size: 26rpx;
		color: #333;
		font-weight: 500;
	}

	.customer-phone {
		font-size: 26rpx;
		color: #666;
	}

	.order-content {
		margin-bottom: 24rpx;
	}

	.product-item {
		display: flex;
		margin-bottom: 24rpx;
		padding-bottom: 24rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.product-item:last-child {
		margin-bottom: 0;
		padding-bottom: 0;
		border-bottom: none;
	}

	.product-image {
		width: 140rpx;
		height: 140rpx;
		border-radius: 8rpx;
		margin-right: 24rpx;
		flex-shrink: 0;
		background: #f5f5f5;
	}

	.product-details {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.product-name {
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
		display: block;
		margin-bottom: 8rpx;
		line-height: 1.4;
		/* 限制两行显示，超出显示省略号 */
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}

	.product-spec {
		font-size: 24rpx;
		color: #999;
		display: block;
		margin-bottom: 12rpx;
	}

	.product-meta {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.product-quantity {
		font-size: 24rpx;
		color: #666;
	}

	.print-document {
		font-size: 22rpx;
		color: #2979ff;
		background: #f0f7ff;
		padding: 4rpx 12rpx;
		border-radius: 6rpx;
	}

	.order-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-top: 1rpx solid #f0f0f0;
		padding-top: 24rpx;
	}

	.total-amount {
		font-size: 32rpx;
		font-weight: 600;
		color: #ff3b30;
	}

	.action-buttons {
		display: flex;
		gap: 16rpx;
	}

	.action-buttons button {
		font-size: 24rpx;
		padding: 3rpx 24rpx;
		border-radius: 20rpx;
		border: none;
		min-width: 120rpx;
		font-weight: 500;
		transition: all 0.2s;
	}

	.action-buttons button:active {
		opacity: 0.8;
		transform: scale(0.98);
	}

	.assign-btn {
		background: #2979ff;
		color: white;
	}

	.progress-btn {
		background: #2196f3;
		color: white;
	}

	.pause-btn {
		background: #ff9800;
		color: white;
	}

	.deliver-btn {
		background: #4caf50;
		color: white;
	}

	.loading-more,
	.no-more {
		text-align: center;
		padding: 40rpx;
		color: #999;
		font-size: 26rpx;
	}

	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 40rpx;
	}

	.empty-image {
		width: 240rpx;
		height: 240rpx;
		margin-bottom: 40rpx;
		opacity: 0.6;
	}

	.empty-text {
		font-size: 28rpx;
		color: #999;
	}

	.assign-popup {
		background: white;
		border-radius: 24rpx 24rpx 0 0;
		max-height: 80vh;
		padding-bottom: env(safe-area-inset-bottom);
	}

	.popup-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 32rpx;
		border-bottom: 1rpx solid #f0f0f0;
		position: sticky;
		top: 0;
		background: white;
		border-radius: 24rpx 24rpx 0 0;
		z-index: 1;
	}

	.popup-title {
		font-size: 32rpx;
		font-weight: 600;
	}

	.popup-close {
		font-size: 40rpx;
		color: #999;
		line-height: 1;
		padding: 16rpx;
	}

	.device-list {
		padding: 0 32rpx;
		max-height: 60vh;
		overflow-y: auto;
	}

	.device-option {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 32rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.device-option:last-child {
		border-bottom: none;
	}

	.device-option:active {
		background: #f9f9f9;
	}

	.device-info {
		flex: 1;
	}

	.device-name {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 8rpx;
	}

	.device-type {
		font-size: 24rpx;
		color: #999;
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
		box-shadow: 0 0 0 4rpx rgba(76, 175, 80, 0.2);
	}

	.status-indicator.offline {
		background: #f44336;
		box-shadow: 0 0 0 4rpx rgba(244, 67, 54, 0.2);
	}

	.status-indicator.busy {
		background: #ff9800;
		box-shadow: 0 0 0 4rpx rgba(255, 152, 0, 0.2);
	}

	.queue-text {
		font-size: 24rpx;
		color: #666;
	}

	.no-device {
		padding: 10rpx 0;
		text-align: center;
		color: #999;
		font-size: 28rpx;
	}

	/* 添加搜索头部布局样式 */
	.search-header {
		display: flex;
		align-items: center;
	}

	.back-icon {
		padding: 3rpx 3rpx 25rpx;
		margin-right: 16rpx;
		flex-shrink: 0;
	}

	.search-box {
		flex: 1;
		display: flex;
		align-items: center;
		background: #f5f5f5;
		border-radius: 12rpx;
		padding: 0 24rpx;
		height: 80rpx;
		position: relative;
	}

	.search-icon {
		margin-right: 12rpx;
		flex-shrink: 0;
	}

	.search-input {
		flex: 1;
		height: 80rpx;
		font-size: 28rpx;
		color: #333;
		padding-right: 20rpx;
	}

	.clear-icon {
		padding: 10rpx;
		flex-shrink: 0;
	}

	.search-btn {
		color: #2979ff;
		font-size: 28rpx;
		font-weight: 500;
		padding: 8rpx 0 8rpx 16rpx;
		flex-shrink: 0;
	}
</style>