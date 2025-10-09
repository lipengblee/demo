<template>
	<s-layout title="门店管理">
		<view class="store-management">
		  <!-- 顶部状态栏 -->
		  <view class="status-bar">
		    <view class="store-info">
		      <text class="store-name">{{ storeInfo.name }}</text>
		      <view class="status-badge" :class="storeInfo.status === 1 ? 'online' : 'offline'">
		        <text class="status-dot"></text>
		        <text class="status-text">{{ storeInfo.status === 1 ? '营业中' : '休息中' }}</text>
		      </view>
		    </view>
		    <view class="user-info">
		      <text class="user-name">{{ userInfo.name }}</text>
		      <view class="avatar">
		        <text class="avatar-text">{{ getUserInitials(userInfo.name) }}</text>
		      </view>
		    </view>
		  </view>
		
		  <!-- 加载状态 -->
		  <view v-if="refreshing" class="loading-container">
		    <view class="loading-spinner"></view>
		    <text class="loading-text">数据加载中...</text>
		  </view>
		
		  <template v-else>
		    <!-- 数据统计卡片 -->
		    <view class="stats-container">
		      <view class="stat-card" @click="navigateToOrders('pending')">
		        <image class="stat-icon total" :src="sheep.$url.static('/static/common/pending.png')" />
		        <view class="stat-number">{{ orderStats.pending }}</view>
		        <view class="stat-label">待处理</view>
		      </view>
		      <view class="stat-card" @click="navigateToOrders('printing')">
		        <image class="stat-icon total" :src="sheep.$url.static('/static/common/printer.png')" />
		        <view class="stat-number">{{ orderStats.printing }}</view>
		        <view class="stat-label">打印中</view>
		      </view>
		      <view class="stat-card" @click="navigateToOrders('completed')">
				  <image class="stat-icon total" :src="sheep.$url.static('/static/common/tick.png')" />
		        <view class="stat-number">{{ orderStats.completed }}</view>
		        <view class="stat-label">已完成</view>
		      </view>
		      <view class="stat-card" @click="navigateToOrders('all')">
				  <image class="stat-icon total" :src="sheep.$url.static('/static/common/chart.png')" />
		        <view class="stat-number">{{ orderStats.total }}</view>
		        <view class="stat-label">总订单</view>
		      </view>
		    </view>
		
		    <!-- 功能菜单 -->
		    <view class="menu-container">
		      <view class="menu-item" @click="navigateToOrders()">
		        <view class="menu-icon orders">
					  <image class="icon_50" :src="sheep.$url.static('/static/common/order_manager.png')" />
		        </view>
		        <view class="menu-text">订单管理</view>
		      </view>
		      <view class="menu-item" @click="navigateToDevices()">
		        <view class="menu-icon devices">
		          <image class="icon_50" :src="sheep.$url.static('/static/common/device_manager.png')" />
		        </view>
		        <view class="menu-text">设备管理</view>
		      </view>
		      <view class="menu-item" @click="navigateToQueue()">
		        <view class="menu-icon queue">
		          <image class="icon_60" :src="sheep.$url.static('/static/common/queue_manager.png')" />
		        </view>
		        <view class="menu-text">打印队列</view>
		      </view>
		      <view class="menu-item" @click="navigateToSettings()">
		        <view class="menu-icon settings">
		          <image class="icon_60" :src="sheep.$url.static('/static/common/setting.png')" />
		        </view>
		        <view class="menu-text">设置</view>
		      </view>
		    </view>
		
		    <!-- 最近订单 -->
		    <view class="recent-orders">
		      <view class="section-header">
		        <text class="section-title">最近订单</text>
		        <text class="more-link" @click="navigateToOrders()">查看全部</text>
		      </view>
		      <view class="order-list">
		        <view v-if="recentOrders.length === 0" class="empty-state">
		          <text class="empty-icon">📋</text>
		          <text class="empty-text">暂无订单</text>
		        </view>
		        <view v-else class="order-item" v-for="order in recentOrders" :key="order.id"
		          @click="viewOrderDetail(order)">
		          <view class="order-header">
		            <text class="order-no">订单号: {{ order.no }}</text>
		            <view class="order-status-badge" :class="getStatusClass(order.status)">
		              {{ getStatusText(order.status) }}
		            </view>
		          </view>
		          <view class="order-content">
		            <view class="customer-info">
		              <text class="customer-name">{{ order.receiverName }}</text>
		              <text class="customer-phone">{{ order.receiverMobile }}</text>
		            </view>
		            <text class="order-time">{{ formatTime(order.createTime) }}</text>
		          </view>
		          <view class="order-footer">
		            <text class="order-amount">¥{{ (order.payPrice / 100).toFixed(2) }}</text>
		            <view class="order-actions">
		              <button v-if="order.printStatus === 'pending'" @click.stop="assignToPrinter(order)"
		                class="btn assign-btn">
		                指派打印
		              </button>
		              <button v-if="order.printStatus === 'printing'" @click.stop="viewProgress(order)"
		                class="btn progress-btn">
		                查看进度
		              </button>
		            </view>
		          </view>
		        </view>
		      </view>
		    </view>
		
		    <!-- 设备状态面板 -->
		    <view class="device-status">
		      <view class="section-header">
		        <text class="section-title">设备状态</text>
		        <text class="more-link" @click="navigateToDevices()">管理设备</text>
		      </view>
		      <view class="device-list">
		        <view v-if="devices.length === 0" class="empty-state">
		          <text class="empty-icon">🖨️</text>
		          <text class="empty-text">暂无设备</text>
		        </view>
		        <view v-else class="device-item" v-for="device in devices" :key="device.id">
		          <view class="device-main">
		            <view class="device-info">
		              <text class="device-name">{{ device.name }}</text>
		              <view class="device-status-indicator" :class="device.status">
		                <text class="status-text">{{ getDeviceStatusText(device.status) }}</text>
		              </view>
		            </view>
		            <view class="device-details">
		              <text class="device-type">{{ device.type }}</text>
		              <text class="queue-count">队列: {{ device.queueCount }}</text>
		            </view>
		          </view>
		          <view class="device-action">
		            <text class="device-action-btn" @click="viewDeviceDetails(device)">详情</text>
		          </view>
		        </view>
		      </view>
		    </view>
		  </template>
		</view>
	</s-layout>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import sheep from '@/sheep';
import StoreApi from '@/sheep/api/store';

// 响应式数据
const storeInfo = reactive({
  id: '',
  name: '店铺名称',
  status: 1
});

const userInfo = reactive({
  name: ''
});

const orderStats = reactive({
  pending: 0,
  printing: 0,
  completed: 0,
  total: 0
});

const recentOrders = ref([]);
const devices = ref([]);
const refreshing = ref(false);
const refreshTimer = ref(null);

// 生命周期
onMounted(async () => {
  // 获取用户信息
  const userInfoStorage = await sheep.$store('user').getInfo();
  userInfo.name = userInfoStorage.nickname;
  
  await initPage();
});

onUnmounted(() => {
  // 清除定时器
  if (refreshTimer.value) {
    clearInterval(refreshTimer.value);
    refreshTimer.value = null;
  }
});

// 方法
const initPage = async () => {
  await getStoreInfo();
  await refreshData();
  setupAutoRefresh();
};

const setupAutoRefresh = () => {
  // 每2分钟自动刷新数据
  refreshTimer.value = setInterval(() => {
    refreshData();
  }, 120000);
};

const refreshData = async () => {
  refreshing.value = true;
  try {
    await Promise.all([
      getOrderStats(),
      getRecentOrders(),
      getDevices()
    ]);
  } catch (error) {
    console.error('刷新数据失败:', error);
    uni.showToast({
      title: '数据刷新失败',
      icon: 'none'
    });
  } finally {
    refreshing.value = false;
  }
};

const getStoreInfo = async () => {
  try {
    const res = await StoreApi.getStoreInfo();
    if (res.code !== 0) {
		sheep.$router.back();
    }
	Object.assign(storeInfo, res.data);
  } catch (error) {
    console.error('获取店铺信息失败:', error);
  }
};

const getOrderStats = async () => {
  try {
    const res = await StoreApi.getOrderStats();
    if (res.code === 0) {
      Object.assign(orderStats, res.data);
    }
  } catch (error) {
    console.error('获取订单统计失败:', error);
  }
};

const getRecentOrders = async () => {
  try {
    const res = await StoreApi.getRecentOrders({
      pageSize: 3
    });
    if (res.code === 0) {
      recentOrders.value = res.data.list || [];
    }
  } catch (error) {
    console.error('获取最近订单失败:', error);
  }
};

const getDevices = async () => {
  try {
    const res = await StoreApi.getDevices();
    if (res.code === 0) {
      devices.value = res.data || [];
    }
  } catch (error) {
    console.error('获取设备信息失败:', error);
  }
};

const navigateToOrders = (status) => {
  uni.navigateTo({
    url: `/pages/store/orders/index?status=${status || 'all'}`
  });
};

const navigateToDevices = () => {
  uni.navigateTo({
    url: '/pages/store/devices/index'
  });
};

const navigateToQueue = () => {
  uni.navigateTo({
    url: '/pages/store/queue/index'
  });
};

const navigateToSettings = () => {
  uni.navigateTo({
    url: '/pages/store/settings/index'
  });
};

const viewOrderDetail = (order) => {
  uni.navigateTo({
    url: `/pages/store/orders/detail?id=${order.id}`
  });
};

const viewDeviceDetails = (device) => {
  uni.navigateTo({
    url: `/pages/store/devices/detail?id=${device.id}`
  });
};

const assignToPrinter = async (order) => {
  const onlineDevices = devices.value.filter(d => d.status === 'online');

  if (onlineDevices.length === 0) {
    uni.showToast({
      title: '暂无在线设备',
      icon: 'none'
    });
    return;
  }

  uni.showActionSheet({
    itemList: onlineDevices.map(d => d.name),
    success: async (res) => {
      const device = onlineDevices[res.tapIndex];
      try {
        const assignRes = await StoreApi.assignOrderToDevice({
          orderId: order.id,
          deviceId: device.id
        });

        if (assignRes.code === 0) {
          uni.showToast({
            title: '指派成功',
            icon: 'success'
          });
          refreshData();
        } else {
          uni.showToast({
            title: assignRes.msg || '指派失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('指派失败:', error);
        uni.showToast({
          title: '指派失败',
          icon: 'none'
        });
      }
    }
  });
};

const viewProgress = (order) => {
  uni.navigateTo({
    url: `/pages/store/orders/progress?id=${order.id}`
  });
};

const getStatusClass = (status) => {
  const statusMap = {
    'pending': 'status-pending',
    'printing': 'status-printing',
    'completed': 'status-completed',
    'cancelled': 'status-cancelled'
  };
  return statusMap[status] || 'status-default';
};

const getStatusText = (status) => {
  const statusMap = {
    'pending': '待处理',
    'printing': '打印中',
    'completed': '已完成',
    'cancelled': '已取消'
  };
  return statusMap[status] || '未知';
};

const getDeviceStatusText = (status) => {
  const statusMap = {
    'online': '在线',
    'offline': '离线',
    'busy': '忙碌'
  };
  return statusMap[status] || '未知';
};

const formatTime = (time) => {
  if (!time) return '';
  const date = new Date(time);
  const now = new Date();
  const diff = now - date;

  if (diff < 60000) {
    return '刚刚';
  } else if (diff < 3600000) {
    return Math.floor(diff / 60000) + '分钟前';
  } else if (diff < 86400000) {
    return Math.floor(diff / 3600000) + '小时前';
  } else {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
  }
};

const getUserInitials = (name) => {
  if (!name) return 'U';
  return name.charAt(0).toUpperCase();
};

const goToHome = () => {
  uni.switchTab({
    url: '/pages/index/user'
  });
};

// 下拉刷新
const onPullDownRefresh = () => {
  refreshData().then(() => {
    uni.stopPullDownRefresh();
  });
};
</script>

<style scoped>
/* 样式保持不变 */
.store-management {
  background-color: #f7f9fc;
  min-height: 100vh;
  padding-bottom: 40rpx;
}

/* 顶部状态栏 */
.status-bar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30rpx 32rpx;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
}

.store-info {
  display: flex;
  flex-direction: column;
}

.store-name {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 12rpx;
}

.status-badge {
  display: flex;
  align-items: center;
  font-size: 24rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  background: rgba(255, 255, 255, 0.2);
  width: fit-content;
}

.status-dot {
  width: 12rpx;
  height: 12rpx;
  border-radius: 50%;
  margin-right: 8rpx;
}

.status-badge.online .status-dot {
  background: #4caf50;
}

.status-badge.offline .status-dot {
  background: #f44336;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-name {
  font-size: 28rpx;
  margin-right: 16rpx;
}

.avatar {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  font-size: 24rpx;
  font-weight: bold;
  color: white;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.loading-spinner {
  width: 60rpx;
  height: 60rpx;
  border: 6rpx solid #f3f3f3;
  border-top: 6rpx solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20rpx;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.loading-text {
  font-size: 28rpx;
  color: #999;
}

/* 数据统计卡片 */
.stats-container {
  display: flex;
  background: white;
  margin: 24rpx;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
  padding: 20rpx 0;
}

.stat-card {
  flex: 1;
  text-align: center;
  padding: 30rpx 0;
  position: relative;
}

.stat-card:not(:last-child)::after {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  height: 60rpx;
  width: 1px;
  background: #f0f0f0;
}

.stat-icon {
  font-size: 44rpx;
  margin-bottom: 16rpx;
  width: 48rpx;
  height: 48rpx;
  text-align: center;
}

.stat-number {
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
}

/* 功能菜单 */
.menu-container {
  display: flex;
  flex-wrap: wrap;
  background: white;
  margin: 24rpx;
  border-radius: 16rpx;
  padding: 20rpx 0;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
}

.menu-item {
  width: 25%;
  text-align: center;
  padding: 30rpx 0;
}

.menu-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16rpx;
  background: #f7f9fc;
}

.menu-icon .icon_50 {
	width: 50rpx;
	height: 50rpx;
}

.menu-icon .icon_60{
	width: 60rpx;
	height: 60rpx;
}

.menu-text {
  font-size: 26rpx;
  color: #333;
}

/* 内容区块 */
.recent-orders,
.device-status {
  background: white;
  margin: 24rpx;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 32rpx;
  border-bottom: 1px solid #f0f0f0;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.more-link {
  font-size: 26rpx;
  color: #667eea;
}

/* 订单列表 */
.order-list {
  padding: 0 32rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 0;
}

.empty-icon {
  font-size: 60rpx;
  margin-bottom: 20rpx;
  opacity: 0.5;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.order-item {
  padding: 32rpx 0;
  border-bottom: 1px solid #f0f0f0;
}

.order-item:last-child {
  border-bottom: none;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.order-no {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
}

.order-status-badge {
  font-size: 22rpx;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  color: white;
}

.status-pending {
  background: #ff9800;
}

.status-printing {
  background: #2196f3;
}

.status-completed {
  background: #4caf50;
}

.status-cancelled {
  background: #f44336;
}

.order-content {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24rpx;
}

.customer-info {
  display: flex;
  flex-direction: column;
}

.customer-name {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.customer-phone {
  font-size: 24rpx;
  color: #999;
}

.order-time {
  font-size: 24rpx;
  color: #999;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-amount {
  font-size: 32rpx;
  font-weight: bold;
  color: #f44336;
}

.order-actions {
  display: flex;
}

.btn {
  font-size: 24rpx;
  padding: 3rpx 24rpx;
  border-radius: 20rpx;
  border: none;
  margin-left: 16rpx;
}

.assign-btn {
  background: #667eea;
  color: white;
}

.progress-btn {
  background: #2196f3;
  color: white;
}

/* 设备列表 */
.device-list {
  padding: 0 32rpx;
}

.device-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx 0;
  border-bottom: 1px solid #f0f0f0;
}

.device-item:last-child {
  border-bottom: none;
}

.device-main {
  flex: 1;
}

.device-info {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.device-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  margin-right: 20rpx;
}

.device-status-indicator {
  display: flex;
  align-items: center;
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.device-status-indicator.online {
  background: rgba(76, 175, 80, 0.1);
  color: #4caf50;
}

.device-status-indicator.offline {
  background: rgba(244, 67, 54, 0.1);
  color: #f44336;
}

.device-status-indicator.busy {
  background: rgba(255, 152, 0, 0.1);
  color: #ff9800;
}

.device-details {
  display: flex;
}

.device-type {
  font-size: 24rpx;
  color: #666;
  margin-right: 20rpx;
}

.queue-count {
  font-size: 24rpx;
  color: #666;
}

.device-action-btn {
  font-size: 24rpx;
  color: #667eea;
  padding: 12rpx 20rpx;
  border: 1px solid #667eea;
  border-radius: 20rpx;
}
</style>