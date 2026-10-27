package dev.jason.gboardpatches.extension.flickupcomplete;

/**
 * BlackBerry-style upward flick gesture tracker on the keyboard body.
 * Divides the keyboard width into 3 horizontal zones:
 * 0 = Left [0, W/3), 1 = Center [W/3, 2W/3), 2 = Right [2W/3, W].
 */
public final class GboardFlickUpCompleteGesture {
    public static final int ZONE_NONE = -1;
    public static final int ZONE_LEFT = 0;
    public static final int ZONE_CENTER = 1;
    public static final int ZONE_RIGHT = 2;

    static final float DISTANCE_DP = 34f;
    static final float DIRECTION_RATIO = 1.25f;
    static final float SLOP_DP = 8f;
    static final long DEFAULT_LONG_PRESS_TIMEOUT_MS = 250L;
    static final long MAX_FLICK_DURATION_MS = 380L;

    private static final int STATE_IDLE = 0;
    private static final int STATE_TRACKING = 1;
    private static final int STATE_CONSUMING = 2;
    private static final int STATE_DISQUALIFIED = 3;

    private int state = STATE_IDLE;
    private int pointerId = -1;
    private int zone = ZONE_NONE;
    private float downX;
    private float downY;
    private long downTimeMs;
    private float distancePx;
    private float slopPx;
    private long longPressTimeoutMs = DEFAULT_LONG_PRESS_TIMEOUT_MS;

    public static int resolveZone(float rawX, float holderLeft, float holderRight) {
        float width = holderRight - holderLeft;
        if (width <= 0f) {
            return ZONE_NONE;
        }
        float relativeX = rawX - holderLeft;
        if (relativeX < 0f || relativeX > width) {
            return ZONE_NONE;
        }
        float third = width / 3f;
        if (relativeX < third) {
            return ZONE_LEFT;
        }
        if (relativeX < third * 2f) {
            return ZONE_CENTER;
        }
        return ZONE_RIGHT;
    }

    public void start(
            int pointerId,
            int zone,
            float rawX,
            float rawY,
            long downTimeMs,
            float density,
            int scaledTouchSlopPx,
            long longPressTimeoutMs) {
        if (zone < ZONE_LEFT || zone > ZONE_RIGHT) {
            reset();
            return;
        }
        float safeDensity = density > 0f ? density : 1f;
        this.state = STATE_TRACKING;
        this.pointerId = pointerId;
        this.zone = zone;
        this.downX = rawX;
        this.downY = rawY;
        this.downTimeMs = downTimeMs;
        this.distancePx = DISTANCE_DP * safeDensity;
        this.slopPx = Math.max(SLOP_DP * safeDensity, Math.max(0, scaledTouchSlopPx));
        this.longPressTimeoutMs = longPressTimeoutMs > 0L
                ? Math.min(longPressTimeoutMs, MAX_FLICK_DURATION_MS)
                : DEFAULT_LONG_PRESS_TIMEOUT_MS;
    }

    public boolean move(int pointerId, float rawX, float rawY, long eventTimeMs) {
        if (state == STATE_CONSUMING) {
            return false;
        }
        if (state != STATE_TRACKING || this.pointerId != pointerId) {
            return false;
        }
        long elapsedMs = eventTimeMs - downTimeMs;
        if (elapsedMs < 0L || elapsedMs > MAX_FLICK_DURATION_MS) {
            state = STATE_DISQUALIFIED;
            return false;
        }

        float dx = rawX - downX;
        float dy = rawY - downY;
        float absDx = Math.abs(dx);
        float upwardDy = -dy;

        if (dy > slopPx) {
            // Downward movement disqualifies upward flick.
            state = STATE_DISQUALIFIED;
            return false;
        }
        if (absDx > slopPx && absDx >= Math.max(upwardDy, slopPx)) {
            // Horizontal movement (glide typing or spacebar cursor trackpad) disqualifies flick.
            state = STATE_DISQUALIFIED;
            return false;
        }
        if (elapsedMs >= longPressTimeoutMs && upwardDy < distancePx) {
            // User held the key past long-press timeout without completing the upward flick.
            state = STATE_DISQUALIFIED;
            return false;
        }
        if (upwardDy >= distancePx && upwardDy >= absDx * DIRECTION_RATIO) {
            state = STATE_CONSUMING;
            return true;
        }
        return false;
    }

    public int getZone() {
        return zone;
    }

    public boolean isConsuming() {
        return state == STATE_CONSUMING;
    }

    public void disqualify() {
        if (state == STATE_TRACKING) {
            state = STATE_DISQUALIFIED;
        }
    }

    public void reset() {
        state = STATE_IDLE;
        pointerId = -1;
        zone = ZONE_NONE;
        downX = 0f;
        downY = 0f;
        downTimeMs = 0L;
        distancePx = 0f;
        slopPx = 0f;
        longPressTimeoutMs = DEFAULT_LONG_PRESS_TIMEOUT_MS;
    }
}
