package com.paragonfervour.charactersheet.stats.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.model.Dice;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Create dice related dialogs, such as a dice chooser dialog.
 */
public final class DiceDialogFactory {

    private DiceDialogFactory() {
    }

    /**
     * Create a dialog for the user to pick a dice from, which will emit signals to the returned
     * Observable.
     *
     * @param context Android context.
     * @return Observable that emits which dice was selected, and onCompleted when the dialog is closed.
     */
    @SuppressLint("InflateParams") // we don't have a ViewGroup to pass it in, just ignore the warning.
    public static Observable<Dice> createDicePickerDialog(Context context) {
        final PublishSubject<Dice> dialogResult = PublishSubject.create();

        View customView = LayoutInflater.from(context).inflate(R.layout.dice_picker_dialog, null, false);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(R.string.dice_picker_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialogResult.onCompleted();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialogResult.onCompleted();
                    }
                })
                .setView(customView)
                .show();

        bindDicePickerToView(customView, dialog, dialogResult);

        return dialogResult.asObservable();
    }

    /**
     * Bind the dice buttons with click listeners to the subject.
     *
     * @param alertCustomView Custom view for the alert.
     * @param alertSubject    Subject that publishes which dice is clicked.
     */
    private static void bindDicePickerToView(View alertCustomView, final AlertDialog dialog,
                                             final PublishSubject<Dice> alertSubject) {
        View d4 = alertCustomView.findViewById(R.id.dialog_dice_picker_d4);
        d4.setOnClickListener(new DiceClickListener(Dice.D4, alertSubject, dialog));

        View d6 = alertCustomView.findViewById(R.id.dialog_dice_picker_d6);
        d6.setOnClickListener(new DiceClickListener(Dice.D6, alertSubject, dialog));

        View d8 = alertCustomView.findViewById(R.id.dialog_dice_picker_d8);
        d8.setOnClickListener(new DiceClickListener(Dice.D8, alertSubject, dialog));

        View d10 = alertCustomView.findViewById(R.id.dialog_dice_picker_d10);
        d10.setOnClickListener(new DiceClickListener(Dice.D10, alertSubject, dialog));

        View d12 = alertCustomView.findViewById(R.id.dialog_dice_picker_d12);
        d12.setOnClickListener(new DiceClickListener(Dice.D12, alertSubject, dialog));

        View d20 = alertCustomView.findViewById(R.id.dialog_dice_picker_d20);
        d20.setOnClickListener(new DiceClickListener(Dice.D20, alertSubject, dialog));
    }

    /**
     * Click listener for a dice, which will publish the dice clicked and then dismiss the dialog.
     */
    private static class DiceClickListener implements View.OnClickListener {
        private Dice mDice;
        private PublishSubject<Dice> mAlertSubject;
        private AlertDialog mDialog;

        private DiceClickListener(Dice dice, PublishSubject<Dice> alertSubject, AlertDialog dialog) {
            mDice = dice;
            mAlertSubject = alertSubject;
            mDialog = dialog;
        }

        @Override
        public void onClick(View v) {
            mAlertSubject.onNext(mDice);
            mAlertSubject.onCompleted();
            mDialog.dismiss();
        }
    }


}
