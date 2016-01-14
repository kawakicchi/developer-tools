package com.github.kawakicchi.developer.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreateRule;
import org.apache.commons.digester.SetNextRule;
import org.apache.commons.digester.SetPropertiesRule;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.xml.sax.SAXException;

public class DatabaseManager {

	private static final DatabaseManager INSTANCE = new DatabaseManager();

	private List<Datasource> datasourceList;
	private Map<String, Datasource> datasourceMap;

	private DatabaseManager() {
		datasourceMap = new HashMap<String, Datasource>();
		datasourceList = new ArrayList<Datasource>();
	}

	public static DatabaseManager getInstance() {
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public synchronized void load(final File file) {
		ArrayList<DatabaseEntity> databases = null;
		try {
			Digester digester = getDatabaseDigester();
			databases = (ArrayList<DatabaseEntity>) digester.parse(file);
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		synchronized (this.datasourceMap) {
			this.datasourceMap.clear();
			this.datasourceList.clear();
			for (DatabaseEntity database : databases) {
				Datasource ds = createDatasource(database);
				this.datasourceMap.put(database.getTitle(), ds);
				this.datasourceList.add(ds);
			}
		}
	}
	
	private Datasource createDatasource(final DatabaseEntity entity) {
		PoolProperties p = new PoolProperties();

		// 接続情報の設定
		p.setUrl(entity.getUrl());
		p.setDriverClassName(entity.getDriver());
		p.setUsername(entity.getUser());
		p.setPassword(entity.getPassword());

		// その他オプション
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);

		p.setInitialSize(1); // プールの起動時に作成されるコネクションの初期サイズ
		p.setMinIdle(1);
		p.setMaxActive(10); // 同時にプールから割り当てることのできるアクティブなコネクションの最大数
		p.setMaxIdle(10);
		
		p.setMaxWait(10 * 1000); // 利用可能なコネクションが存在しないときに待機する最大時間（ミリ秒単位）

		p.setRemoveAbandoned(false); // クローズ漏れコネクションの検知を行う場合はtrueに設定
		p.setRemoveAbandonedTimeout(60);

		p.setMinEvictableIdleTimeMillis(30000);
		p.setLogAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

		DataSource datasource = new DataSource();
		datasource.setPoolProperties(p);

		return new Datasource(entity, datasource);
	}
	
	public static Datasource getDatasource(final String name) {
		return INSTANCE.datasourceMap.get(name);
	}
	
	private Digester getDatabaseDigester() {
		Digester digester = new Digester();
		digester.addRule("database/database-list", new ObjectCreateRule(ArrayList.class));
		digester.addRule("database/database-list/database", new ObjectCreateRule(DatabaseEntity.class));
		digester.addRule("database/database-list/database", new SetPropertiesRule("title", "title"));
		digester.addRule("database/database-list/database/url", new BeanPropertySetterRule("url"));
		digester.addRule("database/database-list/database/driver", new BeanPropertySetterRule("driver"));
		digester.addRule("database/database-list/database/user", new BeanPropertySetterRule("user"));
		digester.addRule("database/database-list/database/password", new BeanPropertySetterRule("password"));
		digester.addRule("database/database-list/database", new SetNextRule("add"));
		return digester;
	}
}
