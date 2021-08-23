package com.stkent.lintchecks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiClassType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement

@Suppress("UnstableApiUsage")
class FragmentAnimationsDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UCallExpression::class.java)
    }

    override fun getApplicableMethodNames(): List<String> {
        return listOf("setCustomAnimations")
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                val receiverType = (node.receiverType as? PsiClassType)?.resolve()?.qualifiedName
                if (receiverType == "androidx.fragment.app.FragmentTransaction") {
                    report(context, node)
                }
            }
        }
    }

    private fun report(context: JavaContext, node: UCallExpression) {
        context.report(
            issue = ISSUE,
            location = context.getLocation(node),
            message = "Should setCustomAnimations be replaced by safeSetCustomAnimations?",
        )
    }

    companion object {
        val ISSUE = Issue.create(
            id = "UnconditionalFragmentAnimations",
            briefDescription = "Unconditional Fragment Animations",
            explanation = """
                    This check highlights calls to FragmentTransaction::setCustomAnimations.
                    Developers should either replace these calls with ::safeSetCustomAnimations or
                    suppress the warning if the code already verifies that animations are enabled.
                    """,
            category = Category.A11Y,
            priority = 6,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                FragmentAnimationsDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
