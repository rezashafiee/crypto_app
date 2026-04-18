package com.tilda.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class CryptoIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(NoAndroidLogDetector.ISSUE)

    override val api: Int = CURRENT_API
}
