package cn.micro.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class OssTokenVO implements Serializable {

    private String token;
    private String accessUrl;

}
