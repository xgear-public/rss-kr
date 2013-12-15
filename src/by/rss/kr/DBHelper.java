package by.rss.kr;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "cof.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	private Dao<NewsItem, Integer> profileDao = null;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(connectionSource, NewsItem.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource arg1, int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, NewsItem.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void clearAllData() {
		try {
			Dao<NewsItem, Integer> daoProfile = getNewsItemDao();
			DeleteBuilder<NewsItem, Integer> dbProfile = daoProfile.deleteBuilder();
			daoProfile.delete(dbProfile.prepare());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Dao<NewsItem, Integer> getNewsItemDao() throws SQLException {
		if (profileDao == null) {
			profileDao = getDao(NewsItem.class);
		}
		return profileDao;
	}


	public void addNewsItemList(final List<NewsItem> newsList) {
		final Dao<NewsItem, Integer> dao;
		try {
			dao = getNewsItemDao();
			dao.callBatchTasks(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					for (NewsItem item : newsList) {
						dao.create(item);
					}
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public List<NewsItem> getAllNews() {
		try {
			Dao<NewsItem, Integer> dao = getNewsItemDao();
			return  dao.queryForAll();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public void deleteAllNews() {
		try {
			Dao<NewsItem, Integer> dao = getNewsItemDao();
			DeleteBuilder<NewsItem, Integer> db = dao.deleteBuilder();
			dao.delete(db.prepare());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

/*	public Profile getProfile(int id) {
		try {
			List<Profile> profiles = getNewsItemDao().queryForEq("uid", id);
			if (profiles != null && !profiles.isEmpty())
				return profiles.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int updateProfile(Profile profile) {
		try {
			Dao<Profile, Integer> dao = getNewsItemDao();
			UpdateBuilder<Profile, Integer> updateBuilder = dao.updateBuilder();
			updateBuilder.updateColumnValue("firstName", profile.getFirstName());
			updateBuilder.updateColumnValue("lastName", profile.getLastName());
			updateBuilder.updateColumnValue("phone", profile.getPhone());
			updateBuilder.updateColumnValue("email", profile.getEmail());
			updateBuilder.updateColumnValue("photoUrl", profile.getPhotoUrl());
			updateBuilder.updateColumnValue("uid", profile.getUid());
			updateBuilder.where().eq("uid", profile.getUid());
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updateEmail(int id, String email){
		try {
			Dao<Profile, Integer> dao = getNewsItemDao();
			UpdateBuilder<Profile, Integer> updateBuilder = dao.updateBuilder();
			updateBuilder.updateColumnValue("email", email);
			updateBuilder.where().eq("uid", id);
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int addOrder(Order order) {
		try {
			Dao<Order, Integer> orderDao = getOrderDao();
			int updated = orderDao.create(order);
			return updated;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Order getOrderById(int id) {
		try {
			List<Order> orders = getOrderDao().queryForEq("orderId", id);
			if(orders != null && !orders.isEmpty())
				return orders.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteAllOrders() {
		try {
			Dao<Order, Integer> dao = getOrderDao();
			DeleteBuilder<Order, Integer> db = dao.deleteBuilder();
			dao.delete(db.prepare());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void deleteAllTraces() {
		try {
			Dao<GeoTrace, Integer> dao = getGeoTraceDao();
			DeleteBuilder<GeoTrace, Integer> db = dao.deleteBuilder();
			dao.delete(db.prepare());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public int addGeoTrace(GeoTrace trace) {
		try {
			Dao<GeoTrace, Integer> traceDao = getGeoTraceDao();
			int updated = traceDao.create(trace);
			return updated;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}*/

	@Override
	public void close() {
		super.close();
		profileDao = null;
	}

}
