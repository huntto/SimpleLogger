/*
 * Copyright 2019 xiangtao
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.ihuntto.simplelogger.compiler;

import com.google.auto.service.AutoService;
import com.ihuntto.simplelogger.annotation.SimpleLog;
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
                if (each.getKind() == ElementKind.CLASS) {
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
