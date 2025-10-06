package com.tilda.konsisttest.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.containingFiles
import com.lemonappdev.konsist.api.ext.list.imports
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class DomainLayerImportsKonsistTest {

    val allowedImports = listOf(
        "androidx.paging.PagingData",
        "kotlinx.coroutines.flow",
        "java.time"
    )

    @Test
    fun `The domain layer must contain only allowed imports`() {
        Konsist
            .scopeFromProduction()
            .packages
            .filter { it.name.contains(".domain") }
            .containingFiles
            .imports
            .assertTrue {
                allowedImports.any { allowedImport ->
                    it.name.startsWith(allowedImport)
                        .or(
                            it.name.contains(".domain")
                        )
                }
            }
    }
}