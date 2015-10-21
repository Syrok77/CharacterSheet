package com.paragonfervour.charactersheet.component;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.model.CharacterInfo;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.features.activity.EditCharacterFeaturesActivity;

/**
 * Component that manages the drawer header view, which displays a character's info.
 *
 * NOTE: This is not injected. Use the constructor to pass in the header view.
 * TODO: add XP controls to this.
 * TODO: XP automatically leveling a character should be added to Settings.
 */
public class CharacterHeaderComponent extends Component {

    private View mHeaderView;
    private TextView mDescription;
    private TextView mCharacterName;

    public CharacterHeaderComponent(View headerView) {
        // bind views
        mHeaderView = headerView;
        mDescription = (TextView) mHeaderView.findViewById(R.id.drawer_header_class_desc);
        mCharacterName = (TextView) mHeaderView.findViewById(R.id.drawer_header_character_name);
    }

    /**
     * Update this component with a GameCharacter object.
     *
     * @param gameCharacter game character to update this component with.
     */
    public void onActiveCharacter(GameCharacter gameCharacter) {
        CharacterInfo info = gameCharacter.getInfo();

        mDescription.setText(String.format(mHeaderView.getContext().getString(R.string.character_info_class_format), info.getLevel(), info.getCharacterClass()));
        mCharacterName.setText(info.getName());

        View editButton = mHeaderView.findViewById(R.id.drawer_header_edit_character_button);
        editButton.setOnClickListener(new EditCharacterClickListener());
    }

    private static class EditCharacterClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent editCharacter = new Intent(context, EditCharacterFeaturesActivity.class);
            context.startActivity(editCharacter);
        }
    }
}
