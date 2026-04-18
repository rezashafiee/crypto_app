package com.tilda.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest

class NoAndroidLogDetectorTest : LintDetectorTest() {
    override fun getDetector() = NoAndroidLogDetector()

    override fun getIssues() = listOf(NoAndroidLogDetector.ISSUE)

    fun testReportsErrorWhenAndroidLogIsUsed() {
        // given
        val source =
            kotlin(
                """
                package test.pkg
                import android.util.Log

                class Sample {
                    fun call() {
                        Log.d("TAG", "message")
                    }
                }
                """.trimIndent()
            )

        // when
        val result = lint().files(source).run()

        // then
        result.expectContains("Use Timber instead of android.util.Log.")
    }

    fun testDoesNotReportWhenTimberIsUsed() {
        // given
        val source =
            kotlin(
                """
                package test.pkg
                import timber.log.Timber

                class Sample {
                    fun call() {
                        Timber.d("message")
                    }
                }
                """.trimIndent()
            )
        val timberStub =
            kotlin(
                """
                package timber.log

                object Timber {
                    fun d(message: String) = Unit
                }
                """.trimIndent()
            )

        // when
        val result = lint().files(source, timberStub).run()

        // then
        result.expectClean()
    }
}
