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

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

import javax.annotation.Nonnull;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

class SimpleLogTranslator extends TreeTranslator {

    private TreeMaker mTreeMaker;
    private Name.Table mNames;
    private Messager mMessager;
    private String mTag;
    private JCTree.JCExpression mTagExpression;

    SimpleLogTranslator(TreeMaker treeMaker, Name.Table names, Messager messager) {
        mTreeMaker = treeMaker;
        mNames = names;
        mMessager = messager;
    }

    void setTag(@Nonnull String tag) {
        mTag = tag;
        mTagExpression = mTreeMaker.Literal(tag);
    }

    @Override
    public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
        super.visitMethodDef(jcMethodDecl);
        note("visit:" + jcMethodDecl.getName());

        JCTree.JCStatement logStatement = generateLogStatement(jcMethodDecl);

        addLogToMethod(jcMethodDecl, logStatement);

        note("added SimpleLog to method " + jcMethodDecl.name);
    }

    private void addLogToMethod(JCTree.JCMethodDecl jcMethodDecl, JCTree.JCStatement logStatement) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        List<JCTree.JCStatement> methodStatements = jcMethodDecl.getBody().getStatements();
        if (methodStatements.size() > 0) {
            JCTree.JCStatement firstStatement = methodStatements.get(0);
            // super(...) statement must before log statement in constructor
            if (firstStatement.toString().startsWith("super(")) {
                statements.append(firstStatement);
                statements.append(logStatement);
            } else {
                statements.append(logStatement);
                statements.append(firstStatement);
            }
        } else {
            statements.add(logStatement);
        }
        for (int i = 1; i < methodStatements.size(); i++) {
            statements.append(jcMethodDecl.getBody().getStatements().get(i));
        }

        JCTree.JCBlock body = mTreeMaker.Block(0, statements.toList());

        result = mTreeMaker.MethodDef(
                jcMethodDecl.getModifiers(),
                mNames.fromString(jcMethodDecl.getName().toString()),
                jcMethodDecl.restype,
                jcMethodDecl.getTypeParameters(),
                jcMethodDecl.getParameters(),
                jcMethodDecl.getThrows(),
                body,
                jcMethodDecl.defaultValue
        );
    }

    /**
     * generate statement of android.util.Log.d(TAG, params = values);
     */
    private JCTree.JCStatement generateLogStatement(JCTree.JCMethodDecl jcMethodDecl) {
        JCTree.JCFieldAccess logMethod = mTreeMaker.Select(
                mTreeMaker.Select(
                        mTreeMaker.Select(
                                mTreeMaker.Ident(mNames.fromString("android")),
                                mNames.fromString("util")
                        ),
                        mNames.fromString("Log")
                ),
                mNames.fromString("d")
        );

        JCTree.JCExpression msgExpression = generateLogMessageExpression(jcMethodDecl);
        JCTree.JCMethodInvocation methodInvocation = mTreeMaker.Apply(
                List.<JCTree.JCExpression>nil(),
                logMethod,
                List.of(mTagExpression, msgExpression)
        );
        return mTreeMaker.Exec(methodInvocation);
    }

    /**
     * generate expression of param1 = value1, param2 = value2, ... ;
     */
    private JCTree.JCExpression generateLogMessageExpression(JCTree.JCMethodDecl jcMethodDecl) {
        JCTree.JCExpression msgExpression = mTreeMaker.Literal(jcMethodDecl.name.toString() + "(");
        if (jcMethodDecl.params.size() > 0) {
            boolean first = true;
            for (JCTree.JCVariableDecl jcVariableDecl : jcMethodDecl.params) {
                if (!first) {
                    // add ,
                    msgExpression = mTreeMaker.Binary(JCTree.Tag.PLUS, msgExpression, mTreeMaker.Literal(", "));
                }
                if (jcVariableDecl.sym == null) {
                    continue;
                }
                first = false;
                JCTree.JCExpression paramName = mTreeMaker.Literal(jcVariableDecl.getName().toString() + " = ");
                JCTree.JCExpression paramValue = mTreeMaker.Ident(jcVariableDecl);
                msgExpression = mTreeMaker.Binary(
                        JCTree.Tag.PLUS,
                        msgExpression,
                        mTreeMaker.Binary(JCTree.Tag.PLUS, paramName, paramValue)
                );
            }
        }
        return mTreeMaker.Binary(JCTree.Tag.PLUS, msgExpression, mTreeMaker.Literal(")"));
    }

    private void note(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, mTag + ": " + msg);
    }

}
