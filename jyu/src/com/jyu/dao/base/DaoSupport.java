package com.jyu.dao.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jyu.dao.DBHelper;
import com.jyu.dao.annotation.Column;
import com.jyu.dao.annotation.ID;
import com.jyu.dao.annotation.TableName;


/**
 * 数据库操作实现类的公共部分
 * 
 * @author Administrator
 * 
 * @param <M>
 */
public abstract class DaoSupport<M> implements DAO<M> {

	@SuppressWarnings("unused")
	private static final String TAG = "DaoSupport";
	protected DBHelper helper;
	protected Context context;
	protected SQLiteDatabase db;

	public DaoSupport(Context context) {
		super();
		this.context = context;
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	@Override
	public long insert(M m) {
		ContentValues values = new ContentValues();
		fillContentValues(m, values);
		return db.insert(getTableName(), null, values);
	}

	@Override
	public int delete(Serializable id) {
		return db.delete(getTableName(), DBHelper.TABLE_ID + "=?",
				new String[] { id.toString() });
	}

	@Override
	public int updata(M m) {
		ContentValues values = new ContentValues();
		fillContentValues(m, values);
		return db.update(getTableName(), values, DBHelper.TABLE_ID + "=?",
				new String[] { getId(m) });
	}

	public List<M> findByCondition(String selection, String[] selectionArgs,
			String orderBy) {
		return findByCondition(null, selection, selectionArgs, null, null,
				orderBy);
	}

	public List<M> findByCondition(String[] columns, String selection,
			String[] selectionArgs, String orderBy) {
		return findByCondition(columns, selection, selectionArgs, null, null,
				orderBy);
	}

	public List<M> findByCondition(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {

		List<M> result;
		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), columns, selection,
					selectionArgs, groupBy, having, orderBy);
			if (cursor != null) {
				result = new ArrayList<M>();
				while (cursor.moveToNext()) {
					M m = getInstance();
					fillEntity(cursor, m);
					// 此处省略3*n行代码
					result.add(m);
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return null;

	}

	@Override
	public List<M> findAll() {
		List<M> result;
		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), null, null, null, null, null,
					null);
			if (cursor != null) {
				result = new ArrayList<M>();
				while (cursor.moveToNext()) {
					M m = getInstance();
					fillEntity(cursor, m);
					// 此处省略3*n行代码
					result.add(m);
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return null;
	}

	/******************************************************/
	// 通用化的问题清单
	// 问题一：表名获取
	// 问题二：需要将数据源News添加到目标ContentValues身上
	// 问题三：将数据源Cursor数据添加目标News身上
	// 问题四：News主键是谁
	// 问题五：当前操作的实体创建

	/**
	 * 问题一：表名获取
	 * 
	 * @return
	 */
	private String getTableName() {
		// M--News
		// 实体与数据库中表一一对应
		// 方案一：获取实体的简单名称“News”，缺少灵活性
		// 方案二：利用注解，指定实体对应的数据库表

		// 步骤：
		// ①获取到实体
		M m = getInstance();
		// ②获取到类上的注解
		TableName tableName = m.getClass().getAnnotation(TableName.class);
		// ③获取到的注解中存放的数据
		if (tableName != null) {
			return tableName.value();
		}
		return "";
	}

	/**
	 * 问题二：需要将数据源News添加到目标ContentValues身上
	 * 
	 * @param m
	 *            :数据源
	 * @param values
	 *            :目标
	 */
	private void fillContentValues(M m, ContentValues values) {
		// values.put(DBHelper.TABLE_NEWS_TITLE, news.getTitle());

		// 表中列与实体中字段一一对应关系
		// for all public fields
		// m.getClass().getFields();
		// objects for all fields
		Field[] fields = m.getClass().getDeclaredFields();
		for (Field item : fields) {
			// 如果是私有的字段
			item.setAccessible(true);

			Column column = item.getAnnotation(Column.class);
			if (column != null) {
				String key = column.value();

				try {
					// sqlite 存储 字符串 特殊：主键+自增
					ID id = item.getAnnotation(ID.class);
					if (id != null) {
						if (id.autoincrement()) {
							// 主键+自增 的情况不能设置数据
						} else {
							String value = item.get(m).toString();// m.title
							values.put(key, value);
						}
					} else {
						String value = item.get(m).toString();// m.title
						values.put(key, value);
					}

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 问题三：将数据源Cursor数据添加目标News身上
	 * 
	 * @param cursor
	 * @param m
	 */
	private void fillEntity(Cursor cursor, M m) {
		// int columnIndex = cursor.getColumnIndex(DBHelper.TABLE_NEWS_TITLE);
		// String title = cursor.getString(columnIndex);
		// news.setTitle(title);

		Field[] fields = m.getClass().getDeclaredFields();
		for (Field item : fields) {
			item.setAccessible(true);
			Column column = item.getAnnotation(Column.class);
			if (column != null) {
				int columnIndex = cursor.getColumnIndex(column.value());
				// 主键+自增 int long
				String value = cursor.getString(columnIndex);
				try {

					if (item.getType() == int.class) {
						item.set(m, Integer.parseInt(value));
					} else if (item.getType() == long.class) {
						item.set(m, Long.parseLong(value));
					} else {
						item.set(m, value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * 问题四：News主键是谁
	 * 
	 * @param m
	 * @return
	 */
	private String getId(M m) {
		// 主键
		// 获取所有的Field，头上有ID注解，循环
		Field[] fields = m.getClass().getDeclaredFields();
		for (Field item : fields) {
			item.setAccessible(true);
			ID id = item.getAnnotation(ID.class);
			if (id != null) {
				try {
					return item.get(m).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 问题五：当前操作的实体创建
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private M getInstance() {
		// ①当前正在运行的孩子是谁
		// DaoSupport 子类操作
		// Object.getClass() 返回此 Object 的"运行时"类。
		// String name = super.getClass().getName();
		//
		// Log.i(TAG, name);

		// ②获取父类：getSuperClass
		// 支持泛型的父类
		// getClass().getSuperclass();//class cn.ithm.dbhm24.dao.base.DaoSupport
		Type superclass = getClass().getGenericSuperclass();// cn.ithm.dbhm24.dao.base.DaoSupport<cn.ithm.dbhm24.dao.domain.News>

		// JDK 让泛型实现一个接口：参数化的类型
		if (superclass instanceof ParameterizedType) {
			// ③获取到泛型中得参数
			Type[] arguments = ((ParameterizedType) superclass)
					.getActualTypeArguments();// 泛型的公共操作 [class
												// cn.ithm.dbhm24.dao.domain.News]
			@SuppressWarnings("rawtypes")
			Class target = (Class) arguments[0];
			try {
				// ④创建实体的对象
				return (M) target.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
