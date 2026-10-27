package dev.jason.gboardpatches.extension.flickupcomplete;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Collections;

import dev.jason.gboardpatches.extension.R;
import dev.jason.gboardpatches.extension.settings.GboardPatchesFeatureAvailability;
import dev.jason.gboardpatches.extension.settings.GboardPatchesSettings;
import dev.jason.gboardpatches.extension.settings.GboardPatchesSettingsContract;
import dev.jason.gboardpatches.extension.settings.GboardSettingsText;

public final class GboardFlickUpCompleteSettingsFeature
        implements GboardPatchesSettingsContract.Feature {
    private static final String TAG = "GboardPatches";

    private final String entryTitle;
    private final String entrySummary;
    private final String headerBadge;
    private final String errorTitle;
    private final String errorSummary;
    private final String sectionTitle;
    private final String enabledTitle;
    private final String enabledSummary;

    public GboardFlickUpCompleteSettingsFeature(Context context) {
        this(
                GboardSettingsText.get(context,
                        R.string.gboard_patches_flick_up_completion_title),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_flick_up_completion_summary),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_header_badge),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_error_header_title),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_error_header_summary),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_flick_up_completion_section),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_flick_up_completion_enabled_title),
                GboardSettingsText.get(context,
                        R.string.gboard_patches_flick_up_completion_enabled_summary));
    }

    GboardFlickUpCompleteSettingsFeature(
            String entryTitle,
            String entrySummary,
            String headerBadge,
            String errorTitle,
            String errorSummary,
            String sectionTitle,
            String enabledTitle,
            String enabledSummary) {
        this.entryTitle = entryTitle;
        this.entrySummary = entrySummary;
        this.headerBadge = headerBadge;
        this.errorTitle = errorTitle;
        this.errorSummary = errorSummary;
        this.sectionTitle = sectionTitle;
        this.enabledTitle = enabledTitle;
        this.enabledSummary = enabledSummary;
    }

    @Override
    public String getEntryTitle() {
        return entryTitle;
    }

    @Override
    public String getEntrySummary() {
        return entrySummary;
    }

    @Override
    public boolean isAvailable(Context context) {
        return GboardPatchesFeatureAvailability.hasFeature(
                context,
                GboardPatchesFeatureAvailability.FEATURE_FLICK_UP_WORD_COMPLETION);
    }

    @Override
    public GboardPatchesSettingsContract.Screen buildScreen(
            GboardPatchesSettingsContract.FeatureHost host) {
        try {
            if (host == null || host.getContext() == null) {
                return buildErrorScreen();
            }
            Context context = host.getContext();
            boolean enabled = GboardFlickUpCompleteSettings.readEnabled(context);
            return buildScreenForState(
                    enabled,
                    value -> GboardFlickUpCompleteSettings.writeEnabled(context, value));
        } catch (Throwable throwable) {
            Log.w(TAG, "Failed to render flick-up word completion settings", throwable);
            return buildErrorScreen();
        }
    }

    GboardPatchesSettingsContract.Screen buildScreenForState(
            boolean enabled,
            GboardPatchesSettingsContract.ToggleAction toggleAction) {
        return new GboardPatchesSettingsContract.Screen(
                entryTitle,
                headerBadge,
                entryTitle,
                "",
                Collections.emptyList(),
                Collections.singletonList(new GboardPatchesSettingsContract.Section(
                        sectionTitle,
                        Collections.singletonList(new GboardPatchesSettingsContract.ToggleRow(
                                enabledTitle,
                                enabledSummary,
                                true,
                                enabled,
                                toggleAction,
                                null)))),
                GboardPatchesSettingsContract.RefreshPolicy.none(),
                GboardPatchesSettingsContract.PanelStyle.FLAT);
    }

    private GboardPatchesSettingsContract.Screen buildErrorScreen() {
        return new GboardPatchesSettingsContract.Screen(
                entryTitle,
                headerBadge,
                entryTitle,
                "",
                Collections.singletonList(new GboardPatchesSettingsContract.StatusBlock(
                        errorTitle,
                        errorSummary,
                        GboardPatchesSettingsContract.StatusTone.WARNING)),
                Collections.emptyList());
    }
}
