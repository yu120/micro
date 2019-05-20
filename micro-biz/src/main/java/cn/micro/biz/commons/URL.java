package cn.micro.biz.commons;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * URL - Uniform Resource Locator (Immutable, ThreadSafe)
 *
 * @author lry
 */
public final class URL implements Serializable {

    public final static String VERSION_KEY = "version";
    public final static String VERSION_DEFAULT = "1.0.0";
    public final static String GROUP_KEY = "group";
    public final static String APPLICATION_KEY = "application";
    public final static String MODULE_KEY = "module";
    public final static String BACKUP_KEY = "backup";

    private String protocol;
    private String host;
    private int port;
    private String path;

    private Map<String, String> parameters;
    private volatile transient Map<String, Number> numbers;

    public URL(String protocol, String host, int port, String path) {
        this(protocol, host, port, path, new HashMap<>());
    }

    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.parameters = parameters;
    }

    public String getBackup() {
        return parameters.get(BACKUP_KEY);
    }

    public static URL valueOf(String url) {
        if (url == null || url.length() == 0) {
            throw new NullPointerException("url is null");
        }

        String protocol = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = new HashMap<>();
        int i = url.indexOf("?");
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("\\&");
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        parameters.put(part, part);
                    }
                }
            }

            url = url.substring(0, i);
        }

        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) {
                throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            }
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        } else {
            i = url.indexOf(":/");
            if (i >= 0) {
                if (i == 0) {
                    throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                }
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }

        i = url.indexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }

        if (url.length() > 0) {
            host = url;
        }

        return new URL(protocol, host, port, path, parameters);
    }

    private static String buildHostPortStr(String host, int defaultPort) {
        if (defaultPort <= 0) {
            return host;
        }

        int idx = host.indexOf(":");
        if (idx < 0) {
            return host + ":" + defaultPort;
        }

        int port = Integer.parseInt(host.substring(idx + 1));
        if (port <= 0) {
            return host.substring(0, idx + 1) + defaultPort;
        }

        return host;
    }

    public URL copy() {
        Map<String, String> params = new HashMap<>();
        if (this.parameters != null) {
            params.putAll(this.parameters);
        }

        return new URL(protocol, host, port, path, params);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return this.getParameter(VERSION_KEY, VERSION_DEFAULT);
    }

    public String getGroup() {
        return this.getParameter(GROUP_KEY);
    }

    public String getApplication() {
        return this.getParameter(APPLICATION_KEY);
    }

    public String getModule() {
        return this.getParameter(MODULE_KEY);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getParameter(String name, String defaultValue) {
        String value = getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public void addParameter(String name, Object value) {
        if (name == null || name.length() == 0 || value == null) {
            return;
        }

        parameters.put(name, String.valueOf(value));
    }

    public void removeParameter(String name) {
        if (name != null) {
            parameters.remove(name);
        }
    }

    public void addParameters(Map<String, String> params) {
        parameters.putAll(params);
    }

    public void addParameterIfAbsent(String name, String value) {
        if (hasParameter(name)) {
            return;
        }
        parameters.put(name, value);
    }

    public boolean getParameter(String name, boolean defaultValue) {
        String value = getParameter(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }

    public int getParameter(String name, int defaultValue) {
        Number n = getNumbers().get(name);
        if (n != null) {
            return n.intValue();
        }

        String value = parameters.get(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        int i = Integer.parseInt(value);
        getNumbers().put(name, i);

        return i;
    }

    public long getParameter(String name, long defaultValue) {
        Number n = getNumbers().get(name);
        if (n != null) {
            return n.longValue();
        }

        String value = parameters.get(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        long l = Long.parseLong(value);
        getNumbers().put(name, l);

        return l;
    }

    public float getParameter(String name, float defaultValue) {
        Number n = getNumbers().get(name);
        if (n != null) {
            return n.floatValue();
        }

        String value = parameters.get(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        float f = Float.parseFloat(value);
        getNumbers().put(name, f);

        return f;
    }

    public enum EET {
        aaa, bbb, ccc;
    }

    public static void main(String[] args) {
        EET ee = EET.aaa;
        EET[] eets = ee.getClass().getEnumConstants();
        for (EET e : eets) {
            System.out.println("==>" + (e instanceof Enum));
            System.out.println(e);
        }
    }

    public <E extends Enum<E>> E getParameter(String name, E defaultValue) {
        String value = this.getParameter(name);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        Object[] array = defaultValue.getClass().getEnumConstants();
        for (Object e : array) {
            E temp = (E) e;
            if (temp.name().equals(name)) {
                return temp;
            }
        }

        return defaultValue;
    }

    public String getUri() {
        return protocol + "://" + host + ":" + port + "/" + path;
    }

    public boolean hasParameter(String key) {
        String value = this.getParameter(key);
        return !(value == null || value.length() == 0);
    }

    public String getServerPortStr() {
        return buildHostPortStr(host, port);
    }

    private Map<String, Number> getNumbers() {
        if (numbers == null) {
            numbers = new ConcurrentHashMap<>();
        }

        return numbers;
    }

    @Override
    public int hashCode() {
        int factor = 31;
        int rs = 1;
        rs = factor * rs + Objects.hashCode(protocol);
        rs = factor * rs + Objects.hashCode(host);
        rs = factor * rs + Objects.hashCode(port);
        rs = factor * rs + Objects.hashCode(path);
        rs = factor * rs + Objects.hashCode(parameters);

        return rs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof URL)) {
            return false;
        }

        URL ou = (URL) obj;
        if (!Objects.equals(this.protocol, ou.protocol)) {
            return false;
        }
        if (!Objects.equals(this.host, ou.host)) {
            return false;
        }
        if (!Objects.equals(this.port, ou.port)) {
            return false;
        }
        if (!Objects.equals(this.path, ou.path)) {
            return false;
        }

        return Objects.equals(this.parameters, ou.parameters);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getUri()).append("?");

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();

            builder.append(name).append("=").append(value).append("&");
        }

        return builder.toString();
    }

    public static String encode(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }

        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String decode(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }

        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}