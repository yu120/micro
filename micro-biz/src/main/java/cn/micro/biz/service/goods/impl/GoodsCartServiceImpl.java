package cn.micro.biz.service.goods.impl;

import cn.micro.biz.entity.goods.GoodsCartEntity;
import cn.micro.biz.mapper.goods.IGoodsCartMapper;
import cn.micro.biz.service.goods.IGoodsCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Goods Cart Service Implements
 *
 * @author lry
 */
@Service
public class GoodsCartServiceImpl extends ServiceImpl<IGoodsCartMapper, GoodsCartEntity> implements IGoodsCartService {

}
