package com.star.lp.module.trade.convert.print;

import com.star.lp.module.trade.controller.app.print.vo.AppPrintSettingOptionRespVO;
import com.star.lp.module.trade.controller.app.print.vo.AppPrintSettingOptionValueRespVO;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionDO;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-03T12:28:04+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
public class SettingOptionConvertImpl implements SettingOptionConvert {

    @Override
    public AppPrintSettingOptionRespVO convert(SettingOptionDO bean, List<SettingOptionValueDO> valueList) {
        if ( bean == null && valueList == null ) {
            return null;
        }

        AppPrintSettingOptionRespVO appPrintSettingOptionRespVO = new AppPrintSettingOptionRespVO();

        if ( bean != null ) {
            appPrintSettingOptionRespVO.setId( bean.getId() );
            appPrintSettingOptionRespVO.setName( bean.getName() );
            appPrintSettingOptionRespVO.setSort( bean.getSort() );
            appPrintSettingOptionRespVO.setRemark( bean.getRemark() );
        }
        appPrintSettingOptionRespVO.setValues( settingOptionValueDOListToAppPrintSettingOptionValueRespVOList( valueList ) );

        return appPrintSettingOptionRespVO;
    }

    @Override
    public AppPrintSettingOptionValueRespVO convertValue(SettingOptionValueDO bean) {
        if ( bean == null ) {
            return null;
        }

        AppPrintSettingOptionValueRespVO appPrintSettingOptionValueRespVO = new AppPrintSettingOptionValueRespVO();

        appPrintSettingOptionValueRespVO.setId( bean.getId() );
        appPrintSettingOptionValueRespVO.setOptionId( bean.getOptionId() );
        appPrintSettingOptionValueRespVO.setValue( bean.getValue() );
        if ( bean.getPrice() != null ) {
            appPrintSettingOptionValueRespVO.setPrice( BigDecimal.valueOf( bean.getPrice() ) );
        }
        appPrintSettingOptionValueRespVO.setSort( bean.getSort() );
        appPrintSettingOptionValueRespVO.setRemark( bean.getRemark() );

        return appPrintSettingOptionValueRespVO;
    }

    protected List<AppPrintSettingOptionValueRespVO> settingOptionValueDOListToAppPrintSettingOptionValueRespVOList(List<SettingOptionValueDO> list) {
        if ( list == null ) {
            return null;
        }

        List<AppPrintSettingOptionValueRespVO> list1 = new ArrayList<AppPrintSettingOptionValueRespVO>( list.size() );
        for ( SettingOptionValueDO settingOptionValueDO : list ) {
            list1.add( convertValue( settingOptionValueDO ) );
        }

        return list1;
    }
}
