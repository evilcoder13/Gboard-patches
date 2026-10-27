package dev.jason.gboardpatches.extension.backuprestore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import dev.jason.gboardpatches.extension.keyboard.GboardLatinGlobeKeyIgnoreIntervalSettings;

@RunWith(RobolectricTestRunner.class)
public final class GboardPatchesBackupManagerTest {
    @Test
    public void roundTripPreservesEverySharedPreferencesTypeAndReplacesStores() {
        Map<String, SharedPreferences> stores = stores();
        SharedPreferences core = stores.get("gboard_patches_settings");
        core.edit()
                .putBoolean("boolean", true)
                .putInt("int", Integer.MIN_VALUE)
                .putLong("long", 9_007_199_254_740_993L)
                .putFloat("negative_zero", Float.intBitsToFloat(0x80000000))
                .putFloat("nan", Float.intBitsToFloat(0x7fc01234))
                .putString("string", "密碼\n\"token\"\\JavaScript")
                .putStringSet("set", Set.of("注音", "emoji"))
                .commit();

        String exported = GboardPatchesBackupManager.exportBackup(stores);
        core.edit().clear().putString("target_only", "remove me").commit();
        GboardPatchesBackupManager.RestoreResult result =
                GboardPatchesBackupManager.restoreBackup(stores, exported);

        assertTrue(result.isSuccess());
        assertEquals(GboardPatchesBackupManager.physicalStoreNames().size(),
                result.getRestoredStoreCount());
        assertEquals(7, result.getRestoredEntryCount());
        assertTrue(core.getBoolean("boolean", false));
        assertEquals(Integer.MIN_VALUE, core.getInt("int", 0));
        assertEquals(9_007_199_254_740_993L, core.getLong("long", 0L));
        assertEquals(0x80000000,
                Float.floatToRawIntBits(core.getFloat("negative_zero", 0f)));
        assertEquals(0x7fc01234, Float.floatToRawIntBits(core.getFloat("nan", 0f)));
        assertEquals("密碼\n\"token\"\\JavaScript", core.getString("string", null));
        assertEquals(Set.of("注音", "emoji"), core.getStringSet("set", Set.of()));
        assertFalse(core.contains("target_only"));
    }

    @Test
    public void modulesCanBeRestoredSelectively() {
        Map<String, SharedPreferences> stores = stores();
        SharedPreferences ocr = stores.get("gboard_ocr_settings");
        SharedPreferences trackpad = stores.get("gboard_cursor_trackpad_settings");
        ocr.edit().putString("pref_ocr_language", "latin").commit();
        trackpad.edit()
                .putBoolean("pref_cursor_trackpad_enabled", true)
                .putInt("pref_cursor_trackpad_sensitivity", 3)
                .commit();
        String exported = GboardPatchesBackupManager.exportBackup(stores);

        ocr.edit().clear().putString("current", "replace").commit();
        trackpad.edit().clear().putString("current", "replace").commit();
        SharedPreferences core = stores.get("gboard_patches_settings");
        core.edit().putString("current", "leave alone").commit();
        GboardPatchesBackupManager.RestoreResult result =
                GboardPatchesBackupManager.restoreBackup(stores, exported, Set.of(
                        "gboard-patches.ocr",
                        "gboard-patches.cursor-trackpad"));

        assertTrue(result.isSuccess());
        assertEquals("latin", ocr.getString("pref_ocr_language", null));
        assertTrue(trackpad.getBoolean("pref_cursor_trackpad_enabled", false));
        assertEquals(3, trackpad.getInt("pref_cursor_trackpad_sensitivity", 0));
        assertEquals(Map.of("current", "leave alone"), core.getAll());
    }

    @Test
    public void restorePreviewReportsAddedChangedRemovedAndUnchangedKeys() {
        Map<String, SharedPreferences> stores = stores();
        SharedPreferences core = stores.get("gboard_patches_settings");
        core.edit()
                .putString("added", "from backup")
                .putString("changed", "backup value")
                .putString("same", "same value")
                .commit();
        String exported = GboardPatchesBackupManager.exportBackup(stores);
        core.edit().clear()
                .putString("changed", "current value")
                .putString("removed", "current only")
                .putString("same", "same value")
                .commit();

        GboardPatchesBackupManager.RestorePreview preview =
                GboardPatchesBackupManager.inspectRestore(stores, exported,
                        Set.of("gboard-patches.core"));
        Map<String, GboardPatchesBackupManager.ChangeStatus> changes = new HashMap<>();
        for (GboardPatchesBackupManager.KeyChange key : preview.getModules().get(0).getKeys()) {
            changes.put(key.getKey(), key.getStatus());
        }

        assertEquals(1, preview.getModules().size());
        assertEquals(GboardPatchesBackupManager.ChangeStatus.ADDED, changes.get("added"));
        assertEquals(GboardPatchesBackupManager.ChangeStatus.CHANGED, changes.get("changed"));
        assertEquals(GboardPatchesBackupManager.ChangeStatus.REMOVED, changes.get("removed"));
        assertEquals(GboardPatchesBackupManager.ChangeStatus.UNCHANGED, changes.get("same"));
        assertEquals(1, preview.getAddedCount());
        assertEquals(1, preview.getChangedCount());
        assertEquals(1, preview.getRemovedCount());
        assertEquals(1, preview.getUnchangedCount());
    }

    @Test
    public void failedCommitRestoresOnlyFailedModuleAndContinues() {
        Map<String, SharedPreferences> stores = stores();
        for (SharedPreferences preferences : stores.values()) {
            preferences.edit().putString("backup", "restore me").commit();
        }
        String exported = GboardPatchesBackupManager.exportBackup(stores);
        for (SharedPreferences preferences : stores.values()) {
            preferences.edit().clear().putString("current", "keep on failure").commit();
        }
        String failingName = GboardPatchesBackupManager.physicalStoreNames().get(2);
        ((MemoryPreferences) stores.get(failingName)).failNextCommit = true;

        GboardPatchesBackupManager.RestoreResult result =
                GboardPatchesBackupManager.restoreBackup(stores, exported);

        assertFalse(result.isSuccess());
        assertTrue(result.isPartialSuccess());
        assertEquals(6, result.getRestoredStoreCount());
        assertEquals(1, result.getFailedModuleCount());
        for (Map.Entry<String, SharedPreferences> entry : stores.entrySet()) {
            assertEquals(entry.getKey().equals(failingName)
                            ? Map.of("current", "keep on failure")
                            : Map.of("backup", "restore me"),
                    entry.getValue().getAll());
        }
        assertEquals(GboardPatchesBackupManager.ModuleStatus.PREVIOUS_VALUES_RESTORED,
                result.getModules().get(2).getStatus());
    }

    @Test
    public void latinGlobeStringBackupRestoresNativeTypesAndExportsPortableTypes()
            throws Exception {
        Map<String, SharedPreferences> stores = stores();
        SharedPreferences latin = stores.get(
                GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_FILE);
        latin.edit()
                .putBoolean(GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_KEY_ENABLED, true)
                .putInt(GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_KEY_INTERVAL_MS, 0)
                .commit();
        JSONObject backup = new JSONObject(GboardPatchesBackupManager.exportBackup(stores));
        JSONObject entries = entriesByModuleId(backup).get("gboard-patches.latin-globe");

        assertEquals("boolean", entries
                .getJSONObject(GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_KEY_ENABLED)
                .getString("type"));
        assertEquals("int", entries
                .getJSONObject(GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_KEY_INTERVAL_MS)
                .getString("type"));
        replaceWithString(entries,
                GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_KEY_ENABLED, "true");
        replaceWithString(entries,
                GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_KEY_INTERVAL_MS, "0");
        latin.edit().clear().commit();

        String stringBackup = backup.toString();
        GboardPatchesBackupManager.RestoreResult result =
                GboardPatchesBackupManager.restoreBackup(stores, stringBackup,
                        Set.of("gboard-patches.latin-globe"));
        GboardPatchesBackupManager.RestorePreview preview =
                GboardPatchesBackupManager.inspectRestore(stores, stringBackup,
                        Set.of("gboard-patches.latin-globe"));

        assertTrue(result.isSuccess());
        assertTrue(latin.getAll().get(
                GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_KEY_ENABLED) instanceof Boolean);
        assertTrue(latin.getAll().get(
                GboardLatinGlobeKeyIgnoreIntervalSettings.PREF_KEY_INTERVAL_MS)
                instanceof Integer);
        assertEquals(0, preview.getDifferenceCount());
        assertEquals(2, preview.getUnchangedCount());
    }

    @Test
    public void moduleCatalogUsesStableIdsAndEnglishDisplayNames() {
        GboardPatchesBackupManager.BackupPlan plan =
                GboardPatchesBackupManager.inspectBackup(
                        GboardPatchesBackupManager.exportBackup(stores()));
        Set<String> ids = new HashSet<>();

        assertEquals(7, plan.getModules().size());
        for (GboardPatchesBackupManager.ModulePlan module : plan.getModules()) {
            assertTrue(ids.add(module.getModuleId()));
            assertTrue(module.getModuleName().chars().allMatch(character -> character < 128));
        }
    }

    private static Map<String, JSONObject> entriesByModuleId(JSONObject backup)
            throws Exception {
        JSONArray modules = backup.getJSONObject("sections")
                .getJSONObject("gboardPatches").getJSONArray("modules");
        Map<String, JSONObject> entries = new LinkedHashMap<>();
        for (int index = 0; index < modules.length(); index++) {
            JSONObject module = modules.getJSONObject(index);
            entries.put(module.getString("moduleId"), module.getJSONObject("entries"));
        }
        return entries;
    }

    private static void replaceWithString(JSONObject entries, String key, String value)
            throws Exception {
        entries.put(key, new JSONObject().put("type", "string").put("value", value));
    }

    private static Map<String, SharedPreferences> stores() {
        Map<String, SharedPreferences> stores = new LinkedHashMap<>();
        for (String name : GboardPatchesBackupManager.physicalStoreNames()) {
            stores.put(name, new MemoryPreferences());
        }
        return stores;
    }

    private static final class MemoryPreferences implements SharedPreferences {
        private final Map<String, Object> values = new HashMap<>();
        private boolean failNextCommit;

        @Override
        public Map<String, ?> getAll() {
            return new HashMap<>(values);
        }

        @Override
        public String getString(String key, String defaultValue) {
            Object value = values.get(key);
            return value instanceof String stringValue ? stringValue : defaultValue;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<String> getStringSet(String key, Set<String> defaultValues) {
            Object value = values.get(key);
            return value instanceof Set<?> ? new HashSet<>((Set<String>) value) : defaultValues;
        }

        @Override
        public int getInt(String key, int defaultValue) {
            Object value = values.get(key);
            return value instanceof Integer intValue ? intValue : defaultValue;
        }

        @Override
        public long getLong(String key, long defaultValue) {
            Object value = values.get(key);
            return value instanceof Long longValue ? longValue : defaultValue;
        }

        @Override
        public float getFloat(String key, float defaultValue) {
            Object value = values.get(key);
            return value instanceof Float floatValue ? floatValue : defaultValue;
        }

        @Override
        public boolean getBoolean(String key, boolean defaultValue) {
            Object value = values.get(key);
            return value instanceof Boolean booleanValue ? booleanValue : defaultValue;
        }

        @Override
        public boolean contains(String key) {
            return values.containsKey(key);
        }

        @Override
        public Editor edit() {
            return new MemoryEditor();
        }

        @Override
        public void registerOnSharedPreferenceChangeListener(
                OnSharedPreferenceChangeListener listener) {
        }

        @Override
        public void unregisterOnSharedPreferenceChangeListener(
                OnSharedPreferenceChangeListener listener) {
        }

        private final class MemoryEditor implements Editor {
            private final Map<String, Object> pending = new HashMap<>();
            private final Set<String> removals = new HashSet<>();
            private boolean clear;

            @Override
            public Editor putString(String key, String value) {
                pending.put(key, value);
                return this;
            }

            @Override
            public Editor putStringSet(String key, Set<String> value) {
                pending.put(key, new HashSet<>(value));
                return this;
            }

            @Override
            public Editor putInt(String key, int value) {
                pending.put(key, value);
                return this;
            }

            @Override
            public Editor putLong(String key, long value) {
                pending.put(key, value);
                return this;
            }

            @Override
            public Editor putFloat(String key, float value) {
                pending.put(key, value);
                return this;
            }

            @Override
            public Editor putBoolean(String key, boolean value) {
                pending.put(key, value);
                return this;
            }

            @Override
            public Editor remove(String key) {
                removals.add(key);
                return this;
            }

            @Override
            public Editor clear() {
                clear = true;
                return this;
            }

            @Override
            public boolean commit() {
                if (failNextCommit) {
                    failNextCommit = false;
                    return false;
                }
                applyChanges();
                return true;
            }

            @Override
            public void apply() {
                applyChanges();
            }

            private void applyChanges() {
                if (clear) {
                    values.clear();
                }
                for (String key : removals) {
                    values.remove(key);
                }
                values.putAll(pending);
            }
        }
    }
}
