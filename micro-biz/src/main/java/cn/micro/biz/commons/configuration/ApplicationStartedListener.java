package cn.micro.biz.commons.configuration;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.DocumentationCache;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Application Started Listener
 *
 * @author lry
 */
@Component
public class ApplicationStartedListener implements ApplicationContextAware, ApplicationListener<ApplicationStartedEvent> {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(@Nullable ApplicationStartedEvent event) {
//        List<MicroClass> microClassList = MicroDoc.readInfo();
//        System.out.println(JSON.toJSONString(microClassList));

        DocumentationCache documentationCache = applicationContext.getBean(DocumentationCache.class);
        Map<String, Documentation> documentationMap = documentationCache.all();
        for (Map.Entry<String, Documentation> entry : documentationMap.entrySet()) {
            Documentation documentation = entry.getValue();
            Set<Tag> newTagSet = new LinkedHashSet<>();
            Set<Tag> tagSet = documentation.getTags();
            for (Tag tag : tagSet) {
                String description = tag.getDescription() + "[自定义描述内容]";
                newTagSet.add(new Tag(tag.getName(), description, tag.getOrder(), tag.getVendorExtensions()));
            }
            Documentation newDocumentation = new Documentation(
                    documentation.getGroupName(),
                    documentation.getBasePath(),
                    newTagSet,
                    documentation.getApiListings(),
                    documentation.getResourceListing(),
                    new LinkedHashSet<>(documentation.getProduces()),
                    new LinkedHashSet<>(documentation.getConsumes()),
                    documentation.getHost(),
                    new LinkedHashSet<>(documentation.getSchemes()),
                    documentation.getVendorExtensions());
            documentationCache.addDocumentation(newDocumentation);
        }

        System.out.println("启动成功:" + documentationCache);
    }

}
