package dev.jason.gboardpatches.extension.flickupcomplete;

import android.content.Context;
import android.content.SharedPreferences;

import dev.jason.gboardpatches.extension.settings.GboardPatchesSettings;

public final class GboardFlickUpCompleteSettings {
    public static final String PREF_KEY_ENABLED =
            "pref_flick_up_word_completion_enabled";
    public static final boolean DEFAULT_ENABLED = true;

    private static volatile Boolean enabledOverrideForTest;

    private GboardFlickUpCompleteSettings() {
    }

    public static boolean isEnabled(Context context) {
        Boolean override = enabledOverrideForTest;
        if (override != null) {
            return override.booleanValue();
        }
        if (context == null) {
            return DEFAULT_ENABLED;
        }
        return readEnabled(context);
    }

    public static boolean readEnabled(Context context) {
        return GboardPatchesSettings.readRuntimeSetting(
                context,
                GboardFlickUpCompleteSettings::readEnabled,
                DEFAULT_ENABLED);
    }

    public static boolean readEnabled(SharedPreferences preferences) {
        if (preferences == null) {
            return DEFAULT_ENABLED;
        }
        Object raw = preferences.getAll().get(PREF_KEY_ENABLED);
        if (raw instanceof Boolean value) {
            return value.booleanValue();
        }
        if (raw instanceof String value) {
            if ("true".equalsIgnoreCase(value)) {
                return true;
            }
            if ("false".equalsIgnoreCase(value)) {
                return false;
            }
        }
        return DEFAULT_ENABLED;
    }

    public static void ensureDefault(SharedPreferences preferences) {
        if (preferences != null && !preferences.contains(PREF_KEY_ENABLED)) {
            preferences.edit()
                    .putBoolean(PREF_KEY_ENABLED, DEFAULT_ENABLED)
                    .commit();
        }
    }

    public static boolean writeEnabled(Context context, boolean enabled) {
        if (context == null) {
            return false;
        }
        SharedPreferences preferences = GboardPatchesSettings.preferencesOrNull(context);
        if (preferences == null) {
            return false;
        }
        try {
            return writeEnabled(preferences, enabled);
        } catch (RuntimeException unavailable) {
            return false;
        }
    }

    public static boolean writeEnabled(SharedPreferences preferences, boolean enabled) {
        if (preferences == null) {
            return false;
        }
        return preferences.edit()
                .putBoolean(PREF_KEY_ENABLED, enabled)
                .commit();
    }

    public static void setEnabledOverrideForTest(Boolean enabled) {
        enabledOverrideForTest = enabled;
    }
}
