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

    public static final String MEMBERS_ID = "members_id";
    public static final String MEMBERS_NAME = "members_name";
    public static final String PLATFORM = "platform";
    public static final String IP = "ip";
    public static final String TIME = "time";
    public static final String AUTHORITIES = "authorities";
    public static final String OTHERS = "others";

    private Long membersId;
    private String membersName;
    private Integer deviceType;
    private List<String> authorities = new ArrayList<>();
    private Map<String, Object> others = new HashMap<>();

}
