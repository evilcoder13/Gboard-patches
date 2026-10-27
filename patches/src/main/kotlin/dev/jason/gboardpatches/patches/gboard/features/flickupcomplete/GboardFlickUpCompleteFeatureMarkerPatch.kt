package dev.jason.gboardpatches.patches.gboard.features.flickupcomplete

import app.morphe.patcher.patch.resourcePatch
import dev.jason.gboardpatches.patches.gboard.features.featureflags.applyFeatureMarker
import dev.jason.gboardpatches.patches.shared.Constants.COMPATIBILITY_GBOARD

private const val FLICK_UP_WORD_COMPLETION_FEATURE_METADATA =
    "dev.jason.gboardpatches.feature.flick_up_word_completion"

internal val gboardFlickUpCompleteFeatureMarkerPatch = resourcePatch(
    description = "Mark Flick-Up Word Completion feature in target APK for settings UI.",
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    finalize {
        applyFeatureMarker(FLICK_UP_WORD_COMPLETION_FEATURE_METADATA)
    }
}
