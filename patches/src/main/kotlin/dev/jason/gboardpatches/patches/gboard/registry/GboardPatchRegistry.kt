package dev.jason.gboardpatches.patches.gboard.registry

import app.morphe.patcher.patch.Patch
import app.morphe.patcher.patch.ResourcePatchBuilder
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import dev.jason.gboardpatches.patches.gboard.features.about.gboardAboutPageResourcePatch
import dev.jason.gboardpatches.patches.gboard.features.advancedvoice.gboardAdvancedVoiceFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.backuprestore.gboardBackupRestoreFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.backuprestore.gboardBackupRestoreManifestPatch
import dev.jason.gboardpatches.patches.gboard.features.bluetoothmicrophone.gboardBluetoothMicrophoneFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.bluetoothmicrophone.gboardBluetoothMicrophoneFlagValuePatch
import dev.jason.gboardpatches.patches.gboard.features.clipboard.gboardClipboardAdapterTrimPatch
import dev.jason.gboardpatches.patches.gboard.features.clipboard.gboardClipboardColumnCountPatch
import dev.jason.gboardpatches.patches.gboard.features.clipboard.gboardClipboardFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.clipboard.gboardClipboardItemBindPatch
import dev.jason.gboardpatches.patches.gboard.features.clipboard.gboardClipboardLoaderPatch
import dev.jason.gboardpatches.patches.gboard.features.clipboard.gboardClipboardPrunePatch
import dev.jason.gboardpatches.patches.gboard.features.clipboardcontentlimit.gboardClipboardContentLimitFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.clipboardcontentlimit.gboardClipboardContentLimitFlagValuePatch
import dev.jason.gboardpatches.patches.gboard.features.closeproactivesuggestions.gboardCloseProactiveSuggestionsFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.closeproactivesuggestions.gboardCloseProactiveSuggestionsFlagValuePatch
import dev.jason.gboardpatches.patches.gboard.features.cursortrackpad.gboardCursorTrackpadFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.cursortrackpad.gboardCursorTrackpadFlagValuePatch
import dev.jason.gboardpatches.patches.gboard.features.editingaccesspoints.gboardEditingAccessPointsFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.editingaccesspoints.gboardEditingAccessPointsLifecyclePatch
import dev.jason.gboardpatches.patches.gboard.features.englishqwerty.gboardEnglishUppercaseToggleFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.featureflags.gboardDeviceIntelligenceFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.featureflags.gboardDeviceIntelligenceFlagValuePatch
import dev.jason.gboardpatches.patches.gboard.features.featureflags.gboardGrammarCheckerFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.featureflags.gboardGrammarCheckerFlagValuePatch
import dev.jason.gboardpatches.patches.gboard.features.featureflags.gboardInlineSuggestionsFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.featureflags.gboardInlineSuggestionsFlagValuePatch
import dev.jason.gboardpatches.patches.gboard.features.flickupcomplete.gboardFlickUpCompleteBytecodePatch
import dev.jason.gboardpatches.patches.gboard.features.flickupcomplete.gboardFlickUpCompleteFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.latinglobe.gboardLatinGlobeKeyIgnoreIntervalBytecodePatch
import dev.jason.gboardpatches.patches.gboard.features.latinglobe.gboardLatinGlobeKeyIgnoreIntervalFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.longpressquickactions.gboardLongPressQuickActionsFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.manualincognito.gboardManualIncognitoFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.manualincognito.gboardManualIncognitoLifecyclePatch
import dev.jason.gboardpatches.patches.gboard.features.manualincognito.gboardManualIncognitoPolicyPatch
import dev.jason.gboardpatches.patches.gboard.features.ocr.gboardOcrFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.packagerename.applyGboardApplicationDisplayName
import dev.jason.gboardpatches.patches.gboard.features.packagerename.gboardPackageRenameResourcePatch
import dev.jason.gboardpatches.patches.gboard.features.packagerename.isValidGboardAppDisplayName
import dev.jason.gboardpatches.patches.gboard.features.signaturebypass.gboardSignatureBypassBytecodePatch
import dev.jason.gboardpatches.patches.gboard.features.splitkeyboard.gboardSplitKeyboardAccessPointPatch
import dev.jason.gboardpatches.patches.gboard.features.splitkeyboard.gboardSplitKeyboardFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.features.splitkeyboard.gboardSplitKeyboardModePatch
import dev.jason.gboardpatches.patches.gboard.features.swipedismiss.gboardSwipeDismissBytecodePatch
import dev.jason.gboardpatches.patches.gboard.features.swipedismiss.gboardSwipeDismissFeatureMarkerPatch
import dev.jason.gboardpatches.patches.gboard.shared.accesspoint.gboardAccessPointContributions1803Patch
import dev.jason.gboardpatches.patches.gboard.shared.gboardPatchesExtensionCarrierPatch
import dev.jason.gboardpatches.patches.gboard.shared.gboardPatchesSettingsPatch
import dev.jason.gboardpatches.patches.gboard.shared.generated.GboardTargetAdmission
import dev.jason.gboardpatches.patches.shared.Constants.COMPATIBILITY_GBOARD

private fun gboardPublicResourcePatch(
    featureId: String,
    name: String,
    description: String,
    default: Boolean,
    block: ResourcePatchBuilder.() -> Unit,
) = GboardTargetAdmission.admittedContributions(featureId).let { admitted ->
    resourcePatch(
        name = name.takeIf { admitted.isNotEmpty() },
        description = description,
        default = default,
    ) {
        if (admitted.isNotEmpty()) {
            block()
            GboardContributionWiring.wireAdmitted(featureId, admitted, this)
        }
    }
}

@Suppress("unused")
val gboardEnglishQwertySlideUppercaseTogglePatch = gboardPublicResourcePatch(
    featureId = "english_qwerty_up_flick_uppercase",
    name = "English QWERTY Up-Flick Uppercase",
    description = "英文 QWERTY 鍵盤上滑大小寫\nFlick up on the English QWERTY keyboard to toggle uppercase and lowercase.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardAboutPageResourcePatch,
        gboardPatchesSettingsPatch,
        gboardEnglishUppercaseToggleFeatureMarkerPatch,
    )
}

@Suppress("unused")
val gboardFlickUpWordCompletionPatch = gboardPublicResourcePatch(
    featureId = "flick_up_word_completion",
    name = "Flick-Up Word Completion",
    description = "在鍵盤上向上滑動（左/中/右區域）以快速選擇對應位置的候選字（BlackBerry 風格）\n" +
        "Flick up on the left, center, or right zone of the keyboard to select the corresponding candidate word (BlackBerry style).",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardFlickUpCompleteFeatureMarkerPatch,
        gboardFlickUpCompleteBytecodePatch,
    )
}

@Suppress("unused")
val gboardLongPressQuickActionsPatch = gboardPublicResourcePatch(
    featureId = "long_press_editing_shortcuts",
    name = "Long-Press Editing Shortcuts",
    description = "在英文 QWERTY 鍵盤加入全選、復原、複製、剪下、貼上與重做長按快捷鍵\n" +
        "Add Select all, Undo, Copy, Cut, Paste, and Redo long-press shortcuts " +
        "to the English QWERTY keyboard.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardLongPressQuickActionsFeatureMarkerPatch,
    )
}

@Suppress("unused")
val gboardManualIncognitoModePatch = gboardPublicResourcePatch(
    featureId = "incognito_mode_toggle",
    name = "Incognito Mode Toggle",
    description = "在 Access Point 工具列新增無痕模式切換按鈕，並可設定無痕模式下是否啟用剪貼簿與語音輸入\n" +
        "Add an Incognito toggle to the Access Point toolbar and configure clipboard and voice typing availability while Incognito mode is active.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardManualIncognitoFeatureMarkerPatch,
        gboardManualIncognitoLifecyclePatch,
        gboardManualIncognitoPolicyPatch,
        gboardAccessPointContributions1803Patch,
    )
}

@Suppress("unused")
val gboardEditingAccessPointsPatch = gboardPublicResourcePatch(
    featureId = "editing_access_points",
    name = "Toolbar Editing Buttons",
    description = "將全選、複製、剪下與貼上加入選單。\n" +
        "Add Select All, Copy, Cut and Paste to the menu.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)
    dependsOn(
        gboardPatchesSettingsPatch,
        gboardEditingAccessPointsFeatureMarkerPatch,
        gboardEditingAccessPointsLifecyclePatch,
        gboardAccessPointContributions1803Patch,
    )
}

@Suppress("unused")
val gboardSwipeDismissPatch = gboardPublicResourcePatch(
    featureId = "swipe_down_to_dismiss_keyboard",
    name = "Swipe Down to Dismiss Keyboard",
    description = "從建議列或工具列向下滑動以關閉鍵盤。\n" +
        "Swipe down from the suggestions or toolbar to close the keyboard.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardSwipeDismissFeatureMarkerPatch,
        gboardSwipeDismissBytecodePatch,
    )
}

@Suppress("unused")
val gboardAdvancedVoiceTypingPatch = gboardPublicResourcePatch(
    featureId = "advanced_voice_typing",
    name = "Advanced Voice Typing",
    description = "啟用進階語音輸入（包含自動標點功能）\n" +
        "Enable Advanced Voice Typing with automatic punctuation.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardAdvancedVoiceFeatureMarkerPatch,
    )
}

@Suppress("unused")
val gboardBluetoothMicrophonePatch = gboardPublicResourcePatch(
    featureId = "use_bluetooth_microphone",
    name = "Use Bluetooth Microphone",
    description = "啟用 語音輸入 -> 使用藍芽麥克風\n" +
        "Enable Voice typing -> Use Bluetooth microphone.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardBluetoothMicrophoneFeatureMarkerPatch,
        gboardBluetoothMicrophoneFlagValuePatch,
    )
}

@Suppress("unused")
val gboardCursorTrackpadPatch = gboardPublicResourcePatch(
    featureId = "enable_cursor_trackpad_mode",
    name = "Enable cursor trackpad mode",
    description = "長按空白鍵開啟游標觸控板與鎖定模式\n" +
        "Long-press the space bar to use cursor trackpad and lock modes.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardCursorTrackpadFeatureMarkerPatch,
        gboardCursorTrackpadFlagValuePatch,
    )
}

@Suppress("unused")
val gboardSplitKeyboardPatch = gboardPublicResourcePatch(
    featureId = "enable_split_keyboard",
    name = "Enable split keyboard",
    description = "啟用分離式鍵盤\nEnable the split keyboard layout.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardSplitKeyboardFeatureMarkerPatch,
        gboardSplitKeyboardAccessPointPatch,
        gboardSplitKeyboardModePatch,
    )
}

@Suppress("unused")
val gboardCloseProactiveSuggestionsPatch = gboardPublicResourcePatch(
    featureId = "close_proactive_suggestions",
    name = "Close Proactive Suggestions",
    description = "在主動建議列顯示關閉按鈕\n" +
        "Show a dismiss button in the proactive suggestions bar.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardCloseProactiveSuggestionsFeatureMarkerPatch,
        gboardCloseProactiveSuggestionsFlagValuePatch,
    )
}

@Suppress("unused")
val gboardBackupRestorePatch = gboardPublicResourcePatch(
    featureId = "backup_restore",
    name = "Backup & Restore",
    description = "匯出或還原全部 Patches 設定，並備份、比較及還原 Gboard PB/XML flag store\n" +
        "Export or restore all Patches settings, and back up, compare, or restore the Gboard PB/XML flag store.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardBackupRestoreFeatureMarkerPatch,
        gboardBackupRestoreManifestPatch,
    )
}

@Suppress("unused")
val gboardClipboardEnhancementsPatch = gboardPublicResourcePatch(
    featureId = "clipboard_enhancements",
    name = "Clipboard Enhancements",
    description = "增強剪貼簿的保留時間、數量上限、預覽行數、倒數/建立時間、順序編號與欄數\nEnhance clipboard retention time, item count limit, preview lines, countdown/creation time labels, order index, and column count.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardAboutPageResourcePatch,
        gboardPatchesSettingsPatch,
        gboardClipboardFeatureMarkerPatch,
        gboardClipboardLoaderPatch,
        gboardClipboardPrunePatch,
        gboardClipboardColumnCountPatch,
        gboardClipboardAdapterTrimPatch,
        gboardClipboardItemBindPatch,
    )
}

@Suppress("unused")
val gboardClipboardContentLimitPatch = gboardPublicResourcePatch(
    featureId = "clipboard_custom_character_limit",
    name = "Clipboard Custom Character Limit",
    description = "自訂每個文字剪貼簿項目的最大字元數\nCustomize the maximum character count for each text clipboard item.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardClipboardContentLimitFeatureMarkerPatch,
        gboardClipboardContentLimitFlagValuePatch,
    )
}

@Suppress("unused")
val gboardDeviceIntelligencePatch = gboardPublicResourcePatch(
    featureId = "enable_inline_autofill_suggestions",
    name = "Enable Inline Autofill Suggestions",
    description = "啟用內嵌自動填入建議\nEnable Inline Autofill Suggestions",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesExtensionCarrierPatch,
        gboardDeviceIntelligenceFlagValuePatch,
        gboardDeviceIntelligenceFeatureMarkerPatch,
    )
}

@Suppress("unused")
val gboardGrammarCheckerFlagPatch = gboardPublicResourcePatch(
    featureId = "grammar_checker",
    name = "Grammar Checker",
    description = "啟用 修正和建議 > 文法檢查\nEnable Text correction > Grammar check.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesExtensionCarrierPatch,
        gboardGrammarCheckerFlagValuePatch,
        gboardGrammarCheckerFeatureMarkerPatch,
    )
}

@Suppress("unused")
val gboardInlineSuggestionsFlagPatch = gboardPublicResourcePatch(
    featureId = "inline_suggestions",
    name = "Inline Suggestions",
    description = "啟用 修正和建議 > 智慧撰寫\nEnable Text correction > Smart Compose.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesExtensionCarrierPatch,
        gboardInlineSuggestionsFlagValuePatch,
        gboardInlineSuggestionsFeatureMarkerPatch,
    )
}

@Suppress("unused")
val gboardOcrScanTextPatch = gboardPublicResourcePatch(
    featureId = "enable_ocr_scan_text",
    name = "Enable OCR / Scan Text",
    description = "啟用 OCR / 掃描文字功能（支援拉丁文辨識後端，涵蓋英文與越南文）\n" +
        "Enable the OCR / Scan Text feature with the Latin recognition backend (supporting English and Vietnamese).",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardOcrFeatureMarkerPatch,
    )
}

@Suppress("unused")
val gboardLatinGlobeKeyIgnoreIntervalPatch = gboardPublicResourcePatch(
    featureId = "latin_globe_key_ignore_interval",
    name = "Latin Globe Key Ignore Interval",
    description = "新增英文鍵盤地球鍵忽略時間覆寫，可獨立控制輸入後切語言延遲\nAdd an independent English globe key ignore interval override for post-typing language-switch delay.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardPatchesSettingsPatch,
        gboardLatinGlobeKeyIgnoreIntervalFeatureMarkerPatch,
        gboardLatinGlobeKeyIgnoreIntervalBytecodePatch,
    )
}

@Suppress("unused")
val gboardPackageRenamePatch = gboardPublicResourcePatch(
    featureId = "package_rename",
    name = "Package Rename",
    description = "將套件名稱改成 dev.jason.com.google.android.inputmethod.latin，並可自訂 App 名稱，以便共存安裝\n" +
        "Rename the package to dev.jason.com.google.android.inputmethod.latin and allow " +
        "a custom app name so it can be installed alongside the official Gboard.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    val appDisplayName = stringOption(
        key = "app_display_name",
        default = "GboardWu",
        values = linkedMapOf(
            "Gboard" to "Gboard",
            "Gboard Dev" to "Gboard Dev",
            "Gboard Patches" to "Gboard Patches",
            "Gboard Mod" to "Gboard Mod",
            "GboardWu" to "GboardWu",
        ),
        title = "App 名稱 / App name",
        description = "",
        required = true,
    ) { value ->
        isValidGboardAppDisplayName(value)
    }

    dependsOn(
        gboardPackageRenameResourcePatch,
    )

    finalize {
        applyGboardApplicationDisplayName(displayName = checkNotNull(appDisplayName.value))
    }
}

@Suppress("unused")
val gboardSignatureBypassPatch = gboardPublicResourcePatch(
    featureId = "add_gboard_signature_bypass",
    name = "Add Gboard Signature Bypass",
    description = "攔截 Gboard 的簽章白名單檢查並強制通過\nBypass Gboard signature whitelist checks and force them to pass.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(
        gboardSignatureBypassBytecodePatch,
    )
}

object GboardPublishedPatchCatalog {
    val morpheRegistrations: Set<Patch<*>> = linkedSetOf(
        gboardEnglishQwertySlideUppercaseTogglePatch,
        gboardFlickUpWordCompletionPatch,
        gboardLongPressQuickActionsPatch,
        gboardManualIncognitoModePatch,
        gboardEditingAccessPointsPatch,
        gboardSwipeDismissPatch,
        gboardAdvancedVoiceTypingPatch,
        gboardBluetoothMicrophonePatch,
        gboardCursorTrackpadPatch,
        gboardSplitKeyboardPatch,
        gboardCloseProactiveSuggestionsPatch,
        gboardBackupRestorePatch,
        gboardClipboardEnhancementsPatch,
        gboardClipboardContentLimitPatch,
        gboardDeviceIntelligencePatch,
        gboardGrammarCheckerFlagPatch,
        gboardInlineSuggestionsFlagPatch,
        gboardOcrScanTextPatch,
        gboardLatinGlobeKeyIgnoreIntervalPatch,
        gboardPackageRenamePatch,
        gboardSignatureBypassPatch,
    ).filterTo(linkedSetOf()) { patch -> patch.name != null }

    @Suppress("DEPRECATION")
    fun publishedInventory(version: String): String {
        val publishedPatches = morpheRegistrations.sortedBy { patch -> patch.name }.map { patch ->
            PublishedPatch(
                name = patch.name!!,
                description = patch.description,
                use = patch.use,
                dependencies = patch.dependencies.mapNotNull { dependency -> dependency.name }.sorted(),
                compatiblePackages = patch.compatiblePackages?.associate { (packageName, versions) ->
                    packageName to versions
                },
                options = patch.options.values.map { option ->
                    PublishedPatch.Option(
                        key = option.key,
                        title = option.title,
                        description = option.description,
                        required = option.required,
                        type = option.type.toString(),
                        default = option.default,
                        values = option.values,
                    )
                },
            )
        }
        val gson = GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
        val inventory = JsonObject().apply {
            addProperty("version", version)
            add("patches", gson.toJsonTree(publishedPatches))
        }

        return gson.toJson(inventory) + "\n"
    }

    private class PublishedPatch(
        val name: String,
        val description: String?,
        val use: Boolean,
        val dependencies: List<String>,
        val compatiblePackages: Map<String, Set<String>?>?,
        val options: List<Option>,
    ) {
        class Option(
            val key: String,
            val title: String?,
            val description: String?,
            val required: Boolean,
            val type: String,
            val default: Any?,
            val values: Map<String, Any?>?,
        )
    }
}
