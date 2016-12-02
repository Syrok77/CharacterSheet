package com.paragonfervour.charactersheet.injection;


import com.paragonfervour.charactersheet.activity.CharacterActivity;
import com.paragonfervour.charactersheet.component.CharacterHeaderComponent;
import com.paragonfervour.charactersheet.features.activity.EditCharacterFeaturesActivity;
import com.paragonfervour.charactersheet.fragment.CharacterPagerFragment;
import com.paragonfervour.charactersheet.fragment.OffenseFragment;
import com.paragonfervour.charactersheet.offense.activity.AddWeaponActivity;
import com.paragonfervour.charactersheet.spells.activity.AddSpellActivity;
import com.paragonfervour.charactersheet.spells.fragment.SpellsFragment;
import com.paragonfervour.charactersheet.stats.fragment.StatsFragment;
import com.paragonfervour.charactersheet.view.StatValueViewComponent;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    // region activities ---------------------------------------------------------------------------

    void inject(AddSpellActivity addSpellActivity);
    void inject(AddWeaponActivity addWeaponActivity);
    void inject(CharacterActivity characterActivity);
    void inject(EditCharacterFeaturesActivity editCharacterFeaturesActivity);

    // endregion

    // region fragments ----------------------------------------------------------------------------

    void inject(CharacterPagerFragment characterPagerFragment);
    void inject(OffenseFragment offenseFragment);
    void inject(SpellsFragment spellsFragment);
    void inject(StatsFragment statsFragment);

    // endregion

    // region components ---------------------------------------------------------------------------

    void inject(CharacterHeaderComponent characterHeaderComponent);

    // endregion

    // region view components ----------------------------------------------------------------------

    void inject(StatValueViewComponent statValueViewComponent);

    // endregion

}
