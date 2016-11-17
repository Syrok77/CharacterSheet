package com.paragonfervour.charactersheet.component;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDao;
import com.paragonfervour.charactersheet.character.helper.CharacterHelper;
import com.paragonfervour.charactersheet.character.model.CharacterInfo;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.features.activity.EditCharacterFeaturesActivity;
import com.paragonfervour.charactersheet.features.widget.XpDialogFactory;
import com.paragonfervour.charactersheet.injection.Injectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Component that manages the drawer header view, which displays a character's info.
 * <p/>
 * NOTE: This is not injected. Use the constructor to pass in the header view.
 */
public class CharacterHeaderComponent extends Component {

    @Inject
    CharacterDao mCharacterDao;

    @Inject
    Context mContext;

    @Inject
    XpDialogFactory mXpDialogFactory;

    @BindView(R.id.drawer_header_character_name)
    TextView mCharacterName;

    @BindView(R.id.drawer_header_class_desc)
    TextView mDescription;

    @BindView(R.id.drawer_header_xp_button)
    TextView mXpButton;

    private final View mHeaderView;

    public CharacterHeaderComponent(View headerView) {
        Injectors.currentActivityComponent().inject(this);

        // bind views
        mHeaderView = headerView;
        ButterKnife.bind(this, headerView);

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

    @OnClick(R.id.drawer_header_edit_character_button)
    void onEditButtonClick() {
        Intent editCharacter = new Intent(mContext, EditCharacterFeaturesActivity.class);
        mContext.startActivity(editCharacter);
    }

    @OnClick(R.id.drawer_header_xp_button)
    void onXpButtonClick() {
        mXpDialogFactory.createAddXpDialog().show();
    }
}