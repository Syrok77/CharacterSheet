package com.paragonfervour.charactersheet.features.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDao;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.injection.Injectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Activity that allows the user to edit their character's features, such as name, race, class, etc.
 */
public class EditCharacterFeaturesActivity extends AppCompatActivity {

    @Inject
    CharacterDao mCharacterDao;

    @BindView(R.id.edit_character_feature_name)
    TextView mEditName;

    @BindView(R.id.edit_character_feature_race)
    TextView mEditRace;

    @BindView(R.id.edit_character_feature_class)
    TextView mEditClass;

    @BindView(R.id.edit_character_feature_level)
    TextView mEditLevel;

    @BindView(R.id.edit_character_feature_speed)
    TextView mEditSpeed;

    @BindView(R.id.activity_edit_feature_toolbar)
    Toolbar mToolbar;

    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injectors.activityComponent(this).inject(this);
        setContentView(R.layout.edit_features_activity);
        ButterKnife.bind(this);

        mCompositeSubscription = new CompositeSubscription();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitle(R.string.edit_activity_label);

        mCompositeSubscription.add(mCharacterDao.getActiveCharacterStream().subscribe(gameCharacter -> {
            mEditName.setText(gameCharacter.getInfo().getName());
            mEditRace.setText(gameCharacter.getInfo().getRace());
            mEditClass.setText(gameCharacter.getInfo().getCharacterClass());
            mEditLevel.setText(String.valueOf(gameCharacter.getInfo().getLevel()));
            mEditSpeed.setText(String.valueOf(gameCharacter.getSpeed()));
        }, throwable -> {
            // ignore errors
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
        mCharacterDao.getActiveCharacterStream().subscribe(new Subscriber<GameCharacter>() {
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
                mCharacterDao.activeCharacterUpdated();
            }
        });
    }
}
