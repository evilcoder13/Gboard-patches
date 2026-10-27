package dev.jason.gboardpatches.extension.flickupcomplete;

import android.content.Context;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

import dev.jason.gboardpatches.extension.settings.GboardPatchesSettings;

public final class GboardFlickUpCompleteRuntime {
    private static final String TAG = "GboardPatches";
    private static final String HEADER_AREA_TAG = ".keyboard-header-area";
    private static final String BODY_AREA_TAG = ".keyboard-body-area";
    private static final String FIXED_CANDIDATES_ENTRY_NAME = "softkey_holder_fixed_candidates";

    private static final WeakHashMap<View, GboardFlickUpCompleteGesture> GESTURES =
            new WeakHashMap<View, GboardFlickUpCompleteGesture>();
    private static final ThreadLocal<Boolean> CANCELLING = new ThreadLocal<Boolean>();
    private static final ThreadLocal<Boolean> DISPATCHING_CANDIDATE = new ThreadLocal<Boolean>();

    private GboardFlickUpCompleteRuntime() {
    }

    public static boolean onBodyTouch(View holder, MotionEvent event) {
        if (holder == null || event == null
                || Boolean.TRUE.equals(CANCELLING.get())
                || Boolean.TRUE.equals(DISPATCHING_CANDIDATE.get())) {
            return false;
        }
        try {
            Context context = holder.getContext();
            if (!GboardPatchesSettings.isUserUnlocked(context)
                    || !GboardFlickUpCompleteSettings.isEnabled(context)
                    || !isBodyHolder(holder)) {
                reset(holder);
                return false;
            }
            GboardFlickUpCompleteGesture gesture = gestureFor(holder);
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    int zone = resolveTouchZone(holder, event);
                    if (zone == GboardFlickUpCompleteGesture.ZONE_NONE) {
                        gesture.reset();
                        return false;
                    }
                    gesture.start(
                            event.getPointerId(0),
                            zone,
                            event.getRawX(),
                            event.getRawY(),
                            event.getDownTime(),
                            density(holder),
                            scaledTouchSlop(context),
                            ViewConfiguration.getLongPressTimeout());
                    return false;

                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                    boolean consumingPointer = gesture.isConsuming();
                    if (!consumingPointer) {
                        gesture.reset();
                    }
                    return consumingPointer;

                case MotionEvent.ACTION_MOVE:
                    if (gesture.isConsuming()) {
                        return true;
                    }
                    int index = event.findPointerIndex(event.getPointerId(0));
                    if (index < 0) {
                        gesture.reset();
                        return false;
                    }
                    float rawX = index == 0 ? event.getRawX() : rawX(holder, event, index);
                    float rawY = index == 0 ? event.getRawY() : rawY(holder, event, index);
                    if (gesture.move(
                            event.getPointerId(index),
                            rawX,
                            rawY,
                            event.getEventTime())) {
                        int triggeredZone = gesture.getZone();
                        View targetCandidate = selectCandidateForZone(holder, triggeredZone);
                        if (targetCandidate == null) {
                            // No visible candidates in bar; let normal typing continue.
                            gesture.reset();
                            gesture.disqualify();
                            return false;
                        }
                        cancelHolderTouch(holder, event);
                        triggerCandidateSelection(targetCandidate);
                        return true;
                    }
                    return false;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    boolean consumed = gesture.isConsuming();
                    gesture.reset();
                    return consumed;

                default:
                    return gesture.isConsuming();
            }
        } catch (Throwable throwable) {
            reset(holder);
            Log.w(TAG, "Flick-up word completion failed closed", throwable);
            return false;
        }
    }

    static boolean isBodyHolder(View holder) {
        if (holder == null || holder.getVisibility() != View.VISIBLE || !holder.isShown()) {
            return false;
        }
        Object tag = holder.getTag();
        if (HEADER_AREA_TAG.equals(tag)) {
            return false;
        }
        if (BODY_AREA_TAG.equals(tag)) {
            return true;
        }
        String entryName = resourceEntryName(holder);
        if ("header".equals(entryName)) {
            return false;
        }
        if ("body".equals(entryName) || "keyboard_body_holder".equals(entryName)) {
            return true;
        }
        return "KeyboardViewHolder".equals(holder.getClass().getSimpleName());
    }

    private static int resolveTouchZone(View holder, MotionEvent event) {
        int width = holder.getWidth();
        if (width <= 0) {
            return GboardFlickUpCompleteGesture.ZONE_NONE;
        }
        int[] location = new int[2];
        holder.getLocationOnScreen(location);
        float holderLeft = location[0];
        float holderRight = holderLeft + width;
        return GboardFlickUpCompleteGesture.resolveZone(
                event.getRawX(), holderLeft, holderRight);
    }

    static View selectCandidateForZone(View holder, int zone) {
        View root = holder.getRootView();
        if (root == null) {
            return null;
        }
        List<View> candidates = findVisibleCandidateViews(root, holder);
        if (candidates.isEmpty()) {
            return null;
        }
        int size = candidates.size();
        return switch (zone) {
            case GboardFlickUpCompleteGesture.ZONE_LEFT -> candidates.get(0);
            case GboardFlickUpCompleteGesture.ZONE_CENTER ->
                    size >= 3 ? candidates.get(1) : candidates.get(0);
            case GboardFlickUpCompleteGesture.ZONE_RIGHT ->
                    candidates.get(Math.min(2, size - 1));
            default -> null;
        };
    }

    static List<View> findVisibleCandidateViews(View root, View bodyHolder) {
        List<View> holderContainers = new ArrayList<View>();
        collectCandidateContainers(root, bodyHolder, holderContainers);

        List<View> candidates = new ArrayList<View>();
        if (!holderContainers.isEmpty()) {
            for (int i = 0; i < holderContainers.size(); i++) {
                collectCandidateSoftKeyViews(holderContainers.get(i), candidates);
            }
        } else {
            collectHeaderCandidateSoftKeyViewsFallback(root, bodyHolder, candidates);
        }

        if (candidates.size() > 1) {
            Collections.sort(candidates, new Comparator<View>() {
                @Override
                public int compare(View left, View right) {
                    return Integer.compare(screenCenterX(left), screenCenterX(right));
                }
            });
        }
        return candidates;
    }

    private static void collectCandidateContainers(
            View view, View bodyHolder, List<View> outContainers) {
        if (view == null || view == bodyHolder || view.getVisibility() != View.VISIBLE) {
            return;
        }
        if (isExcludedHeaderSubtree(view)) {
            return;
        }
        String entryName = resourceEntryName(view);
        String className = view.getClass().getName();
        if (FIXED_CANDIDATES_ENTRY_NAME.equals(entryName)
                || className.endsWith("CandidatesHolderView")
                || className.contains("FixedCandidatesHolder")) {
            outContainers.add(view);
            return;
        }
        if (view instanceof ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                collectCandidateContainers(group.getChildAt(i), bodyHolder, outContainers);
            }
        }
    }

    private static void collectHeaderCandidateSoftKeyViewsFallback(
            View root, View bodyHolder, List<View> outCandidates) {
        List<View> headerHolders = new ArrayList<View>();
        collectHeaderHolders(root, bodyHolder, headerHolders);
        for (int i = 0; i < headerHolders.size(); i++) {
            collectCandidateSoftKeyViews(headerHolders.get(i), outCandidates);
        }
    }

    private static void collectHeaderHolders(
            View view, View bodyHolder, List<View> outHeaderHolders) {
        if (view == null || view == bodyHolder || view.getVisibility() != View.VISIBLE) {
            return;
        }
        Object tag = view.getTag();
        String entryName = resourceEntryName(view);
        if (HEADER_AREA_TAG.equals(tag) || "header".equals(entryName)) {
            outHeaderHolders.add(view);
            return;
        }
        if (view instanceof ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                collectHeaderHolders(group.getChildAt(i), bodyHolder, outHeaderHolders);
            }
        }
    }

    private static void collectCandidateSoftKeyViews(View view, List<View> outCandidates) {
        if (view == null || view.getVisibility() != View.VISIBLE || !view.isShown()) {
            return;
        }
        if (isExcludedHeaderSubtree(view)) {
            return;
        }
        if (isCandidateSoftKeyView(view)) {
            outCandidates.add(view);
            return;
        }
        if (view instanceof ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                collectCandidateSoftKeyViews(group.getChildAt(i), outCandidates);
            }
        }
    }

    private static boolean isExcludedHeaderSubtree(View view) {
        String className = view.getClass().getName();
        if (className.contains("AccessPointsBar")
                || className.contains("AccessPointsPanel")
                || className.contains("ProactiveSuggestionsHolder")) {
            return true;
        }
        String entryName = resourceEntryName(view);
        if (entryName == null) {
            return false;
        }
        return entryName.startsWith("key_pos_header_")
                || entryName.contains("access_points")
                || entryName.contains("proactive_suggestions");
    }

    private static boolean isCandidateSoftKeyView(View view) {
        if (view.getWidth() <= 0 || view.getHeight() <= 0) {
            return false;
        }
        String simpleName = view.getClass().getSimpleName();
        boolean isSoftKey = "SoftKeyView".equals(simpleName)
                || view.getClass().getName().endsWith(".SoftKeyView");
        if (!isSoftKey && !view.isClickable()) {
            return false;
        }
        String entryName = resourceEntryName(view);
        if (entryName != null && entryName.startsWith("key_pos_header_")) {
            return false;
        }
        return hasNonEmptyCandidateLabel(view);
    }

    private static boolean hasNonEmptyCandidateLabel(View view) {
        if (view instanceof TextView textView) {
            CharSequence text = textView.getText();
            if (text != null && !text.toString().trim().isEmpty()) {
                return true;
            }
        }
        if (view instanceof ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child.getVisibility() == View.VISIBLE && hasNonEmptyCandidateLabel(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int screenCenterX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0] + (view.getWidth() / 2);
    }

    private static void cancelHolderTouch(View holder, MotionEvent event) {
        MotionEvent cancel = MotionEvent.obtain(event);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        CANCELLING.set(Boolean.TRUE);
        try {
            holder.dispatchTouchEvent(cancel);
        } finally {
            CANCELLING.remove();
            cancel.recycle();
        }
    }

    static boolean triggerCandidateSelection(View candidateView) {
        if (candidateView == null) {
            return false;
        }
        long now = SystemClock.uptimeMillis();
        float centerX = Math.max(1f, candidateView.getWidth() / 2f);
        float centerY = Math.max(1f, candidateView.getHeight() / 2f);
        MotionEvent down = MotionEvent.obtain(
                now, now, MotionEvent.ACTION_DOWN, centerX, centerY, 0);
        MotionEvent up = MotionEvent.obtain(
                now, now + 10L, MotionEvent.ACTION_UP, centerX, centerY, 0);
        DISPATCHING_CANDIDATE.set(Boolean.TRUE);
        try {
            boolean downHandled = candidateView.dispatchTouchEvent(down);
            boolean upHandled = candidateView.dispatchTouchEvent(up);
            if (!downHandled && !upHandled) {
                return candidateView.performClick();
            }
            return true;
        } finally {
            DISPATCHING_CANDIDATE.remove();
            down.recycle();
            up.recycle();
        }
    }

    private static String resourceEntryName(View view) {
        int id = view.getId();
        if (id == View.NO_ID || view.getResources() == null) {
            return null;
        }
        try {
            return view.getResources().getResourceEntryName(id);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static GboardFlickUpCompleteGesture gestureFor(View holder) {
        synchronized (GESTURES) {
            GboardFlickUpCompleteGesture gesture = GESTURES.get(holder);
            if (gesture == null) {
                gesture = new GboardFlickUpCompleteGesture();
                GESTURES.put(holder, gesture);
            }
            return gesture;
        }
    }

    private static void reset(View holder) {
        synchronized (GESTURES) {
            GboardFlickUpCompleteGesture gesture = GESTURES.get(holder);
            if (gesture != null) {
                gesture.reset();
            }
        }
    }

    private static float density(View view) {
        if (view.getResources() == null) {
            return 1f;
        }
        DisplayMetrics metrics = view.getResources().getDisplayMetrics();
        return metrics != null && metrics.density > 0f ? metrics.density : 1f;
    }

    private static int scaledTouchSlop(Context context) {
        if (context == null) {
            return 0;
        }
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private static float rawX(View holder, MotionEvent event, int pointerIndex) {
        int[] location = new int[2];
        holder.getLocationOnScreen(location);
        return location[0] + event.getX(pointerIndex);
    }

    private static float rawY(View holder, MotionEvent event, int pointerIndex) {
        int[] location = new int[2];
        holder.getLocationOnScreen(location);
        return location[1] + event.getY(pointerIndex);
    }
}
