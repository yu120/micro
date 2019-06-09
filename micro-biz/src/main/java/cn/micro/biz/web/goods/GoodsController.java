package cn.micro.biz.web.goods;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.service.goods.IGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Goods Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("goods")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsController {

    private final IGoodsService goodsService;

}
