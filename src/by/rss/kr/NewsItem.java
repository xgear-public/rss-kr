package by.rss.kr;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "news_items")
public class NewsItem {

	@DatabaseField(generatedId = true)
	private int _id;
	
	@DatabaseField
	private Date date;
	@DatabaseField
	private String title;
	@DatabaseField
	private String description;
	@DatabaseField
	private String imageUrl;
	
	public NewsItem() {
		super();
	}
	
	public NewsItem( Date date, String title, String description, String imageUrl) {
		super();
		this.date = date;
		this.title = title;
		this.description = description;
		this.imageUrl = imageUrl;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
