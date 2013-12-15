package by.rss.kr;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

//Run this util if table was added or db entities was changed.
public class DBConfigUtil extends OrmLiteConfigUtil {
	
	private static final Class<?>[] classes = new Class[] {
		NewsItem.class
	  };
	
	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("ormlite_config.txt", classes);
	}

}