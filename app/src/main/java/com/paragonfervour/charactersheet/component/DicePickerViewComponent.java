package com.paragonfervour.charactersheet.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.helper.DiceHelper;
import com.paragonfervour.charactersheet.character.model.Dice;
import com.paragonfervour.charactersheet.stats.widget.DiceDialogFactory;

import rx.Observable;
import rx.Observer;
import rx.subjects.BehaviorSubject;

/**
 * A button that allows the user to change the Dice associated with this button.
 */
public class DicePickerViewComponent extends LinearLayout {

    private Dice mDice;
    private BehaviorSubject<Dice> mDiceBehaviorSubject = BehaviorSubject.create();

    private ImageView mDiceImageView;

    public DicePickerViewComponent(Context context) {
        super(context);
        init();
    }

    public DicePickerViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DicePickerViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.component_dice_picker, this);

        mDiceImageView = (ImageView) findViewById(R.id.component_dice_picker_image);
        mDiceImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DiceDialogFactory.createDicePickerDialog(getContext())
                        .subscribe(new Observer<Dice>() {
                            @Override
                            public void onCompleted() {}

                            @Override
                            public void onError(Throwable e) {}

                            @Override
                            public void onNext(Dice dice) {
                                setDice(dice);
                            }
                        });
            }
        });

        if (isInEditMode()) {
            setDice(Dice.D4);
        }
    }

    /**
     * Get an Observable that emits changes to this component's chosen Dice.
     *
     * @return Observable that emits component's Dice.
     */
    public Observable<Dice> getDiceObservable() {
        return mDiceBehaviorSubject.asObservable();
    }

    public Dice getDice() {
        return mDice;
    }

    public void setDice(Dice dice) {
        mDice = dice;
        mDiceBehaviorSubject.onNext(mDice);

        mDiceImageView.setImageResource(DiceHelper.getDiceDrawable(mDice));
    }
}
