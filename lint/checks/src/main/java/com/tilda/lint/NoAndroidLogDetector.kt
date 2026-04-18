package com.tilda.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class NoAndroidLogDetector : Detector(), Detector.UastScanner {
    override fun getApplicableMethodNames(): List<String> = LOG_METHOD_NAMES

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val containingClassName = method.containingClass?.qualifiedName ?: return
        if (containingClassName == "android.util.Log") {
            context.report(
                issue = ISSUE,
                scope = node,
                location = context.getNameLocation(node),
                message = "Use Timber instead of android.util.Log."
            )
        }
    }

    companion object {
        private val LOG_METHOD_NAMES =
            listOf("v", "d", "i", "w", "e", "wtf", "println", "isLoggable")

        val ISSUE: Issue =
            Issue.create(
                id = "UseTimber",
                briefDescription = "android.util.Log usage is forbidden",
                explanation = "Use Timber for logging throughout the project instead of android.util.Log.",
                category = Category.CORRECTNESS,
                priority = 8,
                severity = Severity.ERROR,
                implementation =
                    Implementation(NoAndroidLogDetector::class.java, Scope.JAVA_FILE_SCOPE)
            )
    }
}
