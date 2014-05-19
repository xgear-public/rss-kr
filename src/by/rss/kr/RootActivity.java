package by.rss.kr;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.mcsoxford.rss.MediaThumbnail;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class RootActivity extends ActionBarActivity {

	private ListView mNewsList;
	private NewsAdapter mAdapter;
	private DBHelper databaseHelper;
	private MenuItem refreshItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_root);
		mNewsList = (ListView) findViewById(R.id.news_list);
		mAdapter = new NewsAdapter(new ArrayList<NewsItem>());
		mNewsList.setAdapter(mAdapter);
		List<NewsItem> data = getDBHelper().getAllNews();
		if(data != null) {
			mAdapter.addData(data);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.root, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh: {
				refreshItem = item;
				if (isNetworkConnected(this)) {
					new LoadNewsTask(this).execute();
				} else {
					showConnectionErrorDialog(this, false);
				}
				break;
			}
			case R.id.info: {
				Intent i = new Intent(this, InfoActivity.class);
				startActivity(i);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	public DBHelper getDBHelper() {
	    if (databaseHelper == null) {
	        databaseHelper = OpenHelperManager.getHelper(this, DBHelper.class);
	    }
	    return databaseHelper;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    if (databaseHelper != null) {
	        OpenHelperManager.releaseHelper();
	        databaseHelper = null;
	    }
	}

	private final static class LoadNewsTask extends AsyncTask<Object, Object, List<NewsItem>> {

		private WeakReference<RootActivity> mActivityHolder;

		public LoadNewsTask(RootActivity context) {
			mActivityHolder = new WeakReference<RootActivity>(context);
		}

//		private DimProgressDialog dimProgressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			RootActivity context = mActivityHolder.get();
			if (context != null) {
//				dimProgressDialog = new DimProgressDialog(context);
//				dimProgressDialog.show();
				context.refresh();
			
			}
		}

		@Override
		protected List<NewsItem> doInBackground(Object... params) {
			final List<NewsItem> retValue = new ArrayList<NewsItem>();

			try {
				RootActivity context = mActivityHolder.get();
				if (context != null) {

					RSSReader reader = new RSSReader();
					String uri = context.getString(R.string.url);
					RSSFeed feed = reader.load(uri);
					List<RSSItem> rssItems = feed.getItems();
					for (RSSItem rssItem : rssItems) {
						if (rssItem != null) {
							String url = "";
							if (rssItem.getEnclosure() != null && rssItem.getEnclosure().getUrl() != null)
								url = rssItem.getEnclosure().getUrl().toString();
							if(TextUtils.isEmpty(url)) {
								List<MediaThumbnail> thumbnails = rssItem.getThumbnails();
								if(thumbnails != null && !thumbnails.isEmpty()) {
									url = thumbnails.get(0).getUrl().toString();
								}
							}
							retValue.add(new NewsItem(rssItem.getPubDate(),
									rssItem.getTitle(),
									rssItem.getDescription(),
									url));
						}
					}
					context.getDBHelper().deleteAllNews();
					context.getDBHelper().addNewsItemList(retValue);
				}
			} catch (RSSReaderException e) {
				e.printStackTrace();
			}

			return retValue;
		}

		@Override
		protected void onPostExecute(List<NewsItem> result) {
			super.onPostExecute(result);
//			dimProgressDialog.dismiss();

			RootActivity context = mActivityHolder.get();
			if (context != null) {
				context.completeRefresh();

				if (isCancelled())
					return;

				context.mAdapter.setData(result);
				context.mAdapter.notifyDataSetChanged();
			}

		}
	}
	
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
    }
    
	public static class DimProgressDialog {
		private Dialog dialog;

		public DimProgressDialog(Context context) {
			dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
			dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.dimAmount = 0.6f; // Dim level. 0.0 - no dim, 1.0 - completely
									// opaque
			dialog.getWindow().setAttributes(lp);
			dialog.setContentView(R.layout.progress_view);
			dialog.setCancelable(false);
		}

		public void show() {
			dialog.show();
		}

		public void dismiss() {
			dialog.dismiss();
		}

	}

    public static void showConnectionErrorDialog(Activity context, final boolean shouldBeClosed) {
        showErrorDialog(context, context.getString(R.string.connection_faield),
                context.getString(R.string.operation_failed), shouldBeClosed);
    }

    public static void showErrorDialog(final Activity context, String message, String title,
            final boolean shouldBeClosed) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(context.getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (shouldBeClosed) {
                                    context.finish();
                                }
                                dialog.dismiss();
                            }
                        }).setCancelable(false);

        if (title != null && !title.equals("")) {
            builder.setTitle(title);
        }

        alert = builder.create();
        alert.show();

    }
    public void refresh() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.action_view, null);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_upd);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        MenuItemCompat.setActionView(refreshItem, iv);
    }
    
    public void completeRefresh() {
    	MenuItemCompat.getActionView(refreshItem).clearAnimation();
        MenuItemCompat.setActionView(refreshItem, null);
    }

}
