package cn.micro.biz.commons.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Xss Http Servlet Request Wrapper
 *
 * @author lry
 */
public class XssHttpServletRequest extends HttpServletRequestWrapper {

    private static final String BAD_SQL_STR = "'|and|exec|execute|insert|select|delete|update|" +
            "count|drop|%|chr|mid|master|truncate|char|declare|sitename|net user|" +
            "xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
            "table|from|grant|use|group_concat|column_name|information_schema.columns|" +
            "table_schema|union|where|select|delete|update|order|by|count|" +
            "chr|mid|master|truncate|char|declare|or|;|-|--|,|like|//|/|%|#";

    public XssHttpServletRequest(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }

        return encodedValues;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }

        return cleanXSS(value);
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }

        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        // You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        /*
         value = value.replaceAll("[*]", "[" + "*]");
         value = value.replaceAll("[+]", "[" + "+]");
         value = value.replaceAll("[?]", "[" + "?]");
         */

        // replace sql 这里可以自由发挥
        String[] values = value.split(" ");
        String[] badStrArray = BAD_SQL_STR.split("\\|");
        for (String badStr : badStrArray) {
            for (int j = 0; j < values.length; j++) {
                if (values[j].equalsIgnoreCase(badStr)) {
                    values[j] = "forbid";
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i == values.length - 1) {
                sb.append(values[i]);
            } else {
                sb.append(values[i]).append(" ");
            }
        }

        value = sb.toString();
        return value;
    }

}
