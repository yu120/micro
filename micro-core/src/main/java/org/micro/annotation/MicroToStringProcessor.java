package org.micro.annotation;

import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.micro.annotation.MicroToString")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MicroToStringProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(MicroToStringProcessor.class);

    private Messager messager;
    private com.sun.tools.javac.api.JavacTrees trees;
    private com.sun.tools.javac.tree.TreeMaker treeMaker;
    private com.sun.tools.javac.util.Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        log.debug("Enter method init");
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = com.sun.tools.javac.api.JavacTrees.instance(processingEnv);
        Context context = ((com.sun.tools.javac.processing.JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = com.sun.tools.javac.tree.TreeMaker.instance(context);
        this.names = com.sun.tools.javac.util.Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log.debug("process========>{}", annotations);
        Set<? extends Element> annotation = roundEnv.getElementsAnnotatedWith(MicroData.class);
        annotation.stream().map(element -> trees.getTree(element)).forEach(tree -> tree.accept(new TreeTranslator() {

            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClass) {
                //过滤属性
                Map<Name, JCTree.JCVariableDecl> treeMap =
                        jcClass.defs.stream().filter(k -> k.getKind().equals(Tree.Kind.VARIABLE))
                                .map(tree -> (JCTree.JCVariableDecl) tree)
                                .collect(Collectors.toMap(JCTree.JCVariableDecl::getName, Function.identity()));
                //增加toString方法
                jcClass.defs = jcClass.defs.prepend(generateToStringBuilderMethod());
                super.visitClassDef(jcClass);
            }

            @Override
            public void visitMethodDef(JCTree.JCMethodDecl jcMethod) {
                //打印所有方法
                messager.printMessage(Diagnostic.Kind.NOTE, jcMethod.toString());
                //修改方法
                if ("getTest".equals(jcMethod.getName().toString())) {
                    result = treeMaker
                            .MethodDef(jcMethod.getModifiers(), getNameFromString("testMethod"), jcMethod.restype,
                                    jcMethod.getTypeParameters(), jcMethod.getParameters(), jcMethod.getThrows(),
                                    jcMethod.getBody(), jcMethod.defaultValue);
                }
                super.visitMethodDef(jcMethod);
            }
        }));
        return true;
    }

    private JCTree.JCMethodDecl generateToStringBuilderMethod() {

        //修改方法级别
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);

        //添加方法名称
        Name methodName = getNameFromString("toString");

        //设置调用方法函数类型和调用函数
        JCTree.JCExpressionStatement statement = treeMaker.Exec(treeMaker.Apply(List.of(memberAccess("java.lang.Object")),
                memberAccess("com.nicky.lombok.adapter.AdapterFactory.builderStyleAdapter"),
                List.of(treeMaker.Ident(getNameFromString("this")))));
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        jcStatements.append(treeMaker.Return(statement.getExpression()));
        //设置方法体
        JCTree.JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        //添加返回值类型
        JCTree.JCExpression returnType = memberAccess("java.lang.String");

        //参数类型
        List<JCTree.JCTypeParameter> typeParameters = List.nil();

        //参数变量
        List<JCTree.JCVariableDecl> parameters = List.nil();

        //声明异常
        List<JCTree.JCExpression> throwsClauses = List.nil();

        return treeMaker
                .MethodDef(modifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    private JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    private com.sun.tools.javac.util.Name handleMethodSignature(com.sun.tools.javac.util.Name name, String prefix) {
        return names.fromString(prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name.toString()));
    }

    private com.sun.tools.javac.util.Name getNameFromString(String s) {
        return names.fromString(s);
    }

}