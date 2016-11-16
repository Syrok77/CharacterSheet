package com.paragonfervour.charactersheet.component;


import android.animation.Animator;
import android.app.Activity;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.paragonfervour.charactersheet.R;

import javax.inject.Inject;

/**
 * Class that allows access to the Floating Action Button. To use, call {@link #showFab(View.OnClickListener)}
 * and then {@link #hideFab()} to hide. This will show the FAB with a + icon.
 */
public class FloatingActionButtonComponent {

    @Inject
    FloatingActionButtonComponent(Activity activity) {
        mFab = (FloatingActionButton) activity.findViewById(R.id.fab);
    }

    private Animator mActiveHideAnimator;
    private final FloatingActionButton mFab;

    // region public methods -----------------------------------------------------------------------

    /**
     * Show the FAB.
     *
     * @param onClickListener click listener for the FAB
     */
    public void showFab(View.OnClickListener onClickListener) {
        assert mFab != null;

        if (mActiveHideAnimator != null) {
            mActiveHideAnimator.cancel();
        }

        mFab.setOnClickListener(onClickListener);

        // Sometimes this will be called before the views are measured, so post
        // the animation on a Runnable.
        mFab.post(() -> {
            mFab.setVisibility(View.VISIBLE);

            // Circular animations only work on Lollipop+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator floatingActionButtonRevealAnimator = ViewAnimationUtils.createCircularReveal(
                        mFab,
                        mFab.getWidth() / 2,
                        mFab.getHeight() / 2,
                        0,
                        mFab.getWidth());

                floatingActionButtonRevealAnimator.start();
            }
        });
    }

    /**
     * Hide the fab
     */
    public void hideFab() {
        assert mFab != null;

        if (mActiveHideAnimator != null) {
            // the FAB is in the process of being hidden already
            return;
        } else if (mFab.getVisibility() != View.VISIBLE) {
            // the FAB is already hidden, don't animate it again
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActiveHideAnimator = ViewAnimationUtils.createCircularReveal(
                    mFab,
                    mFab.getWidth() / 2,
                    mFab.getHeight() / 2,
                    mFab.getWidth(),
                    0);
            mActiveHideAnimator.addListener(new HideAnimationListener());

            mActiveHideAnimator.start();
        } else {
            mFab.setVisibility(View.INVISIBLE);
        }
    }

    // endregion

    // region inner classes ------------------------------------------------------------------------

    /**
     * AnimatorListener that will finish the hide animation properly.
     */
    private class HideAnimationListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mFab.setVisibility(View.INVISIBLE);
            mFab.setOnClickListener(null);
            mActiveHideAnimator = null;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // Remove this from the animation so that onAnimationEnd is not called.
            animation.removeListener(this);
            mActiveHideAnimator = null;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }

    // endregion

}
