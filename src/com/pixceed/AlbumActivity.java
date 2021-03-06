package com.pixceed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pixceed.fragment.AlbumFragment;
import com.pixceed.util.Memory;

public class AlbumActivity extends ActionBarActivity
{
	private AlbumFragment albumFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		final FragmentManager fragmentManager = getSupportFragmentManager();
		albumFragment = (AlbumFragment) fragmentManager.findFragmentByTag(AlbumFragment.TAG);
		if (albumFragment == null)
		{
			albumFragment = new AlbumFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("id", getIntent().getExtras().getInt("com.pixceed.AlbumId"));
			albumFragment.setArguments(bundle);
			fragmentManager.beginTransaction()
					.add(R.id.album, albumFragment, AlbumFragment.TAG)
					.commit();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putBoolean(AlbumFragment.IS_PICTURE_EXTENDED_KEY, albumFragment.isPictureExtended());
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id)
		{
		case R.id.action_settings:
			startActivity(new Intent(AlbumActivity.this, SettingsActivity.class));
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_refresh:
			albumFragment.update(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed()
	{
		if (albumFragment.isPictureExtended()) albumFragment.minimizePicture();
		else super.onBackPressed();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d("ALBUM", "save data");
		Memory.save(getSharedPreferences(Memory.PIXCEED_TAG, Context.MODE_PRIVATE).edit()).commit();
	}
}
