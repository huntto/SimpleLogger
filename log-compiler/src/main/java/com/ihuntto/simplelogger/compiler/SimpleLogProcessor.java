package com.ihuntto.simplelogger.compiler;

import com.google.auto.service.AutoService;
import com.ihuntto.simplelogger.annotations.SimpleLog;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class SimpleLogProcessor extends AbstractProcessor {
    private Trees trees;
    private SimpleLogTranslator visitor;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        trees = Trees.instance(processingEnv);
        messager = processingEnv.getMessager();
        Context context = ((JavacProcessingEnvironment)
                processingEnv).getContext();
        visitor = new SimpleLogTranslator(TreeMaker.instance(context), Names.instance(context).table, messager);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(SimpleLog.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            Set<? extends Element> elements =
                    roundEnv.getElementsAnnotatedWith(SimpleLog.class);
            for (Element each : elements) {
                if (each.getKind() == ElementKind.CLASS || each.getKind() == ElementKind.METHOD) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "handle element: " + each.getSimpleName());
                    JCTree jcTree = (JCTree) trees.getTree(each);
                    if (jcTree != null) {
                        visitor.setTag(each.getKind() == ElementKind.CLASS ?
                                each.getSimpleName().toString() :
                                each.getEnclosingElement().getSimpleName().toString());
                        jcTree.accept(visitor);
                    } else {
                        messager.printMessage(Diagnostic.Kind.NOTE, ">> jctree is null.");
                    }
                }
            }
        }
        return false;
    }
}
