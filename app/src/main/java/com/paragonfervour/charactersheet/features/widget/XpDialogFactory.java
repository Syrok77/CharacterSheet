package com.paragonfervour.charactersheet.features.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDao;
import com.paragonfervour.charactersheet.character.model.GameCharacter;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Create XP related dialogs, such as Change/Add XP dialog.
 */
public class XpDialogFactory {

    private CharacterDao mCharacterDao;
    private Context mContext;

    @Inject
    XpDialogFactory(CharacterDao characterDao, Context context) {
        mCharacterDao = characterDao;
        mContext = context;
    }

    /**
     * Create a dialog that asks the user how much Xp to add to their character, and also gives them
     * the option of changing their xp to a specific amount.
     *
     * @return dialog with add xp option.
     */
    public AlertDialog createAddXpDialog() {
        View customView = LayoutInflater.from(mContext).inflate(R.layout.add_xp_dialog, null, false);
        final TextView xpAmount = (TextView) customView.findViewById(R.id.dialog_add_xp_amount);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_add_xp_title)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    if (!xpAmount.getText().toString().isEmpty()) {
                        final int amount = Integer.valueOf(xpAmount.getText().toString());
                        mCharacterDao.getActiveCharacter().subscribe(new Subscriber<GameCharacter>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onNext(GameCharacter gameCharacter) {
                                int xp = gameCharacter.getInfo().getXp() + amount;
                                gameCharacter.getInfo().setXp(xp);

                                unsubscribe();
                                mCharacterDao.activeCharacterUpdated();
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.dialog_add_xp_change, (dialog, which) -> {
                    createChangeXpDialog().show();
                })
                .setView(customView);

        return builder.create();
    }

    /**
     * Creates a dialog that gives the user the ability to set the amount of XP their character has.
     *
     * @return dialog with change xp option.
     */
    public AlertDialog createChangeXpDialog() {
        View customView = LayoutInflater.from(mContext).inflate(R.layout.change_xp_dialog, null, false);
        final TextView xpAmount = (TextView) customView.findViewById(R.id.dialog_change_xp_amount);

        return new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_change_xp_title)
                .setPositiveButton(R.string.dialog_change_xp_accept, (dialog, which) -> {
                    if (!xpAmount.getText().toString().isEmpty()) {
                        final int amount = Integer.valueOf(xpAmount.getText().toString());
                        mCharacterDao.getActiveCharacter().subscribe(new Subscriber<GameCharacter>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onNext(GameCharacter gameCharacter) {
                                gameCharacter.getInfo().setXp(amount);

                                unsubscribe();
                                mCharacterDao.activeCharacterUpdated();
                            }
                        });
                    }
                })
                .setView(customView)
                .create();
    }

}
