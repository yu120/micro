package org.micro.annotation;

import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.google.common.base.Throwables;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.micro.annotation.MicroData")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MicroGetterProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(MicroGetterProcessor.class);

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
        for (Element element : annotation) {
            com.sun.tools.javac.tree.JCTree jcTree = trees.getTree(element);
            jcTree.accept(new com.sun.tools.javac.tree.TreeTranslator() {
                @Override
                public void visitClassDef(com.sun.tools.javac.tree.JCTree.JCClassDecl jcClass) {
                    //过滤属性
                    Map<com.sun.tools.javac.util.Name, com.sun.tools.javac.tree.JCTree.JCVariableDecl> treeMap = new HashMap<>();
                    for (com.sun.tools.javac.tree.JCTree jcTree : jcClass.defs) {
                        if (jcTree.getKind().equals(Tree.Kind.VARIABLE)) {
                            com.sun.tools.javac.tree.JCTree.JCVariableDecl jcVariableDecl = (com.sun.tools.javac.tree.JCTree.JCVariableDecl) jcTree;
                            treeMap.put(jcVariableDecl.getName(), jcVariableDecl);
                        }
                    }

                    //处理变量
                    for (Map.Entry<com.sun.tools.javac.util.Name, com.sun.tools.javac.tree.JCTree.JCVariableDecl> entry : treeMap.entrySet()) {
                        messager.printMessage(Diagnostic.Kind.NOTE, String.format("fields:%s", entry.getKey()));
                        try {
                            //增加get方法
                            jcClass.defs = jcClass.defs.prepend(generateGetterMethod(entry.getValue()));
                            //增加set方法
                            jcClass.defs = jcClass.defs.prepend(generateSetterMethod(entry.getValue()));
                        } catch (Exception e) {
                            messager.printMessage(Diagnostic.Kind.ERROR, Throwables.getStackTraceAsString(e));
                        }
                    }

                    super.visitClassDef(jcClass);
                }

                @Override
                public void visitMethodDef(com.sun.tools.javac.tree.JCTree.JCMethodDecl jcMethod) {
                    //打印所有方法
                    messager.printMessage(Diagnostic.Kind.NOTE, jcMethod.toString());
                    //修改方法
                    if ("getTest".equals(jcMethod.getName().toString())) {
                        result = treeMaker.MethodDef(
                                jcMethod.getModifiers(),
                                getNameFromString("testMethod"),
                                jcMethod.restype,
                                jcMethod.getTypeParameters(),
                                jcMethod.getParameters(),
                                jcMethod.getThrows(),
                                jcMethod.getBody(),
                                jcMethod.defaultValue);
                    }
                    super.visitMethodDef(jcMethod);
                }
            });
        }

        return true;
    }

    private com.sun.tools.javac.tree.JCTree.JCMethodDecl generateGetterMethod(com.sun.tools.javac.tree.JCTree.JCVariableDecl jcVariable) {
        //修改方法级别
        com.sun.tools.javac.tree.JCTree.JCModifiers jcModifiers = treeMaker.Modifiers(com.sun.tools.javac.code.Flags.PUBLIC);
        //添加方法名称
        com.sun.tools.javac.util.Name methodName = handleMethodSignature(jcVariable.getName(), "get");

        //添加方法内容
        com.sun.tools.javac.util.ListBuffer<com.sun.tools.javac.tree.JCTree.JCStatement> jcStatements = new com.sun.tools.javac.util.ListBuffer<>();
        jcStatements.append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(getNameFromString("this")), jcVariable.getName())));
        com.sun.tools.javac.tree.JCTree.JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        //添加返回值类型
        com.sun.tools.javac.tree.JCTree.JCExpression returnType = jcVariable.vartype;
        //参数类型
        List<com.sun.tools.javac.tree.JCTree.JCTypeParameter> typeParameters = List.nil();
        //参数变量
        List<com.sun.tools.javac.tree.JCTree.JCVariableDecl> parameters = List.nil();
        //声明异常
        List<com.sun.tools.javac.tree.JCTree.JCExpression> throwsClauses = List.nil();
        //构建方法
        return treeMaker.MethodDef(jcModifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    private com.sun.tools.javac.tree.JCTree.JCMethodDecl generateSetterMethod(com.sun.tools.javac.tree.JCTree.JCVariableDecl jcVariable) throws ReflectiveOperationException {
        //修改方法级别
        com.sun.tools.javac.tree.JCTree.JCModifiers modifiers = treeMaker.Modifiers(com.sun.tools.javac.code.Flags.PUBLIC);

        //添加方法名称
        com.sun.tools.javac.util.Name variableName = jcVariable.getName();
        com.sun.tools.javac.util.Name methodName = handleMethodSignature(variableName, "set");

        //设置方法体
        com.sun.tools.javac.util.ListBuffer<com.sun.tools.javac.tree.JCTree.JCStatement> jcStatements = new com.sun.tools.javac.util.ListBuffer<>();
        jcStatements.append(treeMaker.Exec(treeMaker
                .Assign(treeMaker.Select(treeMaker.Ident(getNameFromString("this")), variableName),
                        treeMaker.Ident(variableName))));
        //定义方法体
        com.sun.tools.javac.tree.JCTree.JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        //添加返回值类型
        com.sun.tools.javac.tree.JCTree.JCExpression returnType = treeMaker.Type((Type) (Class.forName("com.sun.tools.javac.code.Type$JCVoidType").newInstance()));
        List<com.sun.tools.javac.tree.JCTree.JCTypeParameter> typeParameters = List.nil();

        //定义参数
        com.sun.tools.javac.tree.JCTree.JCVariableDecl variableDecl = treeMaker
                .VarDef(treeMaker.Modifiers(com.sun.tools.javac.code.Flags.PARAMETER, List.nil()), jcVariable.name, jcVariable.vartype, null);
        List<com.sun.tools.javac.tree.JCTree.JCVariableDecl> parameters = List.of(variableDecl);

        //声明异常
        List<com.sun.tools.javac.tree.JCTree.JCExpression> throwsClauses = List.nil();
        return treeMaker.MethodDef(modifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    private com.sun.tools.javac.util.Name handleMethodSignature(com.sun.tools.javac.util.Name name, String prefix) {
        return names.fromString(prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name.toString()));
    }

    private com.sun.tools.javac.util.Name getNameFromString(String s) {
        return names.fromString(s);
    }

}