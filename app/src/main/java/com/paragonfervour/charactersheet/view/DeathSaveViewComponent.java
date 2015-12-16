package com.paragonfervour.charactersheet.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.paragonfervour.charactersheet.R;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Component that shows/counts a user's death save attempts. Counts failed death saves up to 3, at
 * which point a character is dead.
 * TODO: When you succeed, does your HP stay at 0 (assumed) or go to 1? Do you race to 3 or is 1
 * TODO: success good enough?
 */
public class DeathSaveViewComponent extends LinearLayout {

    private CheckBox mFirstFail;
    private CheckBox mSecondFail;
    private CheckBox mThirdFail;

    private final PublishSubject<Integer> mFailureCountSubject = PublishSubject.create();

    private int mFailCount;

    public DeathSaveViewComponent(Context context) {
        super(context);
        init();
    }

    public DeathSaveViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DeathSaveViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.death_saves_component, this);

        View checkboxSection = findViewById(R.id.death_saves_check_section);
        Button successButton = (Button) findViewById(R.id.death_saves_success_button);
        mFirstFail = (CheckBox) findViewById(R.id.death_saves_first_fail);
        mSecondFail = (CheckBox) findViewById(R.id.death_saves_second_fail);
        mThirdFail = (CheckBox) findViewById(R.id.death_saves_third_fail);

        checkboxSection.setOnClickListener(v -> {
            if (mFailCount == 3) {
                return;
            }

            mFailCount++;
            mFailureCountSubject.onNext(mFailCount);

            applyFailCountToView();
            if (mFailCount == 3) {
                onFinalFail();
            }
        });

        successButton.setOnClickListener(v -> {
            reset(0);
            mFailureCountSubject.onNext(0);
        });
    }

    /**
     * Reset this component back to 0 failures.
     */
    public void reset(int count) {
        mFailCount = count;

        applyFailCountToView();
    }

    public Observable<Integer> getFailuresObservable() {
        return mFailureCountSubject.asObservable();
    }

    private void applyFailCountToView() {
        if (mFailCount >= 3) {
            mThirdFail.setChecked(true);
        }

        if (mFailCount >= 2) {
            mSecondFail.setChecked(true);
        }

        if (mFailCount >= 1) {
            mFirstFail.setChecked(true);
        }

        if (mFailCount <= 0) {
            mFirstFail.setChecked(false);
            mSecondFail.setChecked(false);
            mThirdFail.setChecked(false);
        }
    }

    /**
     * When the final checkbox is hit, the character dies. Forever. :(
     */
    private void onFinalFail() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.death_message_title)
                .setMessage(R.string.death_message_description)
                .setNegativeButton(R.string.death_message_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
