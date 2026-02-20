package io.github.xiechanglei.cell.plugin.ap.base;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public abstract class BaseProcessor extends AbstractProcessor {
    protected JavacTrees javacTrees; // 获取 JcTree
    protected TreeMaker treeMaker; // 构建生成 jcTree
    protected Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        javacTrees = JavacTrees.instance(processingEnv);// 语法树
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        super.init(processingEnv);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                JCTree tree = javacTrees.getTree(element);
                processTree(tree, element, annotation);
            }
        }
        return false;
    }

    protected abstract void processTree(JCTree tree, Element element, TypeElement typeElement);

    protected JCTree.JCFieldAccess select(JCTree.JCExpression selected, String expressive) {
        return treeMaker.Select(selected, names.fromString(expressive));
    }

    protected JCTree.JCFieldAccess select(String expressive) {
        String[] exps = expressive.split("\\.");
        JCTree.JCFieldAccess access = treeMaker.Select(ident(exps[0]), names.fromString(exps[1]));
        int index = 2;
        while (index < exps.length) {
            access = treeMaker.Select(access, names.fromString(exps[index++]));
        }
        return access;
    }

    protected JCTree.JCIdent ident(String name) {
        return treeMaker.Ident(names.fromString(name));
    }
}