package com.tilda.konsisttest.tests

import com.lemonappdev.konsist.api.Konsist
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Enforces that EVERY @Test function contains Given/When/Then markers within its body.
 * We look for line comments "// given", "// when", and "// then" (case-insensitive).
 */
class GivenWhenThenKonsistTest {
    @Test
    fun `every test function should contain given when then markers`() {
        // Collect all test source files from the whole project
        val testFiles = Konsist.scopeFromTest().files
            .filter { file ->
                val path = file.path
                !path.contains("/konsistTest/")
            }
            // Only keep files that actually have @Test annotations to avoid false positives
            .filter { file -> file.text.contains("@Test") }

        val violations = mutableListOf<String>()

        testFiles.forEach { file ->
            val fileText = file.text
            val testsInFile = extractTestFunctions(fileText)
            testsInFile.forEach { (name, body) ->
                val lc = body.lowercase()
                val hasGiven = lc.contains("// given")
                val hasWhen = lc.contains("// when")
                val hasThen = lc.contains("// then")
                if (!(hasGiven && hasWhen && hasThen)) {
                    violations += buildString {
                        append("Missing markers in ")
                        append(file.path)
                        append(" -> ")
                        append(name)
                        append(". Required markers: ")
                        if (!hasGiven) append("[// given] ")
                        if (!hasWhen) append("[// when] ")
                        if (!hasThen) append("[// then]")
                    }
                }
            }
        }

        assertTrue(
            violations.isEmpty(),
            message = buildString {
                appendLine("The following @Test functions are missing Given/When/Then markers:")
                violations.forEach { appendLine(" - $it") }
            }
        )
    }
}

private fun extractTestFunctions(fileText: String): List<Pair<String, String>> {
    // Returns list of (functionName, functionBodyText)
    val result = mutableListOf<Pair<String, String>>()
    var index = 0
    while (true) {
        val testIdx = fileText.indexOf("@Test", startIndex = index)
        if (testIdx == -1) break
        // Find the next "fun" after the @Test annotation
        val funIdx = fileText.indexOf("fun", startIndex = testIdx)
        if (funIdx == -1) break

        // Find the opening brace of the function body
        val bodyStart = fileText.indexOf('{', startIndex = funIdx)
        if (bodyStart == -1) {
            index = funIdx + 3
            continue
        }

        // Extract function header to get the name
        val headerText = fileText.substring(funIdx, bodyStart)
        val nameMatch = Regex("""fun\s+`?([A-Za-z0-9_]+)`?""").find(headerText)
        if (nameMatch != null) {
            val name = nameMatch.groupValues[1]
            val bodyEnd = findMatchingBrace(fileText, bodyStart)
            if (bodyEnd != -1) {
                result += name to fileText.substring(bodyStart, bodyEnd + 1)
                index = bodyEnd + 1
                continue
            }
        }
        // Fallback advance to avoid infinite loop
        index = bodyStart + 1
    }
    return result
}

private fun findMatchingBrace(text: String, openBraceIndex: Int): Int {
    var depth = 0
    for (i in openBraceIndex until text.length) {
        val c = text[i]
        if (c == '{') depth++
        if (c == '}') {
            depth--
            if (depth == 0) return i
        }
    }
    return -1
}
