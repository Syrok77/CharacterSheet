package com.paragonfervour.charactersheet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Component view that contains a stat value control, used to +/- a stat's value on screen, such as
 * an ability score or the player's HP.
 * <p/>
 * In order to react to change events and save changes, subscribe to getValueObservable()
 */
public class StatValueComponent extends LinearLayout {

    private static final int DELTA_COUNTER_DELAY = 4000;

    // child views
    private TextView mValueTextView;
    private TextView mDeltaTextView;
    private TextView mTitleTextView;

    // members
    private PublishSubject<Integer> mValuePublisher = PublishSubject.create();
    private Handler mCurrentDeltaHandler = new Handler();
    private Runnable mCurrentDeltaRunner = null;

    private int mHapticInterval = -1;
    private int mCurrentDelta = 0;

    public StatValueComponent(Context context) {
        super(context);
        init(null);
    }

    public StatValueComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatValueComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.component_stat_value, this, true);
        String title = null;

        mValueTextView = (TextView) findViewById(R.id.component_stat_value);
        mDeltaTextView = (TextView) findViewById(R.id.component_stat_delta_counter);
        mTitleTextView = (TextView) findViewById(R.id.component_stat_title);
        ImageButton valueIncreaseButton = (ImageButton) findViewById(R.id.component_stat_increase_button);
        ImageButton valueDecreaseButton = (ImageButton) findViewById(R.id.component_stat_decrease_button);
        View controlSection = findViewById(R.id.component_stat_control_section);

        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.StatValueComponent);
            if (attributes != null) {
                mHapticInterval = attributes.getInt(R.styleable.StatValueComponent_hapticInterval, -1);
                title = attributes.getString(R.styleable.StatValueComponent_statTitle);
                int color = attributes.getColor(R.styleable.StatValueComponent_statBackground, 0);
                if (color == 0) {
                    //noinspection deprecation
                    color = getResources().getColor(R.color.blue_700);
                }

                GradientDrawable bg = (GradientDrawable) controlSection.getBackground();
                bg.setColor(color);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    controlSection.setBackgroundDrawable(bg);
                } else {
                    controlSection.setBackground(bg);
                }

                attributes.recycle();
            }
        }

        mTitleTextView.setText(title);

        valueIncreaseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCounter(1);
            }
        });

        valueDecreaseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCounter(-1);
            }
        });
    }

    /**
     * @return an Observable that emits this components values as they are edited by the user.
     */
    public Observable<Integer> getValueObservable() {
        return mValuePublisher.asObservable();
    }

    /**
     * @return current counter value.
     */
    public int getValue() {
        if (mValueTextView != null && !mValueTextView.getText().toString().isEmpty()) {
            return Integer.valueOf(mValueTextView.getText().toString());
        }
        return -1;
    }

    /**
     * Set the counter's value
     *
     * @param value Integer value for this view to express.
     */
    public void setValue(int value) {
        if (mValueTextView != null) {
            mValueTextView.setText(String.valueOf(value));
        }
        mValuePublisher.onNext(value);
    }

    /**
     * Set increment interval to fire haptic feedback to the user. This will fire a slight buzz every
     * time this interval is hit. For example, a character attribute counter might set this to 2, so that
     * every time a character's ability score increases they get some user feedback from it.
     *
     * @param interval haptic interval.
     */
    @SuppressWarnings("unused")
    public void setHapticInterval(int interval) {
        mHapticInterval = interval;
    }

    /**
     * Change the value by the given delta. Changes main displayed value and also shows a temporary
     * counter that shows how much you've changed it by recently.
     *
     * @param by delta to apply to value
     */
    private void changeCounter(int by) {
        int value = getValue();
        mCurrentDelta += by;
        mDeltaTextView.setAlpha(1f);
        if (mCurrentDelta != 0) {
            // Show the counter
            String text = mCurrentDelta > 0 ? "+" : "";
            text = text + mCurrentDelta;
            mDeltaTextView.setText(text);

            // set fade timer
            if (mCurrentDeltaRunner != null) {
                mCurrentDeltaHandler.removeCallbacks(mCurrentDeltaRunner);
            }
            mCurrentDeltaRunner = new Runnable() {
                @Override
                public void run() {
                    mCurrentDelta = 0;
                    mDeltaTextView.animate()
                            .alpha(0)
                            .start();
                }
            };
            mCurrentDeltaHandler.postDelayed(mCurrentDeltaRunner, DELTA_COUNTER_DELAY);
        } else {
            // hide the counter
            mDeltaTextView.setText("");
        }

        setValue(value + by);

        // Send haptic feedback
        if (mHapticInterval > 0 && getValue() % mHapticInterval == 0) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }


        if (by != 0) {
            mValuePublisher.onNext(getValue());
        }
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }
}
