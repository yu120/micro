package cn.micro.biz.web.advertisement;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.service.advertisement.IAvailableScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Available Score Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("available-score")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AvailableScoreController {

    private final IAvailableScoreService availableScoreService;

}
