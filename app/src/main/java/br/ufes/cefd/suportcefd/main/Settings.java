package br.ufes.cefd.suportcefd.main;

/**
 * Created by pgrippa on 22/10/16.
 */

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import br.ufes.cefd.suportcefd.R;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction().replace(android.R.id.content, settingsFragment).commit();
    }

    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Bundle b = getArguments();
            if(b!=null){
                String type = b.getString("type");
                EditTextPreference preference = (EditTextPreference) findPreference("webservice");
                preference.setEnabled(type.equals("user") ? false : true);
            }
        }
    }

}
