package dev.jason.gboardpatches.patches.gboard.shared.runtimeabi

internal enum class RuntimeCallId(internal val encodedAbi: String) {
    CLIPBOARD_RUNTIME_ABORT_ITEM_BIND("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->abortItemBind()V"),
    CLIPBOARD_RUNTIME_RESOLVE_COLUMN_COUNT("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->resolveColumnCount(Ljava/lang/Object;I)I"),
    CLIPBOARD_RUNTIME_AFTER_LAYOUT("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->afterLayout(Ljava/lang/Object;)V"),
    ADVANCED_VOICE_RUNTIME_SHOULD_CHECK_NATIVE("Ldev/jason/gboardpatches/extension/advancedvoice/GboardAdvancedVoiceRuntime;->shouldCheckNative(Z)Z"),
    CURSOR_TRACKPAD_RUNTIME_APPLY_BOOLEAN_PREFERENCE("Ldev/jason/gboardpatches/extension/cursortrackpad/GboardCursorTrackpadRuntime;->applyBooleanPreference(Ljava/lang/String;Z)Z"),
    CURSOR_TRACKPAD_RUNTIME_APPLY_RESOURCE_BOOLEAN_PREFERENCE("Ldev/jason/gboardpatches/extension/cursortrackpad/GboardCursorTrackpadRuntime;->applyResourceBooleanPreference(IZ)Z"),
    EDITING_ACCESS_POINTS_START("Ldev/jason/gboardpatches/extension/editingaccesspoints/GboardEditingAccessPointsRuntime;->start(Ljava/lang/Object;)V"),
    EDITING_ACCESS_POINTS_END("Ldev/jason/gboardpatches/extension/editingaccesspoints/GboardEditingAccessPointsRuntime;->end()V"),
    ACCESS_POINT_CONTRIBUTIONS_AFTER_CONTROLLER_CREATED("Ldev/jason/gboardpatches/extension/accesspoint/GboardAccessPointContributionsRuntime;->afterControllerCreated(Ljava/lang/Object;Landroid/content/Context;)V"),
    ACCESS_POINT_CONTRIBUTIONS_INCLUDE_ORDER_CATALOG("Ldev/jason/gboardpatches/extension/accesspoint/GboardAccessPointContributionsRuntime;->includeOrderCatalog(Landroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;"),
    ADVANCED_VOICE_RUNTIME_AFTER_FLAG_VALUE("Ldev/jason/gboardpatches/extension/advancedvoice/GboardAdvancedVoiceRuntime;->afterFlagValue(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
    ADVANCED_VOICE_RUNTIME_SEED_APPLICATION_CONTEXT("Ldev/jason/gboardpatches/extension/advancedvoice/GboardAdvancedVoiceRuntime;->seedApplicationContext(Landroid/content/Context;)V"),
    BLUETOOTH_MICROPHONE_RUNTIME_APPLY_OVERRIDDEN_FLAG_VALUE("Ldev/jason/gboardpatches/extension/bluetoothmicrophone/GboardBluetoothMicrophoneRuntime;->applyOverriddenFlagValue(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
    CLIPBOARD_CONTENT_LIMIT_RUNTIME_APPLY_OVERRIDDEN_FLAG_VALUE("Ldev/jason/gboardpatches/extension/clipboardcontentlimit/GboardClipboardContentLimitRuntime;->applyOverriddenFlagValue(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"),
    CLOSE_PROACTIVE_SUGGESTIONS_RUNTIME_APPLY_FLAG_VALUE("Ldev/jason/gboardpatches/extension/closeproactivesuggestions/GboardCloseProactiveSuggestionsRuntime;->applyFlagValue(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"),
    CLIPBOARD_RUNTIME_AFTER_ADAPTER_TRIM("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->afterAdapterTrim(Ljava/lang/Object;)V"),
    CLIPBOARD_RUNTIME_AFTER_ITEM_BIND("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->afterItemBind(Ljava/lang/Object;Ljava/lang/Object;I)V"),
    CLIPBOARD_RUNTIME_BEFORE_ITEM_BIND("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->beforeItemBind(Ljava/lang/Object;Ljava/lang/Object;I)V"),
    CLIPBOARD_RUNTIME_HANDLE_CUSTOM_PRUNE("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->handleCustomPrune(Ljava/lang/Object;)Z"),
    CLIPBOARD_RUNTIME_MAYBE_BUILD_LOADER_RESULT("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->maybeBuildLoaderResult(Ljava/lang/Object;)Ljava/lang/Object;"),
    CLIPBOARD_RUNTIME_RESOLVE_COLUMN_COUNT_OVERRIDE("Ldev/jason/gboardpatches/extension/clipboard/GboardClipboardRuntime;->resolveColumnCountOverride(Ljava/lang/Object;)Ljava/lang/Integer;"),
    CURSOR_TRACKPAD_RUNTIME_APPLY_OVERRIDDEN_FLAG_VALUE("Ldev/jason/gboardpatches/extension/cursortrackpad/GboardCursorTrackpadRuntime;->applyOverriddenFlagValue(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"),
    DEVICE_INTELLIGENCE_RUNTIME_APPLY_FLAG_VALUE("Ldev/jason/gboardpatches/extension/featureflags/GboardFeatureFlagsRuntime;->applyDeviceIntelligenceFlagValue(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"),
    GRAMMAR_CHECKER_RUNTIME_APPLY_FLAG_VALUE("Ldev/jason/gboardpatches/extension/featureflags/GboardFeatureFlagsRuntime;->applyGrammarCheckerFlagValue(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"),
    INLINE_SUGGESTIONS_RUNTIME_APPLY_FLAG_VALUE("Ldev/jason/gboardpatches/extension/featureflags/GboardFeatureFlagsRuntime;->applyInlineSuggestionsFlagValue(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"),
    ENGLISH_UPPERCASE_TOGGLE_RUNTIME_CACHE_PATCHED_METADATA("Ldev/jason/gboardpatches/extension/keyboard/GboardEnglishUppercaseToggleRuntime;->cachePatchedMetadata(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
    ENGLISH_UPPERCASE_TOGGLE_RUNTIME_GET_CACHED_PATCHED_METADATA("Ldev/jason/gboardpatches/extension/keyboard/GboardEnglishUppercaseToggleRuntime;->getCachedPatchedMetadata(Ljava/lang/Object;)Ljava/lang/Object;"),
    ENGLISH_UPPERCASE_TOGGLE_RUNTIME_IS_ENABLED("Ldev/jason/gboardpatches/extension/keyboard/GboardEnglishUppercaseToggleRuntime;->isEnabled()Z"),
    ENGLISH_UPPERCASE_TOGGLE_RUNTIME_IS_PATCHED_METADATA("Ldev/jason/gboardpatches/extension/keyboard/GboardEnglishUppercaseToggleRuntime;->isPatchedMetadata(Ljava/lang/Object;)Z"),
    FLICK_UP_COMPLETE_RUNTIME_ON_BODY_TOUCH("Ldev/jason/gboardpatches/extension/flickupcomplete/GboardFlickUpCompleteRuntime;->onBodyTouch(Landroid/view/View;Landroid/view/MotionEvent;)Z"),
    LATIN_GLOBE_KEY_IGNORE_INTERVAL_RUNTIME_APPLY_OVERRIDE("Ldev/jason/gboardpatches/extension/keyboard/GboardLatinGlobeKeyIgnoreIntervalRuntime;->applyOverride(Ljava/lang/Object;)Ljava/lang/Object;"),
    LONG_PRESS_QUICK_ACTIONS_RUNTIME_MAYBE_ENSURE_LONG_PRESS_SCHEDULED("Ldev/jason/gboardpatches/extension/longpressquickactions/GboardLongPressQuickActionsRuntime;->maybeEnsureLongPressScheduled(Ljava/lang/Object;Landroid/view/View;)V"),
    LONG_PRESS_QUICK_ACTIONS_RUNTIME_MAYBE_HANDLE_GLOBE_GESTURE("Ldev/jason/gboardpatches/extension/longpressquickactions/GboardGlobeDragRuntime;->maybeHandleGestureDispatch(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z"),
    LONG_PRESS_QUICK_ACTIONS_RUNTIME_MAYBE_HANDLE_INPUT_EVENT("Ldev/jason/gboardpatches/extension/longpressquickactions/GboardLongPressQuickActionsRuntime;->maybeHandleInputEvent(Landroid/inputmethodservice/InputMethodService;Ljava/lang/Object;)Z"),
    LONG_PRESS_QUICK_ACTIONS_RUNTIME_MAYBE_PATCH_METADATA("Ldev/jason/gboardpatches/extension/longpressquickactions/GboardLongPressQuickActionsRuntime;->maybePatchMetadata(Ljava/lang/Object;Landroid/view/View;)Ljava/lang/Object;"),
    LONG_PRESS_QUICK_ACTIONS_RUNTIME_ON_GLOBE_POINTER_CANCEL("Ldev/jason/gboardpatches/extension/longpressquickactions/GboardGlobeDragRuntime;->onPointerCancel(Ljava/lang/Object;)V"),
    LONG_PRESS_QUICK_ACTIONS_RUNTIME_ON_GLOBE_POINTER_FINISH("Ldev/jason/gboardpatches/extension/longpressquickactions/GboardGlobeDragRuntime;->onPointerFinish(Ljava/lang/Object;)V"),
    MANUAL_INCOGNITO_RUNTIME_APPLY_CLIPBOARD_POLICY("Ldev/jason/gboardpatches/extension/manualincognito/GboardManualIncognitoRuntime;->applyClipboardIncognitoPolicy(Z)Z"),
    MANUAL_INCOGNITO_RUNTIME_APPLY_DICTATION_FLAG("Ldev/jason/gboardpatches/extension/manualincognito/GboardManualIncognitoRuntime;->applyDictationIncognitoFlag(Z)Z"),
    MANUAL_INCOGNITO_RUNTIME_APPLY_INCOGNITO_PREDICATE("Ldev/jason/gboardpatches/extension/manualincognito/GboardManualIncognitoRuntime;->applyIncognitoPredicate(Z)Z"),
    MANUAL_INCOGNITO_RUNTIME_APPLY_VOICE_POLICY("Ldev/jason/gboardpatches/extension/manualincognito/GboardManualIncognitoRuntime;->applyVoiceIncognitoPolicy(Z)Z"),
    MANUAL_INCOGNITO_RUNTIME_ON_INPUT_STARTING("Ldev/jason/gboardpatches/extension/manualincognito/GboardManualIncognitoRuntime;->onInputStarting(Ljava/lang/Object;Landroid/view/inputmethod/EditorInfo;)V"),
    MANUAL_INCOGNITO_RUNTIME_ON_INPUT_VIEW_STARTING("Ldev/jason/gboardpatches/extension/manualincognito/GboardManualIncognitoRuntime;->onInputViewStarting(Ljava/lang/Object;Landroid/view/inputmethod/EditorInfo;)V"),
    MANUAL_INCOGNITO_RUNTIME_ON_INPUT_WINDOW_HIDDEN("Ldev/jason/gboardpatches/extension/manualincognito/GboardManualIncognitoRuntime;->onInputWindowHidden(Ljava/lang/Object;)V"),
    OCR_RUNTIME_AFTER_THIN_INIT("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->afterThinInit(Ljava/lang/Object;)V"),
    OCR_RUNTIME_APPLY_CONFIG_LABEL("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->applyConfigLabel(Ljava/lang/String;)Ljava/lang/String;"),
    OCR_RUNTIME_APPLY_FLAG_VALUE("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->applyFlagValue(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"),
    OCR_RUNTIME_APPLY_LANGUAGE_HINT("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->applyLanguageHint(Ljava/lang/String;)Ljava/lang/String;"),
    OCR_RUNTIME_APPLY_LOGGING_LIBRARY("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->applyLoggingLibrary(Ljava/lang/String;)Ljava/lang/String;"),
    OCR_RUNTIME_APPLY_MODULE_AVAILABILITY("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->applyModuleAvailability(Z)Z"),
    OCR_RUNTIME_APPLY_MODULE_ID("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->applyModuleId(Ljava/lang/String;)Ljava/lang/String;"),
    OCR_RUNTIME_APPLY_OPTIONAL_MODULE_NAME("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->applyOptionalModuleName(Ljava/lang/String;)Ljava/lang/String;"),
    OCR_RUNTIME_APPLY_OPTIONAL_MODULE_REQUEST("Ldev/jason/gboardpatches/extension/ocr/GboardOcrRuntime;->applyOptionalModuleRequest(Ljava/lang/Object;)Ljava/lang/Object;"),
    RAMBLER_RUNTIME_ENTER_DEFAULT_SELECTION_SUPPRESSION("Ldev/jason/gboardpatches/extension/rambler/GboardRamblerOfficialSelectionRuntime;->enterDefaultSelectionSuppression()V"),
    RAMBLER_RUNTIME_ENTER_VOICE_SETTINGS_SCOPE("Ldev/jason/gboardpatches/extension/rambler/GboardRamblerOfficialSelectionRuntime;->enterVoiceSettingsScope()V"),
    RAMBLER_RUNTIME_EXIT_DEFAULT_SELECTION_SUPPRESSION("Ldev/jason/gboardpatches/extension/rambler/GboardRamblerOfficialSelectionRuntime;->exitDefaultSelectionSuppression()V"),
    RAMBLER_RUNTIME_EXIT_VOICE_SETTINGS_SCOPE("Ldev/jason/gboardpatches/extension/rambler/GboardRamblerOfficialSelectionRuntime;->exitVoiceSettingsScope()V"),
    RAMBLER_RUNTIME_UPDATE_OFFICIAL_SELECTION("Ldev/jason/gboardpatches/extension/rambler/GboardRamblerOfficialSelectionRuntime;->updateOfficialSelection(Z)V"),
    SPLIT_KEYBOARD_RUNTIME_APPLY_ACCESS_POINT_STATE("Ldev/jason/gboardpatches/extension/splitkeyboard/GboardSplitKeyboardRuntime;->applyAccessPointState(Ljava/lang/String;I)I"),
    SPLIT_KEYBOARD_RUNTIME_AFTER_MODE_ENVIRONMENT_CHANGED("Ldev/jason/gboardpatches/extension/splitkeyboard/GboardSplitKeyboardRuntime;->afterModeEnvironmentChanged(Ljava/lang/Object;)V"),
    SPLIT_KEYBOARD_RUNTIME_INCLUDE_ACCESS_POINT_IN_ORDER("Ldev/jason/gboardpatches/extension/splitkeyboard/GboardSplitKeyboardRuntime;->includeAccessPointInOrder(Ljava/lang/Object;)Ljava/lang/Object;"),
    SPLIT_KEYBOARD_RUNTIME_INTERCEPT_SPLIT_ACTION("Ldev/jason/gboardpatches/extension/splitkeyboard/GboardSplitKeyboardRuntime;->interceptSplitAction(Ljava/lang/Object;)Z"),
    SWIPE_DISMISS_RUNTIME_ON_HEADER_TOUCH("Ldev/jason/gboardpatches/extension/swipedismiss/GboardSwipeDismissRuntime;->onHeaderTouch(Landroid/view/View;Landroid/view/MotionEvent;)Z"),
}

internal data class RuntimeAbi(
    val owner: String,
    val name: String,
    val parameters: List<String>,
    val returnType: String,
    val isStatic: Boolean,
) {
    val descriptor: String
        get() = parameters.joinToString(prefix = "(", postfix = ")$returnType", separator = "")

    val reference: String
        get() = "$owner->$name$descriptor"

    companion object {
        fun decode(encoded: String): RuntimeAbi {
            val owner = encoded.substringBefore("->")
            val method = encoded.substringAfter("->")
            val name = method.substringBefore('(')
            val parametersDescriptor = method.substringAfter('(').substringBefore(')')
            return RuntimeAbi(
                owner = owner,
                name = name,
                parameters = decodeTypes(parametersDescriptor),
                returnType = method.substringAfter(')'),
                isStatic = true,
            )
        }

        private fun decodeTypes(descriptor: String): List<String> = buildList {
            var index = 0
            while (index < descriptor.length) {
                val start = index
                while (descriptor[index] == '[') index++
                if (descriptor[index] == 'L') {
                    index = descriptor.indexOf(';', index) + 1
                    require(index > 0) { "Malformed object type in runtime ABI: $descriptor" }
                } else {
                    index++
                }
                add(descriptor.substring(start, index))
            }
        }
    }
}

internal object RuntimeAbiCatalog {
    private val abiByCall = RuntimeCallId.entries.associateWith { call ->
        RuntimeAbi.decode(call.encodedAbi)
    }

    fun abi(call: RuntimeCallId): RuntimeAbi = abiByCall.getValue(call)
}
