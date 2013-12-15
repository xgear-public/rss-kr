package by.rss.kr;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsAdapter extends BaseAdapter {
	
	private List<NewsItem> mData;
	private static final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
	
	public NewsAdapter(List<NewsItem> mData) {
		super();
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public NewsItem getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = parent.getContext();
		ViewHolder holder;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null, false);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NewsItem card = mData.get(position);
		holder.title.setText(card.getTitle());
		holder.date.setText(mFormat.format(card.getDate()));
		Picasso.with(context).load(card.getImageUrl()).into(holder.img);
		return convertView;
	}

	public void setData(List<NewsItem> dataList) {
		mData = dataList;
	}
	
	public void addData(List<NewsItem> dataList) {
		mData.addAll(dataList);
	}
	
	public void clearData() {
		mData.clear();
	}
	
	static class ViewHolder {
		ImageView img;
		TextView title;
		TextView date;
	}

}
