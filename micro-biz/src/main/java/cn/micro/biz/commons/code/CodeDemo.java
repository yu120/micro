package cn.micro.biz.commons.code;

import java.util.List;

public class CodeDemo {

    public static void main(String[] args) {
        String realPath = CodeDemo.class.getResource("/").getPath();
        realPath = realPath.substring(0, realPath.length() - 15) + "src/main/java/";
        String packageName = "cn.micro.biz";
        List<String> sqlList = CodeFactory.INSTANCE.handler(realPath, packageName);
        for (String sql : sqlList) {
            System.out.println(sql);
        }
    }

}
