package cn.micro.biz.service.spider.support;

import cn.micro.biz.entity.spider.SpiderGoodsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Spider Application
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum SpiderApp {

    // ====

    TAO_BAO(1, "TB") {
        @Override
        public String apply(Object... args) throws Exception {
            String q = URLEncoder.encode(String.valueOf(args[0]), StandardCharsets.UTF_8.name());
            String filter = URLEncoder.encode("reserve_price[" + (args[1] == null ? "" : args[1]) + "," + (args[2] == null ? "" : args[2]) + "]", StandardCharsets.UTF_8.name());
            String s = String.valueOf(44 * ((int) args[3] - 1));
            return "https://s.taobao.com/search?q=" + q + "&sort=sale-desc&filter=" + filter + "&bcoffset=0&p4ppushleft=,44&s=" + s + "&auction_tag%5B%5D=4806";
        }
    };

    public static Map<SpiderApp, Map<String, SpiderAttr>> APP_FIELD_MAPPING = new HashMap<>();

    static {
        try {
            Field[] fields = SpiderGoodsEntity.class.getDeclaredFields();
            for (Field field : fields) {
                SpiderAttrs attrs = field.getDeclaredAnnotation(SpiderAttrs.class);
                if (attrs != null) {
                    for (SpiderAttr attr : attrs.value()) {
                        Map<String, SpiderAttr> fieldMapping = APP_FIELD_MAPPING.computeIfAbsent(attr.app(), k -> new HashMap<>());
                        fieldMapping.put(field.getName(), attr);
                    }
                }
                SpiderAttr attr = field.getDeclaredAnnotation(SpiderAttr.class);
                if (attr != null) {
                    Map<String, SpiderAttr> fieldMapping = APP_FIELD_MAPPING.computeIfAbsent(attr.app(), k -> new HashMap<>());
                    fieldMapping.put(field.getName(), attr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int category;
    private String code;

    public abstract String apply(Object... args) throws Exception;

    public static SpiderApp parse(int category) {
        for (SpiderApp spiderApp : values()) {
            if (spiderApp.getCategory() == category) {
                return spiderApp;
            }
        }

        throw new IllegalArgumentException("category=" + category);
    }

}
