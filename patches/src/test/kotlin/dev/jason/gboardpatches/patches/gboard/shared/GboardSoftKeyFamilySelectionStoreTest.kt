package dev.jason.gboardpatches.patches.gboard.shared

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GboardSoftKeyFamilySelectionStoreTest {
    @Test
    fun `take returns each selected feature once and clears the context`() {
        val store = GboardSoftKeyFamilySelectionStore<Any>()
        val context = Any()

        store.add(context, GboardSoftKeyFamilyFeature.LONG_PRESS_QUICK_ACTIONS)
        store.add(context, GboardSoftKeyFamilyFeature.LONG_PRESS_QUICK_ACTIONS)
        store.add(context, GboardSoftKeyFamilyFeature.ENGLISH_QWERTY)

        assertEquals(
            setOf(
                GboardSoftKeyFamilyFeature.LONG_PRESS_QUICK_ACTIONS,
                GboardSoftKeyFamilyFeature.ENGLISH_QWERTY,
            ),
            store.take(context).toSet(),
        )
        assertTrue(store.take(context).isEmpty())
    }
}
