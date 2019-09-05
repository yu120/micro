/*
 * 四川生学教育科技有限公司
 * Copyright (c) 2015-2025 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 */
package cn.micro.biz.commons.excel;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Excel导出工具
 *
 * @author lry
 */
@Slf4j
public class ExcelDownloadUtils {

    public void s(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        connection.ignoreContentType(true);
        Connection.Response response = connection.execute();
        BufferedInputStream inputStream = response.bodyStream();
    }

    /**
     * 多个Excel压缩成一个zip
     *
     * @param byteMap
     * @return
     * @throws Exception
     */
    public static byte[] toZipByte(Map<String, byte[]> byteMap) throws Exception {
        ByteArrayOutputStream bos = null;
        ZipOutputStream zos = null;

        try {
            bos = new ByteArrayOutputStream();
            zos = new ZipOutputStream(bos);
            // 进行压缩存储
            zos.setMethod(ZipOutputStream.DEFLATED);
            // 压缩级别值为0-9共10个级别(值越大，表示压缩越厉害)
            zos.setLevel(Deflater.BEST_COMPRESSION);
            for (Map.Entry<String, byte[]> entry : byteMap.entrySet()) {
                zos.putNextEntry(new ZipEntry(entry.getKey()));
                zos.write(entry.getValue());
                zos.closeEntry();
            }

            zos.close();
            return bos.toByteArray();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}