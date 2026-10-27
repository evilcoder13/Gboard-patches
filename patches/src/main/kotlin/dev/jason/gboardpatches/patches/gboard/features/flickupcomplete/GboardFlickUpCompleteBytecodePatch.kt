package dev.jason.gboardpatches.patches.gboard.features.flickupcomplete

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.patcher.util.smali.ExternalLabel
import dev.jason.gboardpatches.patches.gboard.shared.findMutableMethodOrThrow
import dev.jason.gboardpatches.patches.gboard.shared.gboardPatchesExtensionCarrierPatch
import dev.jason.gboardpatches.patches.gboard.shared.isMethodReference
import dev.jason.gboardpatches.patches.gboard.shared.runtimeabi.RuntimeAbiCatalog
import dev.jason.gboardpatches.patches.gboard.shared.runtimeabi.RuntimeCallEmitter
import dev.jason.gboardpatches.patches.gboard.shared.runtimeabi.RuntimeCallId
import dev.jason.gboardpatches.patches.shared.Constants.COMPATIBILITY_GBOARD

internal val gboardFlickUpCompleteBytecodePatch = bytecodePatch(
    description = "Inject flick-up word completion touch interceptor into KeyboardViewHolder.dispatchTouchEvent.",
) {
    compatibleWith(COMPATIBILITY_GBOARD)
    dependsOn(gboardPatchesExtensionCarrierPatch)

    execute {
        findMutableMethodOrThrow(
            classType = KEYBOARD_VIEW_HOLDER_CLASS,
            name = DISPATCH_TOUCH_EVENT_NAME,
            returnType = DISPATCH_TOUCH_EVENT_RETURN,
            parameterTypes = listOf(MOTION_EVENT_TYPE),
        ).applyFlickUpCompleteTouchInterceptor()
    }
}

internal fun MutableMethod.applyFlickUpCompleteTouchInterceptor() {
    val abi = RuntimeAbiCatalog.abi(
        RuntimeCallId.FLICK_UP_COMPLETE_RUNTIME_ON_BODY_TOUCH,
    )
    val instructions = implementation?.instructions
        ?: error("No instructions in $definingClass->$name")
    val existing = instructions.count { it.isMethodReference(abi.reference) }
    if (existing > 0) {
        check(existing == 1) {
            "Malformed flick-up word completion touch interceptor in $definingClass->$name"
        }
        return
    }
    addInstructions(0, "nop")
    val continuation = implementation!!.instructions[0]
    addInstructionsWithLabels(
        0,
        """
            ${RuntimeCallEmitter.invoke(
                RuntimeCallId.FLICK_UP_COMPLETE_RUNTIME_ON_BODY_TOUCH,
                "p0, p1",
            )}

            move-result v0

            if-eqz v0, :jasondev_continue_flick_up_complete

            const/4 v0, 0x1

            return v0
        """.trimIndent(),
        ExternalLabel("jasondev_continue_flick_up_complete", continuation),
    )
}

private const val KEYBOARD_VIEW_HOLDER_CLASS =
    "Lcom/google/android/libraries/inputmethod/keyboard/impl/KeyboardViewHolder;"
private const val DISPATCH_TOUCH_EVENT_NAME = "dispatchTouchEvent"
private const val MOTION_EVENT_TYPE = "Landroid/view/MotionEvent;"
private const val DISPATCH_TOUCH_EVENT_RETURN = "Z"
