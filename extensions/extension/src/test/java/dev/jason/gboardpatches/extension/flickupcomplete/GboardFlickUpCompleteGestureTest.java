package dev.jason.gboardpatches.extension.flickupcomplete;

import org.junit.Assert;
import org.junit.Test;

public final class GboardFlickUpCompleteGestureTest {
    @Test
    public void onlyBodyTouchStartCanInitiateTracking() {
        Assert.assertTrue(GboardFlickUpCompleteGesture.isBodyStart(
                450f, 1800f, 0, 1750, 1220, 2500));
        Assert.assertFalse(GboardFlickUpCompleteGesture.isBodyStart(
                450f, 1700f, 0, 1750, 1220, 2500));
        Assert.assertFalse(GboardFlickUpCompleteGesture.isBodyStart(
                1300f, 1800f, 0, 1750, 1220, 2500));
    }

    @Test
    public void resolvesLeftCenterAndRightZonesFromStartingX() {
        GboardFlickUpCompleteGesture gesture = new GboardFlickUpCompleteGesture();

        // Left zone: 0 .. 299 (out of 900)
        gesture.begin(true, 0, 150f, 200f, 900, 1000L);
        Assert.assertEquals(0, gesture.move(0, 152f, 155f, 1f, 8f, 1080L, 300L));

        // Center zone: 300 .. 599 (out of 900)
        gesture.begin(true, 0, 450f, 200f, 900, 2000L);
        Assert.assertEquals(1, gesture.move(0, 452f, 155f, 1f, 8f, 2080L, 300L));

        // Right zone: 600 .. 900 (out of 900)
        gesture.begin(true, 0, 750f, 200f, 900, 3000L);
        Assert.assertEquals(2, gesture.move(0, 752f, 155f, 1f, 8f, 3080L, 300L));
    }

    @Test
    public void flickUpConsumesOncePastDistanceThreshold() {
        GboardFlickUpCompleteGesture gesture = new GboardFlickUpCompleteGesture();
        gesture.begin(true, 0, 120f, 200f, 900, 1000L);

        // 200 -> 165 is 35dp (< 40dp threshold)
        Assert.assertEquals(-1, gesture.move(0, 122f, 165f, 1f, 8f, 1050L, 300L));
        Assert.assertFalse(gesture.isConsumed());

        // 200 -> 158 is 42dp (>= 40dp threshold)
        Assert.assertEquals(0, gesture.move(0, 123f, 158f, 1f, 8f, 1080L, 300L));
        Assert.assertTrue(gesture.isConsumed());

        // Subsequent moves in the same gesture do not fire again
        Assert.assertEquals(-1, gesture.move(0, 123f, 130f, 1f, 8f, 1100L, 300L));
    }

    @Test
    public void downwardOrHorizontalMotionResetsTracking() {
        GboardFlickUpCompleteGesture gesture = new GboardFlickUpCompleteGesture();
        gesture.begin(true, 0, 450f, 200f, 900, 1000L);

        // Downward past touch slop
        Assert.assertEquals(-1, gesture.move(0, 450f, 215f, 1f, 8f, 1050L, 300L));
        Assert.assertFalse(gesture.isTracking());
        Assert.assertEquals(-1, gesture.move(0, 450f, 140f, 1f, 8f, 1100L, 300L));

        // Predominantly horizontal glide typing
        gesture.begin(true, 0, 450f, 200f, 900, 2000L);
        Assert.assertEquals(-1, gesture.move(0, 500f, 175f, 1f, 8f, 2050L, 300L));
        Assert.assertFalse(gesture.isTracking());
    }

    @Test
    public void longPressStationaryOrSlowDragCannotFlickComplete() {
        GboardFlickUpCompleteGesture gesture = new GboardFlickUpCompleteGesture();
        gesture.begin(true, 0, 450f, 200f, 900, 1000L);

        // Stationary past long press timeout (320ms >= 300ms)
        Assert.assertEquals(-1, gesture.move(0, 451f, 199f, 1f, 8f, 1320L, 300L));
        Assert.assertFalse(gesture.isTracking());

        // Slow drag exceeding 350ms max flick duration
        gesture.begin(true, 0, 450f, 200f, 900, 2000L);
        Assert.assertEquals(-1, gesture.move(0, 452f, 150f, 1f, 8f, 2360L, 300L));
        Assert.assertFalse(gesture.isTracking());
        Assert.assertFalse(gesture.isConsumed());
    }
}
