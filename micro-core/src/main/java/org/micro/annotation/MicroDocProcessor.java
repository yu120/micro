package org.micro.annotation;

import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.micro.annotation.MicroDoc")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MicroDocProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(MicroDocProcessor.class);

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        log.debug("MicroDocProcessor init=======================");
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log.debug("MicroDocProcessor process========>{}", annotations);
        return true;
    }

}