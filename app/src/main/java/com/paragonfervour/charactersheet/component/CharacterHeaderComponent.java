package com.paragonfervour.charactersheet.component;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.helper.CharacterHelper;
import com.paragonfervour.charactersheet.character.model.CharacterInfo;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.features.activity.EditCharacterFeaturesActivity;
import com.paragonfervour.charactersheet.features.widget.XpDialogFactory;

import roboguice.RoboGuice;

/**
 * Component that manages the drawer header view, which displays a character's info.
 * <p/>
 * NOTE: This is not injected. Use the constructor to pass in the header view.
 * TODO: XP automatically leveling a character should be added to Settings. And implemented in the
 * TODO: first place.
 */
public class CharacterHeaderComponent extends Component {

    @Inject
    private CharacterDAO mCharacterDAO;

    @Inject
    private XpDialogFactory mXpDialogFactory;

    private final View mHeaderView;
    private final TextView mDescription;
    private final TextView mCharacterName;
    private final Button mXpButton;

    public CharacterHeaderComponent(View headerView) {
        RoboGuice.injectMembers(headerView.getContext(), this);

        // bind views
        mHeaderView = headerView;
        mDescription = (TextView) mHeaderView.findViewById(R.id.drawer_header_class_desc);
        mCharacterName = (TextView) mHeaderView.findViewById(R.id.drawer_header_character_name);
        mXpButton = (Button) mHeaderView.findViewById(R.id.drawer_header_xp_button);

        View editButton = mHeaderView.findViewById(R.id.drawer_header_edit_character_button);
        editButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent editCharacter = new Intent(context, EditCharacterFeaturesActivity.class);
            context.startActivity(editCharacter);
        });

        mXpButton.setOnClickListener(v -> mXpDialogFactory.createAddXpDialog().show());

        // dumb view compat stuff
        TypedValue typedValue = new TypedValue();
        headerView.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        ViewCompat.setBackgroundTintList(mXpButton, ColorStateList.valueOf(typedValue.data));
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

        mXpButton.setText(CharacterHelper.getXpDisplayText(mHeaderView.getContext(), gameCharacter.getInfo().getXp()));
    }
}