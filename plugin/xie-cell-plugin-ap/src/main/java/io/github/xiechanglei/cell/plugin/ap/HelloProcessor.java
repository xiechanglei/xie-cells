package io.github.xiechanglei.cell.plugin.ap;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
/**
 * Hello注解处理器
 * 该处理器会扫描被@Hello注解标记的类，并向类中的每个方法添加System.out.println("hello world!");语句
 * 这是一个基于Javac内部API的编译时代码增强处理器，展示了如何在编译时修改AST（抽象语法树）
 *
 * @author xie
 * @date 2026/2/11
 */
@AutoService(Processor.class)  // 使用AutoService自动生成META-INF/services配置文件
@SupportedAnnotationTypes("io.github.xiechanglei.cell.plugin.ap.Hello")  // 声明该处理器支持处理的注解类型
@SupportedSourceVersion(SourceVersion.RELEASE_21)  // 声明支持的Java源码版本
public class HelloProcessor extends AbstractProcessor {
    private JavacTrees javacTrees; // 获取 JcTree
    private TreeMaker treeMaker; // 构建生成 jcTree
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"这是我的处理器1");
        javacTrees = JavacTrees.instance(processingEnv);// 语法树
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"这是我的处理器2");
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"这是我的处理器3");
        this.treeMaker = TreeMaker.instance(context);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"这是我的处理器4");
        super.init(processingEnv);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"这是我的处理器5");
        this.names = Names.instance(context);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"这是我的处理器6");

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.stream()
                .flatMap(t->roundEnv.getElementsAnnotatedWith(t).stream())
                .forEach(t->{
                    JCTree tree = javacTrees.getTree(t);
                    tree.accept(new TreeTranslator(){
                        @Override
                        public void visitMethodDef(JCTree.JCMethodDecl tree) {
                            JCTree.JCStatement sysout = treeMaker.Exec(
                                    treeMaker.Apply(
                                            List.nil(),
                                            select("System.out.println"),
                                            List.of(treeMaker.Literal("hello world!")) // 方法中的内容
                                    )
                            );
                            // 覆盖原有的语句块
                            tree.body.stats=tree.body.stats.append(sysout);
                            super.visitMethodDef(tree);
                        }
                    });
                });

        return true;
    }


    private JCTree.JCFieldAccess select(JCTree.JCExpression selected, String expressive) {
        return treeMaker.Select(selected, names.fromString(expressive));
    }

    private JCTree.JCFieldAccess select(String expressive) {
        String[] exps = expressive.split("\\.");
        JCTree.JCFieldAccess access = treeMaker.Select(ident(exps[0]), names.fromString(exps[1]));
        int index = 2;
        while (index < exps.length) {
            access = treeMaker.Select(access, names.fromString(exps[index++]));
        }
        return access;
    }

    private JCTree.JCIdent ident(String name) {
        return treeMaker.Ident(names.fromString(name));
    }
}