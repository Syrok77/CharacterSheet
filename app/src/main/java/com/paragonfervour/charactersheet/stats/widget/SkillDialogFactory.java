package com.paragonfervour.charactersheet.stats.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Skill;

import rx.functions.Action1;


public class SkillDialogFactory {

    private Context mContext;
    private CharacterDAO mCharacterDAO;

    public interface SkillCreatedListener {
        void onSkillCreated(Skill skill);
    }

    @Inject
    public SkillDialogFactory(Context context, CharacterDAO characterDAO) {
        mContext = context;
        mCharacterDAO = characterDAO;
    }

    public AlertDialog createSkillDialog(final SkillCreatedListener listener) {

        // Is there a better way to access the fields in the custom view than keeping a reference
        // to this inflated view?
        final View customView = LayoutInflater.from(mContext).inflate(R.layout.dialog_skill, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true)
                .setTitle(R.string.skill_add_new)
                .setPositiveButton(R.string.skill_new_accept, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Skill skill = new Skill();

                        EditText nameText = (EditText) customView.findViewById(R.id.skill_dialog_name);
                        EditText valueText = (EditText) customView.findViewById(R.id.skill_dialog_value);


                        skill.setValue(Integer.valueOf(valueText.getText().toString()));
                        skill.setName(nameText.getText().toString());

                        mCharacterDAO.getActiveCharacter()
                                .subscribe(new Action1<GameCharacter>() {
                                    @Override
                                    public void call(GameCharacter gameCharacter) {
                                        gameCharacter.getSkills().add(skill);
                                        listener.onSkillCreated(skill);
                                    }
                                });
                    }
                })
                .setNegativeButton(R.string.skill_new_cancel, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(customView);


        return builder.create();
    }
}
