package com.stkent.lintchecks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

@Suppress("UnstableApiUsage")
class Registry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(FragmentAnimationsDetector.ISSUE)

    override val api: Int = CURRENT_API
}
