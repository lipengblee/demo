package com.star.lp.module.trade.convert.store;

import com.star.lp.module.trade.controller.app.store.vo.AddDeviceReqVO;
import com.star.lp.module.trade.controller.app.store.vo.PrintQueueItemRespVO;
import com.star.lp.module.trade.controller.app.store.vo.StoreDeviceRespVO;
import com.star.lp.module.trade.controller.app.store.vo.StoreInfoRespVO;
import com.star.lp.module.trade.controller.app.store.vo.StoreOrderDetailRespVO;
import com.star.lp.module.trade.controller.app.store.vo.StoreOrderRespVO;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderItemDO;
import com.star.lp.module.trade.dal.dataobject.print.PrintDeviceDO;
import com.star.lp.module.trade.dal.dataobject.print.PrintQueueDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-03T13:44:32+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class StoreManagementConvertImpl implements StoreManagementConvert {

    @Override
    public StoreInfoRespVO convert(DeliveryPickUpStoreDO store) {
        if ( store == null ) {
            return null;
        }

        StoreInfoRespVO storeInfoRespVO = new StoreInfoRespVO();

        storeInfoRespVO.setId( store.getId() );
        storeInfoRespVO.setName( store.getName() );
        storeInfoRespVO.setStatus( store.getStatus() );
        storeInfoRespVO.setPhone( store.getPhone() );

        return storeInfoRespVO;
    }

    @Override
    public ArrayList<StoreDeviceRespVO> convertDevices(List<PrintDeviceDO> devices) {
        if ( devices == null ) {
            return null;
        }

        ArrayList<StoreDeviceRespVO> arrayList = new ArrayList<StoreDeviceRespVO>();
        for ( PrintDeviceDO printDeviceDO : devices ) {
            arrayList.add( printDeviceDOToStoreDeviceRespVO( printDeviceDO ) );
        }

        return arrayList;
    }

    @Override
    public ArrayList<StoreOrderRespVO> convertStoreOrders(List<TradeOrderDO> orders, List<TradeOrderItemDO> orderItems) {
        if ( orders == null && orderItems == null ) {
            return null;
        }

        ArrayList<StoreOrderRespVO> arrayList = new ArrayList<StoreOrderRespVO>();

        return arrayList;
    }

    @Override
    public StoreOrderDetailRespVO convertOrderDetail(TradeOrderDO order, List<TradeOrderItemDO> orderItems, PrintDeviceDO assignedDevice, Integer printProgress, List<StoreOrderDetailRespVO.OrderLogVO> logs) {
        if ( order == null && orderItems == null && assignedDevice == null && printProgress == null && logs == null ) {
            return null;
        }

        StoreOrderDetailRespVO storeOrderDetailRespVO = new StoreOrderDetailRespVO();

        if ( order != null ) {
            storeOrderDetailRespVO.setId( order.getId() );
            if ( order.getStatus() != null ) {
                storeOrderDetailRespVO.setStatus( String.valueOf( order.getStatus() ) );
            }
            storeOrderDetailRespVO.setCreateTime( order.getCreateTime() );
            storeOrderDetailRespVO.setRemark( order.getRemark() );
            storeOrderDetailRespVO.setNo( order.getNo() );
            storeOrderDetailRespVO.setPrintStatus( order.getPrintStatus() );
            storeOrderDetailRespVO.setReceiverName( order.getReceiverName() );
            storeOrderDetailRespVO.setReceiverMobile( order.getReceiverMobile() );
            storeOrderDetailRespVO.setPayPrice( order.getPayPrice() );
            storeOrderDetailRespVO.setUserRemark( order.getUserRemark() );
            storeOrderDetailRespVO.setReceiverDetailAddress( order.getReceiverDetailAddress() );
        }
        storeOrderDetailRespVO.setAssignedDevice( printDeviceDOToStoreDeviceRespVO( assignedDevice ) );
        storeOrderDetailRespVO.setPrintProgress( printProgress );
        List<StoreOrderDetailRespVO.OrderLogVO> list = logs;
        if ( list != null ) {
            storeOrderDetailRespVO.setLogs( new ArrayList<StoreOrderDetailRespVO.OrderLogVO>( list ) );
        }

        return storeOrderDetailRespVO;
    }

    @Override
    public PrintDeviceDO convert(AddDeviceReqVO reqVO) {
        if ( reqVO == null ) {
            return null;
        }

        PrintDeviceDO.PrintDeviceDOBuilder printDeviceDO = PrintDeviceDO.builder();

        printDeviceDO.name( reqVO.getName() );
        printDeviceDO.type( reqVO.getType() );
        printDeviceDO.model( reqVO.getModel() );
        printDeviceDO.location( reqVO.getLocation() );
        printDeviceDO.address( reqVO.getAddress() );
        printDeviceDO.connectionType( reqVO.getConnectionType() );
        printDeviceDO.port( reqVO.getPort() );
        printDeviceDO.remark( reqVO.getRemark() );

        return printDeviceDO.build();
    }

    @Override
    public List<PrintQueueItemRespVO> convertPrintQueue(List<PrintQueueDO> queueItems) {
        if ( queueItems == null ) {
            return null;
        }

        List<PrintQueueItemRespVO> list = new ArrayList<PrintQueueItemRespVO>( queueItems.size() );
        for ( PrintQueueDO printQueueDO : queueItems ) {
            list.add( printQueueDOToPrintQueueItemRespVO( printQueueDO ) );
        }

        return list;
    }

    protected StoreDeviceRespVO printDeviceDOToStoreDeviceRespVO(PrintDeviceDO printDeviceDO) {
        if ( printDeviceDO == null ) {
            return null;
        }

        StoreDeviceRespVO storeDeviceRespVO = new StoreDeviceRespVO();

        storeDeviceRespVO.setId( printDeviceDO.getId() );
        storeDeviceRespVO.setName( printDeviceDO.getName() );
        storeDeviceRespVO.setType( printDeviceDO.getType() );
        storeDeviceRespVO.setModel( printDeviceDO.getModel() );
        storeDeviceRespVO.setStatus( printDeviceDO.getStatus() );
        storeDeviceRespVO.setLocation( printDeviceDO.getLocation() );
        storeDeviceRespVO.setQueueCount( printDeviceDO.getQueueCount() );
        storeDeviceRespVO.setTodayCount( printDeviceDO.getTodayCount() );
        storeDeviceRespVO.setSuccessRate( printDeviceDO.getSuccessRate() );
        storeDeviceRespVO.setPaperStatus( printDeviceDO.getPaperStatus() );
        storeDeviceRespVO.setInkLevel( printDeviceDO.getInkLevel() );
        storeDeviceRespVO.setLastConnectTime( printDeviceDO.getLastConnectTime() );

        return storeDeviceRespVO;
    }

    protected PrintQueueItemRespVO printQueueDOToPrintQueueItemRespVO(PrintQueueDO printQueueDO) {
        if ( printQueueDO == null ) {
            return null;
        }

        PrintQueueItemRespVO printQueueItemRespVO = new PrintQueueItemRespVO();

        printQueueItemRespVO.setId( printQueueDO.getId() );
        printQueueItemRespVO.setOrderId( printQueueDO.getOrderId() );
        printQueueItemRespVO.setOrderNo( printQueueDO.getOrderNo() );
        printQueueItemRespVO.setQueueIndex( printQueueDO.getQueueIndex() );
        printQueueItemRespVO.setStatus( printQueueDO.getStatus() );
        printQueueItemRespVO.setDocumentTitle( printQueueDO.getDocumentTitle() );
        printQueueItemRespVO.setPages( printQueueDO.getPages() );
        printQueueItemRespVO.setCopies( printQueueDO.getCopies() );
        printQueueItemRespVO.setCustomerName( printQueueDO.getCustomerName() );
        printQueueItemRespVO.setProgress( printQueueDO.getProgress() );
        printQueueItemRespVO.setErrorMessage( printQueueDO.getErrorMessage() );
        printQueueItemRespVO.setCreateTime( printQueueDO.getCreateTime() );
        printQueueItemRespVO.setStartTime( printQueueDO.getStartTime() );
        printQueueItemRespVO.setCompleteTime( printQueueDO.getCompleteTime() );

        return printQueueItemRespVO;
    }
}
