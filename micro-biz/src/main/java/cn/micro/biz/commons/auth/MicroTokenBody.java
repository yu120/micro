package cn.micro.biz.commons.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Micro Token Body
 *
 * @author lry
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MicroTokenBody {

    public static final String TENANT_ID = "tenant_id";
    public static final String MEMBER_ID = "member_id";
    public static final String MEMBER_NAME = "member_name";
    public static final String DEVICE = "device";
    public static final String CLIENT = "client";
    public static final String TIME = "time";
    public static final String AUTHORITIES = "authorities";
    public static final String OTHERS = "others";

    private Long tenantId;
    private Long memberId;
    private String memberName;
    private Integer deviceType;
    private List<String> authorities = new ArrayList<>();
    private Map<String, Object> others = new HashMap<>();

}
