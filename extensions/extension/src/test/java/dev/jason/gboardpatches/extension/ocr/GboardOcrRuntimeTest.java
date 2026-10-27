package dev.jason.gboardpatches.extension.ocr;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public final class GboardOcrRuntimeTest {
    @After
    public void tearDown() {
        GboardOcrRuntime.resetForTests();
    }

    @Test
    public void entryGateForcesOnlyTheThreeOcrFlags() {
        Assert.assertEquals(Boolean.TRUE,
                GboardOcrRuntime.applyFlagValue("enable_ocr", Boolean.FALSE));
        Assert.assertEquals(Boolean.TRUE,
                GboardOcrRuntime.applyFlagValue("config_ocr", Boolean.FALSE));
        Assert.assertEquals("*",
                GboardOcrRuntime.applyFlagValue("enabled_ocr_language_tags", "en"));
        Object untouched = new Object();
        Assert.assertSame(untouched,
                GboardOcrRuntime.applyFlagValue("another_flag", untouched));
    }

    @Test
    public void latinBackendSelectsExactThinAndThickMetadata() {
        GboardOcrRuntime.setEngineOverrideForTests(GboardOcrEngine.LATIN);
        GboardOcrRuntime.setThickAvailabilityOverrideForTests(Boolean.FALSE);

        Assert.assertEquals(
                "taser_tflite_gocrlatin_mbv2_scriptid_aksara_layout_gcn_mobile",
                GboardOcrRuntime.applyConfigLabel("stock"));
        Assert.assertEquals("en", GboardOcrRuntime.applyLanguageHint("stock"));
        Assert.assertEquals("play-services-mlkit-text-recognition",
                GboardOcrRuntime.applyLoggingLibrary("stock"));
        Assert.assertEquals("ocr",
                GboardOcrRuntime.applyOptionalModuleName("stock"));
        Assert.assertEquals("com.google.android.gms.vision.ocr",
                GboardOcrRuntime.applyModuleId("stock"));
        Assert.assertFalse(GboardOcrRuntime.applyModuleAvailability(false));

        GboardOcrRuntime.setThickAvailabilityOverrideForTests(Boolean.TRUE);
        Assert.assertEquals("text-recognition",
                GboardOcrRuntime.applyLoggingLibrary("stock"));
        Assert.assertEquals("com.google.mlkit.dynamite.text.latin",
                GboardOcrRuntime.applyModuleId("stock"));
        Assert.assertTrue(GboardOcrRuntime.applyModuleAvailability(false));
    }

    @Test
    public void formalThickPathEmptyRequestStaysAuthoritative() {
        GboardOcrRuntime.setEngineOverrideForTests(GboardOcrEngine.LATIN);
        FakeRequest[] stock = new FakeRequest[0];

        Assert.assertSame(stock, GboardOcrRuntime.applyOptionalModuleRequest(stock));
    }

    @Test
    public void latinModuleRequestAndPayloadPolicyRemainStockCompatible() {
        GboardOcrRuntime.setEngineOverrideForTests(GboardOcrEngine.LATIN);
        FakeRequest[] stock = new FakeRequest[]{new FakeRequest("vision.ocr")};
        Assert.assertSame(stock, GboardOcrRuntime.applyOptionalModuleRequest(stock));
    }

    @Test
    public void moduleInstallRequestsCoverLatinBackend() {
        Assert.assertArrayEquals(new String[]{"vision.ocr"},
                GboardOcrRuntime.selectedOptionalModuleNames(GboardOcrEngine.LATIN));
    }

    private static final class FakeRequest {
        private final String a;

        private FakeRequest(String name) {
            a = name;
        }

        @SuppressWarnings("unused")
        private FakeRequest(String name, int legacyVersion, long version,
                boolean fullyRolledOut) {
            a = name;
        }
    }
}
