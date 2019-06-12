package cn.micro.biz.commons.code;

import com.sun.javadoc.RootDoc;

public class MicroDoclet {

    public static RootDoc rootDoc;

    public static boolean start(RootDoc rootDoc) {
        MicroDoclet.rootDoc = rootDoc;
        return true;
    }

}
