<template>
	<s-layout :title="isEdit ? '编辑设备' : '添加设备'" :back="true">
		<view class="device-edit">
			<form @submit="handleSubmit">
				<!-- 基本信息 -->
				<view class="form-section">
					<view class="section-header">
						<text class="section-title">基本信息</text>
					</view>

					<view class="form-item">
						<text class="form-label required">设备名称</text>
						<input class="form-input" v-model="deviceForm.name" placeholder="请输入设备名称" maxlength="50"
							@blur="validateField('name')" />
						<text class="error-text" v-if="errors.name">{{ errors.name }}</text>
					</view>

					<view class="form-item">
						<text class="form-label required">设备类型</text>
						<picker @change="onDeviceTypeChange" :value="deviceTypeIndex" :range="deviceTypes"
							class="picker-container">
							<view class="picker-view" :class="{ placeholder: !deviceForm.type }">
								{{ deviceForm.type || '请选择设备类型' }}
								<text class="picker-arrow">›</text>
							</view>
						</picker>
						<text class="error-text" v-if="errors.type">{{ errors.type }}</text>
					</view>

					<view class="form-item">
						<text class="form-label">设备型号</text>
						<input class="form-input" v-model="deviceForm.model" placeholder="请输入设备型号" maxlength="50" />
					</view>

					<view class="form-item">
						<text class="form-label">设备位置</text>
						<input class="form-input" v-model="deviceForm.location" placeholder="例：前台、办公室A区"
							maxlength="100" />
					</view>
				</view>

				<!-- 连接设置 -->
				<view class="form-section">
					<view class="section-header">
						<text class="section-title">连接设置</text>
					</view>

					<view class="form-item">
						<text class="form-label required">连接类型</text>
						<picker @change="onConnectionTypeChange" :value="connectionTypeIndex" :range="connectionTypes"
							range-key="label" class="picker-container">
							<view class="picker-view" :class="{ placeholder: !deviceForm.connectionType }">
								{{ getConnectionTypeText(deviceForm.connectionType) || '请选择连接类型' }}
								<text class="picker-arrow">›</text>
							</view>
						</picker>
						<text class="error-text" v-if="errors.connectionType">{{ errors.connectionType }}</text>
					</view>

					<view class="form-item">
						<text class="form-label required">连接地址</text>
						<input class="form-input" v-model="deviceForm.address" :placeholder="getAddressPlaceholder()"
							@blur="validateField('address')" />
						<view class="form-hint">
							<text class="hint-text">{{ getAddressHint() }}</text>
						</view>
						<text class="error-text" v-if="errors.address">{{ errors.address }}</text>
					</view>

					<view class="form-item" v-if="showPortField">
						<text class="form-label">端口</text>
						<input class="form-input" v-model.number="deviceForm.port" type="number" placeholder="默认9100"
							@blur="validateField('port')" />
						<text class="error-text" v-if="errors.port">{{ errors.port }}</text>
					</view>
				</view>

				<!-- 备注 -->
				<view class="form-section">
					<view class="section-header">
						<text class="section-title">备注信息</text>
					</view>

					<view class="form-item">
						<text class="form-label">设备备注</text>
						<textarea class="form-textarea" v-model="deviceForm.remark" placeholder="请输入设备备注信息"
							maxlength="200" show-count />
					</view>
				</view>

				<!-- 连接测试 -->
				<view class="form-section" v-if="isEdit || (deviceForm.address && deviceForm.connectionType)">
					<view class="section-header">
						<text class="section-title">连接测试</text>
					</view>

					<view class="test-container">
						<button class="test-btn" :class="{ testing: isTesting }" @click="testConnection"
							:disabled="isTesting || !canTest">
							<text class="test-icon" v-if="!isTesting">🔗</text>
							<text class="test-icon loading" v-else>⟳</text>
							<text>{{ isTesting ? '测试中...' : '测试连接' }}</text>
						</button>

						<view class="test-result" v-if="testResult">
							<view class="result-item" :class="testResult.success ? 'success' : 'error'">
								<text class="result-icon">{{ testResult.success ? '✓' : '×' }}</text>
								<text class="result-text">{{ testResult.message }}</text>
							</view>
							<view class="result-details" v-if="testResult.details">
								<text class="details-text">{{ testResult.details }}</text>
							</view>
						</view>
					</view>
				</view>

				<!-- 操作按钮 -->
				<view class="action-buttons">
					<button class="save-btn" :class="{ disabled: !isFormValid || isSaving }" @click="handleSave"
						:disabled="!isFormValid || isSaving">
						<text v-if="!isSaving">{{ isEdit ? '保存修改' : '添加设备' }}</text>
						<text v-else>保存中...</text>
					</button>

					<button class="cancel-btn" @click="handleCancel">取消</button>
				</view>
			</form>
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

	// 响应式数据
	const isEdit = ref(false);
	const deviceId = ref(null);
	const isSaving = ref(false);
	const isTesting = ref(false);
	const testResult = ref(null);

	const deviceForm = reactive({
		name: '',
		type: '',
		model: '',
		location: '',
		connectionType: '',
		address: '',
		port: 9100,
		defaultPaperSize: '',
		defaultQuality: '',
		supportDuplex: false,
		supportColor: false,
		maxQueueSize: 50,
		printTimeout: 300,
		autoReconnect: true,
		enableMonitoring: true,
		remark: ''
	});

	const errors = reactive({});

	// 选择器数据
	const deviceTypes = ref(['激光打印机', '喷墨打印机']);
	const connectionTypes = ref([{
			label: 'TCP网络连接',
			value: 'TCP'
		},
		{
			label: 'USB连接',
			value: 'USB'
		},
		{
			label: '蓝牙连接',
			value: 'Bluetooth'
		}
	]);

	// 选择器索引
	const deviceTypeIndex = ref(0);
	const connectionTypeIndex = ref(0);

	// 计算属性
	const showPortField = computed(() => {
		return deviceForm.connectionType === 'TCP';
	});

	const canTest = computed(() => {
		return deviceForm.address && deviceForm.connectionType &&
			(!showPortField.value || deviceForm.port);
	});

	const isFormValid = computed(() => {
		const requiredFields = ['name', 'type', 'connectionType', 'address'];
		return requiredFields.every(field => deviceForm[field]) &&
			Object.keys(errors).length === 0;
	});

	const props = defineProps({
		id: {
			type: Number,
			required: true
		}
	});

	// 生命周期
	onMounted(() => {
		const id = props.id;
		console.log("获取设别ID:", id)
		if (id) {
			deviceId.value = id;
			isEdit.value = true;
			loadDeviceData(id);
		}
	});

	// 方法
	const loadDeviceData = async (id) => {
		try {
			const response = await StoreApi.getDeviceDetail(id);
			if (response.code === 0) {
				Object.assign(deviceForm, response.data);

				// 设置选择器索引
				deviceTypeIndex.value = deviceTypes.value.indexOf(deviceForm.type);
				connectionTypeIndex.value = connectionTypes.value.findIndex(item => item.value === deviceForm
					.connectionType);
			}
		} catch (error) {
			console.error('加载设备数据失败:', error);
			uni.showToast({
				title: '加载数据失败',
				icon: 'none'
			});
		}
	};

	// 选择器事件
	const onDeviceTypeChange = (e) => {
		deviceTypeIndex.value = e.detail.value;
		deviceForm.type = deviceTypes.value[e.detail.value];
		validateField('type');
	};

	const onConnectionTypeChange = (e) => {
		connectionTypeIndex.value = e.detail.value;
		deviceForm.connectionType = connectionTypes.value[e.detail.value].value;

		// 重置地址和端口
		deviceForm.address = '';
		if (deviceForm.connectionType === 'TCP') {
			deviceForm.port = 9100;
		}

		validateField('connectionType');
		clearFieldError('address');
	};

	// 表单验证
	const validateField = (fieldName) => {
		clearFieldError(fieldName);

		switch (fieldName) {
			case 'name':
				if (!deviceForm.name.trim()) {
					setFieldError('name', '请输入设备名称');
				} else if (deviceForm.name.trim().length < 2) {
					setFieldError('name', '设备名称至少2个字符');
				}
				break;

			case 'type':
				if (!deviceForm.type) {
					setFieldError('type', '请选择设备类型');
				}
				break;

			case 'connectionType':
				if (!deviceForm.connectionType) {
					setFieldError('connectionType', '请选择连接类型');
				}
				break;

			case 'address':
				if (!deviceForm.address.trim()) {
					setFieldError('address', '请输入连接地址');
				} else if (!isValidAddress(deviceForm.address, deviceForm.connectionType)) {
					setFieldError('address', '连接地址格式不正确');
				}
				break;

			case 'port':
				if (showPortField.value && (!deviceForm.port || deviceForm.port < 1 || deviceForm.port > 65535)) {
					setFieldError('port', '请输入有效的端口号（1-65535）');
				}
				break;
		}
	};

	const validateAllFields = () => {
		const fieldsToValidate = ['name', 'type', 'connectionType', 'address'];
		if (showPortField.value) {
			fieldsToValidate.push('port');
		}

		fieldsToValidate.forEach(field => validateField(field));
		return Object.keys(errors).length === 0;
	};

	const setFieldError = (field, message) => {
		errors[field] = message;
	};

	const clearFieldError = (field) => {
		if (errors[field]) {
			delete errors[field];
		}
	};

	// 地址验证
	const isValidAddress = (address, connectionType) => {
		if (!address || !connectionType) return false;

		switch (connectionType) {
			case 'TCP':
				// IP地址验证
				const ipRegex =
					/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
				const domainRegex =
					/^[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\.[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
				return ipRegex.test(address) || domainRegex.test(address);

			case 'USB':
				// USB设备路径验证
				return address.startsWith('/dev/') || address.startsWith('COM') || address.startsWith('LPT');

			case 'Bluetooth':
				// 蓝牙MAC地址验证
				const bluetoothRegex = /^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$/;
				return bluetoothRegex.test(address);

			default:
				return true;
		}
	};

	// 连接类型
	const getConnectionTypeText = (value) => {
		console.log("连接类型", value)
		const item = connectionTypes.value.find(item => item.value === value);
		return item ? item.label : value;
	};

	const getAddressPlaceholder = () => {
		switch (deviceForm.connectionType) {
			case 'TCP':
				return '192.168.1.100 或 printer.company.com';
			case 'USB':
				return '/dev/usb/lp0 或 COM1';
			case 'Bluetooth':
				return '00:11:22:33:44:55';
			default:
				return '请输入连接地址';
		}
	};

	const getAddressHint = () => {
		switch (deviceForm.connectionType) {
			case 'TCP':
				return '输入打印机的IP地址或域名';
			case 'USB':
				return '输入USB设备路径';
			case 'Bluetooth':
				return '输入蓝牙MAC地址';
			default:
				return '';
		}
	};

	// 连接测试
	const testConnection = async () => {
		if (!canTest.value || isTesting.value) return;

		isTesting.value = true;
		testResult.value = null;

		try {
			const response = await sheep.api.post('/api/store/devices/test-connection', {
				connectionType: deviceForm.connectionType,
				address: deviceForm.address,
				port: deviceForm.port
			});

			if (response.code === 0) {
				testResult.value = {
					success: true,
					message: '连接测试成功',
					details: response.data.details || '设备响应正常'
				};
			} else {
				testResult.value = {
					success: false,
					message: '连接测试失败',
					details: response.message || '无法连接到设备'
				};
			}
		} catch (error) {
			console.error('连接测试失败:', error);
			testResult.value = {
				success: false,
				message: '连接测试失败',
				details: error.message || '网络错误'
			};
		} finally {
			isTesting.value = false;
		}
	};

	// 表单提交
	const handleSave = async () => {
		if (!validateAllFields() || isSaving.value) {
			return;
		}

		isSaving.value = true;

		try {
			const response = await StoreApi.addPrint(deviceForm);

			if (response.code === 0) {
				uni.showToast({
					title: isEdit.value ? '修改成功' : '添加成功',
					icon: 'success'
				});

				// 延迟返回
				setTimeout(() => {
					uni.navigateBack();
				}, 1500);
			} else {
				throw new Error(response.message || '操作失败');
			}
		} catch (error) {
			console.error('保存失败:', error);
			uni.showToast({
				title: error.message || '保存失败',
				icon: 'none'
			});
		} finally {
			isSaving.value = false;
		}
	};

	const handleCancel = () => {
		uni.showModal({
			title: '确认取消',
			content: '当前修改尚未保存，确定要取消吗？',
			success: (res) => {
				if (res.confirm) {
					uni.navigateBack();
				}
			}
		});
	};
</script>

<style scoped>
	.device-edit {
		background-color: #f8f9fa;
		min-height: 100vh;
		padding-bottom: 40rpx;
	}

	.form-section {
		background: white;
		margin: 20rpx;
		border-radius: 16rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
		overflow: hidden;
	}

	.section-header {
		padding: 30rpx;
		border-bottom: 1px solid #f0f0f0;
		background: #fafafa;
	}

	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.form-item {
		padding: 30rpx;
		border-bottom: 1px solid #f0f0f0;
		position: relative;
	}

	.form-item:last-child {
		border-bottom: none;
	}

	.form-label {
		font-size: 28rpx;
		color: #333;
		margin-bottom: 16rpx;
		display: block;
	}

	.form-label.required::after {
		content: '*';
		color: #f44336;
		margin-left: 4rpx;
	}

	.form-input {
		width: 100%;
		height: 80rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 12rpx;
		padding: 0 24rpx;
		font-size: 28rpx;
		box-sizing: border-box;
		transition: border-color 0.3s;
	}

	.form-input:focus {
		border-color: #667eea;
		outline: none;
	}

	.form-textarea {
		width: 100%;
		min-height: 160rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 12rpx;
		padding: 24rpx;
		font-size: 28rpx;
		box-sizing: border-box;
		resize: vertical;
	}

	.form-textarea:focus {
		border-color: #667eea;
		outline: none;
	}

	.picker-container {
		width: 100%;
	}

	.picker-view {
		height: 80rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 12rpx;
		padding: 0 24rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 28rpx;
		color: #333;
		background: white;
	}

	.picker-view.placeholder {
		color: #999;
	}

	.picker-arrow {
		font-size: 32rpx;
		color: #999;
		transform: rotate(90deg);
		margin-left: 16rpx;
	}

	.form-hint {
		margin-top: 12rpx;
	}

	.hint-text {
		font-size: 24rpx;
		color: #999;
	}

	.error-text {
		color: #f44336;
		font-size: 24rpx;
		margin-top: 8rpx;
		display: block;
	}

	.switch-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 40rpx 30rpx;
	}

	.switch-item .form-label {
		margin-bottom: 0;
		flex: 1;
	}

	.test-container {
		padding: 30rpx;
	}

	.test-btn {
		width: 100%;
		height: 80rpx;
		border: 2rpx solid #667eea;
		background: white;
		color: #667eea;
		border-radius: 40rpx;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 20rpx;
		transition: all 0.3s;
	}

	.test-btn:not([disabled]):active {
		background: #667eea;
		color: white;
	}

	.test-btn[disabled] {
		opacity: 0.5;
	}

	.test-btn.testing {
		border-color: #ff9800;
		color: #ff9800;
	}

	.test-icon {
		margin-right: 12rpx;
		font-size: 32rpx;
	}

	.test-icon.loading {
		animation: spin 1s linear infinite;
	}

	@keyframes spin {
		from {
			transform: rotate(0deg);
		}

		to {
			transform: rotate(360deg);
		}
	}

	.test-result {
		background: #f8f9fa;
		border-radius: 12rpx;
		padding: 24rpx;
	}

	.result-item {
		display: flex;
		align-items: center;
		margin-bottom: 12rpx;
	}

	.result-item:last-child {
		margin-bottom: 0;
	}

	.result-item.success {
		color: #4caf50;
	}

	.result-item.error {
		color: #f44336;
	}

	.result-icon {
		margin-right: 12rpx;
		font-size: 32rpx;
		font-weight: bold;
	}

	.result-text {
		font-size: 28rpx;
		font-weight: bold;
	}

	.result-details {
		margin-top: 8rpx;
		padding-left: 44rpx;
	}

	.details-text {
		font-size: 24rpx;
		color: #666;
	}

	.action-buttons {
		margin: 40rpx 20rpx 20rpx;
		display: flex;
		gap: 20rpx;
	}

	.save-btn {
		flex: 2;
		height: 88rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: white;
		border: none;
		border-radius: 44rpx;
		font-size: 32rpx;
		font-weight: bold;
	}

	.save-btn.disabled {
		background: #ccc;
		opacity: 0.6;
	}

	.cancel-btn {
		flex: 1;
		height: 88rpx;
		background: white;
		color: #666;
		border: 2rpx solid #ddd;
		border-radius: 44rpx;
		font-size: 28rpx;
	}
</style>