package com.tilda.konsisttest.android

import androidx.lifecycle.ViewModel
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.ext.list.containingFiles
import com.lemonappdev.konsist.api.ext.list.imports
import com.lemonappdev.konsist.api.ext.list.withAllParentsOf
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Before
import org.junit.Test

class ViewModelImportsKonsistTest {

    lateinit var viewModels: List<KoClassDeclaration>

    @Before
    fun setup() {
        val productionCode = Konsist.scopeFromProduction()
        viewModels = productionCode
            .classes()
            .withAllParentsOf(ViewModel::class)

    }

    @Test
    fun `viewModels should not have problematic android imports`() {
        val forbiddenImports = listOf(
            "android.widget.",
            "android.view.",
            "android.app.Activity",
            "android.app.Fragment",
            "android.content.Context"
        )

        viewModels
            .assertTrue(
                additionalMessage = "ViewModels should not import directly from UI packages like android.widget or android.view."
            ) {
                it.containingFile
                    .imports.none { import ->
                        forbiddenImports.any { forbidden ->
                            import.name.contains(
                                forbidden
                            )
                        }
                    }
            }
    }

    @Test
    fun `viewModels should not have access to resources`() {
        viewModels
            .containingFiles
            .imports
            .assertFalse { it.name.endsWith(".R") }
    }
}