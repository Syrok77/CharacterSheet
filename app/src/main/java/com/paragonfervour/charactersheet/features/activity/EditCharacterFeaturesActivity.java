package com.paragonfervour.charactersheet.features.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.GameCharacter;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Activity that allows the user to edit their character's features, such as name, race, class, etc.
 */
public class EditCharacterFeaturesActivity extends RoboActivity {

    @Inject
    private CharacterDAO mCharacterDAO;

    @InjectView(R.id.edit_character_feature_name)
    private TextView mEditName;

    @InjectView(R.id.edit_character_feature_race)
    private TextView mEditRace;

    @InjectView(R.id.edit_character_feature_class)
    private TextView mEditClass;

    @InjectView(R.id.edit_character_feature_level)
    private TextView mEditLevel;

    @InjectView(R.id.edit_character_feature_speed)
    private TextView mEditSpeed;

    @InjectView(R.id.activity_edit_feature_toolbar)
    private Toolbar mToolbar;

    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_features);

        mCompositeSubscription = new CompositeSubscription();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitle(R.string.edit_activity_label);

        mCompositeSubscription.add(mCharacterDAO.getActiveCharacter().subscribe(new Action1<GameCharacter>() {
            @Override
            public void call(GameCharacter gameCharacter) {
                mEditName.setText(gameCharacter.getInfo().getName());
                mEditRace.setText(gameCharacter.getInfo().getRace());
                mEditClass.setText(gameCharacter.getInfo().getCharacterClass());
                mEditLevel.setText(String.valueOf(gameCharacter.getInfo().getLevel()));
                mEditSpeed.setText(String.valueOf(gameCharacter.getSpeed()));
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void finish() {
        super.finish();
        // Update character with fields
        mCharacterDAO.getActiveCharacter().subscribe(new Subscriber<GameCharacter>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GameCharacter gameCharacter) {
                gameCharacter.getInfo().setName(mEditName.getText().toString());
                gameCharacter.getInfo().setRace(mEditRace.getText().toString());
                gameCharacter.getInfo().setCharacterClass(mEditClass.getText().toString());

                if (!mEditLevel.getText().toString().isEmpty()) {
                    gameCharacter.getInfo().setLevel(Integer.valueOf(mEditLevel.getText().toString()));
                }

                if (!mEditSpeed.getText().toString().isEmpty()) {
                    gameCharacter.setSpeed(Integer.valueOf(mEditSpeed.getText().toString()));
                }

                unsubscribe();
                mCharacterDAO.activeCharacterUpdated();
            }
        });
    }
}
