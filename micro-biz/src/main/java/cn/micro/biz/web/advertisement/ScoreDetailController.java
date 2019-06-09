package cn.micro.biz.web.advertisement;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.service.advertisement.IAdvertisementService;
import cn.micro.biz.service.advertisement.IScoreDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Score Detail Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("score-detail")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScoreDetailController {

    private final IScoreDetailService scoreDetailService;

}
