package dev.jason.gboardpatches.patches.gboard.registry

import dev.jason.gboardpatches.patches.gboard.features.closeproactivesuggestions.gboardCloseProactiveSuggestionsFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.closeproactivesuggestions.gboardCloseProactiveSuggestionsFlagValuePatch
import dev.jason.gboardpatches.patches.gboard.features.flickupcomplete.gboardFlickUpCompleteBytecodePatch
import dev.jason.gboardpatches.patches.gboard.features.flickupcomplete.gboardFlickUpCompleteFeatureMarkerPatch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class GboardRuntimePatchesContractTest {
    @Test
    fun `remaining features are independently published and enabled by default`() {
        val registrations = GboardPublishedPatchCatalog.morpheRegistrations
        assertTrue(registrations.none { it.name == "Hyperspeed Typing Animation" })
        val close = registrations.single { patch ->
            patch.name == "Close Proactive Suggestions"
        }
        val flickUp = registrations.single { patch -> patch.name == "Flick-Up Word Completion" }

        assertSame(gboardCloseProactiveSuggestionsPatch, close)
        assertSame(gboardFlickUpCompletePatch, flickUp)

        val generated = generatedPublishedPatches().associateBy { patch ->
            patch["name"].asString
        }
        assertTrue(generated.getValue(close.name!!)["use"].asBoolean)
        assertTrue(generated.getValue(flickUp.name!!)["use"].asBoolean)
    }

    @Test
    fun `each public patch owns only its feature marker and runtime contributions`() {
        assertTrue(gboardCloseProactiveSuggestionsPatch.dependencies.any { dependency ->
            dependency === gboardCloseProactiveSuggestionsFeatureMarkerPatch
        })
        assertTrue(gboardCloseProactiveSuggestionsPatch.dependencies.any { dependency ->
            dependency === gboardCloseProactiveSuggestionsFlagValuePatch
        })

        assertTrue(gboardFlickUpCompletePatch.dependencies.any { dependency ->
            dependency === gboardFlickUpCompleteFeatureMarkerPatch
        })
        assertTrue(gboardFlickUpCompletePatch.dependencies.any { dependency ->
            dependency === gboardFlickUpCompleteBytecodePatch
        })
    }

    @Test
    fun `public descriptions match the requested bilingual product copy`() {
        assertEquals(
            "在主動建議列顯示關閉按鈕\n" +
                "Show a dismiss button in the proactive suggestions bar.",
            gboardCloseProactiveSuggestionsPatch.description,
        )
        assertEquals(
            "向上滑動鍵盤左、中、右區域以快速選擇對應候選詞（BlackBerry 風格）\n" +
                "Flick up in the left, center, or right keyboard zone to commit the matching candidate word (BlackBerry style).",
            gboardFlickUpCompletePatch.description,
        )
    }
}
