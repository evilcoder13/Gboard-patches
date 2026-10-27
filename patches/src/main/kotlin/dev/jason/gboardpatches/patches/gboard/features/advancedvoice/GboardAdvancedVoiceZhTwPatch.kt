package dev.jason.gboardpatches.patches.gboard.features.advancedvoice

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.jason.gboardpatches.patches.gboard.shared.GboardFieldTarget
import dev.jason.gboardpatches.patches.gboard.shared.GboardMethodTarget
import dev.jason.gboardpatches.patches.gboard.shared.addHelperMethodIfMissing
import dev.jason.gboardpatches.patches.gboard.shared.gboardPatchesExtensionCarrierPatch
import dev.jason.gboardpatches.patches.gboard.shared.isOpcode
import dev.jason.gboardpatches.patches.gboard.shared.mutableClass
import dev.jason.gboardpatches.patches.gboard.shared.runtimeabi.RuntimeCallEmitter
import dev.jason.gboardpatches.patches.gboard.shared.runtimeabi.RuntimeCallId
import dev.jason.gboardpatches.patches.shared.Constants.COMPATIBILITY_GBOARD

internal val gboardAdvancedVoice1803ZhTwPatch = bytecodePatch(
    description = "Verify native dictation payload readiness and seed application context for Advanced Voice Typing.",
) {
    compatibleWith(COMPATIBILITY_GBOARD)
    dependsOn(gboardPatchesExtensionCarrierPatch)
    execute {
        val readiness = GboardMethodTarget("Lssi;", "a", emptyList(), "Z").resolve(this)
        val eligibility = GboardMethodTarget(
            "Lssx;",
            "<init>",
            listOf(
                "Landroid/content/Context;", "Ltwj;", "Luym;", "Lsjt;", "Ltwj;", "Ltwj;", "Ltcy;",
                "Ltwj;", "Ltdf;", "Lsyl;", "Llez;", "Lsxs;", "Lsya;", "Lvec;", "Lszj;", "Ltdj;",
                "Ltfh;", "Lsiy;", "Lstm;", "Lssl;", "Ltwj;", "Laclu;", "Ljava/util/Set;",
            ),
            "V",
        ).resolve(this)
        val mdd = GboardMethodTarget(
            "Lsjt;",
            "<init>",
            listOf("Landroid/content/Context;", "Lsju;", "Lsjo;", "Laclu;"),
            "V",
        ).resolve(this)
        GboardFieldTarget("Lssi;", "b", "Lwhq;").resolve(this)
        GboardMethodTarget("Lwhq;", "get", emptyList(), "Ljava/lang/Object;").resolve(this)
        mutableClass("Lssi;").addHelperMethodIfMissing(
            "morpheNativeSplitReady",
            listOf("Z"),
            "Z",
            0x1a,
            3,
            advancedVoice1853NativeBody(),
        )
        val normalReadinessReturns = readiness.implementation!!.instructions.indices.filter {
            readiness.implementation!!.instructions[it].isOpcode("RETURN")
        }
        check(normalReadinessReturns.size == 2)
        normalReadinessReturns.asReversed().forEach { i ->
            val r = (readiness.implementation!!.instructions[i] as OneRegisterInstruction).registerA
            readiness.addInstructions(
                i,
                """
                    invoke-static {v$r}, Lssi;->morpheNativeSplitReady(Z)Z
                    move-result v$r
                """.trimIndent(),
            )
        }
        eligibility.addInstructions(
            0,
            RuntimeCallEmitter.invoke(
                RuntimeCallId.ADVANCED_VOICE_RUNTIME_SEED_APPLICATION_CONTEXT,
                "p1 .. p1",
            ),
        )
        mdd.addInstructions(
            0,
            RuntimeCallEmitter.invoke(
                RuntimeCallId.ADVANCED_VOICE_RUNTIME_SEED_APPLICATION_CONTEXT,
                "p1 .. p1",
            ),
        )
    }
}

internal fun advancedVoice1853NativeBody() = """
    :start
    ${RuntimeCallEmitter.invoke(RuntimeCallId.ADVANCED_VOICE_RUNTIME_SHOULD_CHECK_NATIVE, "p0")}
    move-result v0
    if-eqz v0, :done
    sget-object v0, Lssi;->b:Lwhq;
    invoke-interface {v0}, Lwhq;->get()Ljava/lang/Object;
    move-result-object v0
    check-cast v0, Ljava/lang/Boolean;
    invoke-virtual {v0}, Ljava/lang/Boolean;->booleanValue()Z
    move-result v0
    if-eqz v0, :done
    const/4 p0, 0x1
    :done
    return p0
    :end
    .catch Ljava/lang/Throwable; {:start .. :end} :failed
    :failed
    move-exception v0
    return p0
""".trimIndent()
