package cn.micro.biz.web.advertisement;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.service.advertisement.IAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Advertisement Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("advertisement")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvertisementController {

    private final IAdvertisementService advertisementService;

}
