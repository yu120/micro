package cn.micro.biz.web.advertisement;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.service.advertisement.IAdvertisementStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Advertisement Store Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("advertisement-store")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvertisementStoreController {

    private final IAdvertisementStoreService advertisementStoreService;

}
