package com.tilda.konsisttest.general

import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withModifier
import com.lemonappdev.konsist.api.verify.assertTrue
import kotlin.test.Test

class DataClassPropertiesKonsistTest {
    @Test
    fun `data classes should have 'val' properties`() {
        Konsist
            .scopeFromProduction()
            .classes()
            .withModifier(KoModifier.DATA)
            .assertTrue {
                it.properties().all { prop -> prop.isVal }
            }
    }

}