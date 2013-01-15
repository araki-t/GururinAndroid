package jp.co.ricoh;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class SelectView extends PreferenceActivity {
	ListPreference list;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.selctview);
        Bundle extras=getIntent().getExtras();
		Integer num=0;
		if(extras!=null){
			num = extras.getInt("keyword");
			
		}
        ListPreference list_preference = (ListPreference)getPreferenceScreen().findPreference("hoge");
        list_preference.setSummary(list_preference.getEntry());
        list_preference.setValueIndex(num);
        ListPreference list = (ListPreference)findPreference("hoge");
		Intent data = new Intent();
		data.putExtra("keyword", list.getValue());
		setResult(RESULT_OK, data);		
    }
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,  String key) {
		Intent data = new Intent();
		data.putExtra("keyword", list.getValue());
		setResult(RESULT_OK, data);
		 ListPreference list_preference = (ListPreference)getPreferenceScreen().findPreference("hoge");
	        list_preference.setSummary(list_preference.getEntry());
	}
	private SharedPreferences.OnSharedPreferenceChangeListener listener =   
		    new SharedPreferences.OnSharedPreferenceChangeListener() {  
		       
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,  String key) {
			ListPreference list_preference = (ListPreference)getPreferenceScreen().findPreference("hoge");
		    list_preference.setSummary(list_preference.getEntry());
			Intent data = new Intent();
			data.putExtra("keyword", list_preference.getValue());
			setResult(RESULT_OK, data);
			 
		} 
	};  
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
	}
	 
	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
	}


    public static String getIP(Context con){
        return PreferenceManager.getDefaultSharedPreferences(con).getString("check1", "127.0.0.1");
    }
}

