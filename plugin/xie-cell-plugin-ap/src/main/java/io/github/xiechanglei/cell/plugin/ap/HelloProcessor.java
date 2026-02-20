package io.github.xiechanglei.cell.plugin.ap;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import io.github.xiechanglei.cell.plugin.ap.base.BaseProcessor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

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
public class HelloProcessor extends BaseProcessor {

    @Override
    protected void processTree(JCTree tree, Element element, TypeElement typeElement) {
        tree.accept(new TreeTranslator() {
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
                tree.body.stats = tree.body.stats.append(sysout);
                super.visitMethodDef(tree);
            }
        });
    }
}