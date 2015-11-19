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
import com.paragonfervour.charactersheet.character.model.Skill;

/**
 * Injectable class that creates, handles and forwards callbacks for creating/editing character skills.
 */
public class SkillDialogFactory {

    private Context mContext;

    public interface SkillListener {
        void onSkillCreated(Skill skill);

        void onSkillUpdated(Skill skill);

        void onSkillDeleted(Skill skill);
    }

    @Inject
    public SkillDialogFactory(Context context, CharacterDAO characterDAO) {
        mContext = context;
    }

    /**
     * Create a skill creation dialog.
     *
     * @param listener Skill creation callbacks.
     * @return AlertDialog to present to the user.
     */
    public AlertDialog createSkillDialog(final SkillListener listener) {
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
                        skill.setName(nameText.getText().toString().toLowerCase());

                        listener.onSkillCreated(skill);
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

    /**
     * Create skill update dialog, which you can use to change or delete an existing skill.
     *
     * @param listener    Skill dialog callbacks.
     * @param updateSkill Skill to present for udpating.
     * @return AlertDialog to display.
     */
    public AlertDialog updateSkillDialog(final SkillListener listener, final Skill updateSkill) {
        // Is there a better way to access the fields in the custom view than keeping a reference
        // to this inflated view?
        final View customView = LayoutInflater.from(mContext).inflate(R.layout.dialog_skill, null, false);

        // Initialize text views with updateSkill's value
        EditText nameText = (EditText) customView.findViewById(R.id.skill_dialog_name);
        EditText valueText = (EditText) customView.findViewById(R.id.skill_dialog_value);
        nameText.setText(updateSkill.getName());
        valueText.setText(String.valueOf(updateSkill.getValue()));

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true)
                .setTitle(R.string.skill_add_update)
                .setPositiveButton(R.string.skill_update_accept, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText nameText = (EditText) customView.findViewById(R.id.skill_dialog_name);
                        EditText valueText = (EditText) customView.findViewById(R.id.skill_dialog_value);

                        updateSkill.setValue(Integer.valueOf(valueText.getText().toString()));
                        updateSkill.setName(nameText.getText().toString());

                        listener.onSkillUpdated(updateSkill);
                    }
                })
                .setNegativeButton(R.string.skill_update_remove, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.onSkillDeleted(updateSkill);
                    }
                })
                .setView(customView);


        return builder.create();
    }
}
