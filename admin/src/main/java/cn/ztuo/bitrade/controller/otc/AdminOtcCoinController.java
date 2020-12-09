package cn.ztuo.bitrade.controller.otc;

import cn.ztuo.bitrade.annotation.AccessLog;
import cn.ztuo.bitrade.constant.AdminModule;
import cn.ztuo.bitrade.constant.PageModel;
import cn.ztuo.bitrade.controller.common.BaseAdminController;
import cn.ztuo.bitrade.entity.Coin;
import cn.ztuo.bitrade.entity.OtcCoin;
import cn.ztuo.bitrade.service.CoinService;
import cn.ztuo.bitrade.service.LocaleMessageSourceService;
import cn.ztuo.bitrade.service.OtcCoinService;
import cn.ztuo.bitrade.util.BindingResultUtil;
import cn.ztuo.bitrade.util.FileUtil;
import cn.ztuo.bitrade.util.MessageResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.util.Assert.isNull;
import static org.springframework.util.Assert.notNull;

/**
 * @author MrGao
 * @description otc币种
 * @date 2018/1/11 13:35
 */
@RestController
@RequestMapping("/otc/otc-coin")
public class AdminOtcCoinController extends BaseAdminController {

    @Autowired
    private OtcCoinService otcCoinService;
    @Autowired
    private LocaleMessageSourceService messageSource;

    @Autowired
    private CoinService coinService ;

    @RequiresPermissions("otc:otc-coin:create")
    @PostMapping("create")
    @AccessLog(module = AdminModule.OTC, operation = "创建otc币种otcCoin")
    public MessageResult create(@Valid OtcCoin otcCoin, BindingResult bindingResult) {
        isNull(otcCoin.getId(), "validate otcCoin.id!");
        OtcCoin oldCoin=otcCoinService.findByUnit(otcCoin.getUnit());
        if(oldCoin!=null){
            return MessageResult.error(messageSource.getMessage("PRE_COIN_EXIST"));
        }
        MessageResult result = BindingResultUtil.validate(bindingResult);
        if (result != null) {
            return result;
        }
        Coin coin = coinService.findByUnit(otcCoin.getUnit());
        if(coin==null) {
            return error(messageSource.getMessage("COIN_NOT_SUPPORTED"));
        }
        otcCoinService.save(otcCoin);
        return success();
    }

    @RequiresPermissions("otc:otc-coin:all")
    @PostMapping("all")
    @AccessLog(module = AdminModule.OTC, operation = "所有otc币种otcCoin")
    public MessageResult all() {
        List<OtcCoin> all = otcCoinService.findAll();
        if (all != null && all.size() > 0) {
            return success(all);
        }
        return error(messageSource.getMessage("NO_DATA"));
    }

    @RequiresPermissions("otc:otc-coin:detail")
    @PostMapping("detail")
    @AccessLog(module = AdminModule.OTC, operation = "otc币种otcCoin详情")
    public MessageResult detail(@RequestParam("id") Long id) {
        OtcCoin one = (OtcCoin)otcCoinService.findById(id);
        notNull(one, "validate otcCoin.id!");
        return success(one);
    }

    @RequiresPermissions("otc:otc-coin:update")
    @PostMapping("update")
    @AccessLog(module = AdminModule.OTC, operation = "更新otc币种otcCoin")
    public MessageResult update(@Valid OtcCoin otcCoin, BindingResult bindingResult) {
        notNull(otcCoin.getId(), "validate otcCoin.id!");
        MessageResult result = BindingResultUtil.validate(bindingResult);
        if (result != null){
            return result;
        }

        OtcCoin one = otcCoinService.findOne(otcCoin.getId());
        notNull(one, "validate otcCoin.id!");
        otcCoinService.save(otcCoin);
        return success();
    }

    @RequiresPermissions("otc:otc-coin:deletes")
    @PostMapping("deletes")
    @AccessLog(module = AdminModule.OTC, operation = "otc币种otcCoin 删除")
    public MessageResult deletes(
            @RequestParam(value = "ids") Long[] ids) {
        otcCoinService.deletes(ids);
        return success(messageSource.getMessage("SUCCESS"));
    }

    @RequiresPermissions("otc:otc-coin:alter-jy-rate")
    @PostMapping("alter-jy-rate")
    @AccessLog(module = AdminModule.OTC, operation = "修改otc币种otcCoin交易率")
    public MessageResult memberStatistics(
            @RequestParam("id") Long id,
            @RequestParam("jyRate") BigDecimal jyRate) {
        OtcCoin one = (OtcCoin) otcCoinService.findById(id);
        notNull(one, "validate otcCoin.id");
        one.setJyRate(jyRate);
        otcCoinService.save(one);
        return success();
    }

    @RequiresPermissions("otc:otc-coin:page-query")
    @PostMapping("page-query")
    @AccessLog(module = AdminModule.OTC, operation = "分页查找otc币种otcCoin")
    public MessageResult pageQuery(PageModel pageModel) {
        Page<OtcCoin> pageResult = otcCoinService.findAll(null, pageModel.getPageable());
        return success(pageResult);
    }

    @RequiresPermissions("otc:otc-coin:out-excel")
    @GetMapping("out-excel")
    @AccessLog(module = AdminModule.OTC, operation = "导出otc币种otcCoin Excel")
    public MessageResult outExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List all = otcCoinService.findAll();
        return new FileUtil().exportExcel(request, response, all, "otcCoin");
    }

    @PostMapping("all-otc-coin-units")
    public MessageResult getAllOtcCoinUnits(){
        List<String> list = otcCoinService.findAllUnits() ;
        return success(messageSource.getMessage("SUCCESS"),list);
    }
}
