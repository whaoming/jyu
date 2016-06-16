package com.jyu.view.activity;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jyu.R;
import com.jyu.dao.impl.NumberDaoImpl;
import com.jyu.domain.MyNumber;
import com.jyu.view.base.BaseActivity;


public class PhoneActivity extends BaseActivity {
//	private Spinner spinner1;
//	private Spinner spinner2;

//	private ArrayAdapter<String> arr_adapter;
//	private ArrayAdapter<String> arr_adapter2;

	private ListView listview;
	
	public MyAdapter adapter;

	String[] l1 = { "学校领导", "行政党群部门", "教学系室", "公共教研室", "教辅单位", "江南校区",
			"亮湖楼招待所，校内餐厅" };

	final String[][] l2 = {
			{ "党委，校长办公室" },
			{ " 党委、校长办公室 ", "组织部人事处", "宣传统战部", "学生工作处", "监察审计处", "外事处", "教务处",
					"设备与实验室管理处", "科研处", "财务处", "后勤管理处", "保卫处", "工会", "团委",
					"成人教育处", "成人教育处(江南校区)" },
			{ "数学学院", "物理与光信息科技学院", "化学与环境学院", "文学院(客家学院)", "外国语学院", "政法学院",
					"地理科学与旅游学院", "生命科学学院", "经济与管理学院", "电子信息工程学院", "计算机学院",
					"美术学院", "土木工程系", "体育学院", "音乐学院", "教育科学学院" },
			{ "社科部", "师能教研室" }, { "图书馆", "教育技术中心、信息网络中心", "客家研究所", "学  报" },
			{ "--------" }, { "--------" } };
	private List<MyNumber> list;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_phone);
		listview = (ListView) findViewById(R.id.listview);
//		spinner1 = (Spinner) findViewById(R.id.spinner1);
//		spinner2 = (Spinner) findViewById(R.id.spinner2);
		// 适配器
//		arr_adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, l1);
		// 设置样式
//		arr_adapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//		// 加载适配器
//		spinner1.setAdapter(arr_adapter);
//		// 适配器
//		arr_adapter2 = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, l2[0]);
//		// 设置样式
//		arr_adapter2
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		// 加载适配器
//		spinner2.setAdapter(arr_adapter2);
//
//		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				arr_adapter2 = new ArrayAdapter<String>(PhoneActivity.this,
//						android.R.layout.simple_spinner_item, l2[position]);
//				spinner2.setAdapter(arr_adapter2);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				String one = spinner1.getSelectedItem().toString();
//				String second = spinner2.getSelectedItem().toString();
//				processData(one, second);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//
//			}
//		});
		
	}

	@SuppressWarnings("static-access")
	protected void processData(String one, String second) {
		NumberDaoImpl impl = new NumberDaoImpl();
		list = impl.query(one, second);
		if(adapter==null && list!=null){
			adapter = new MyAdapter();
			listview.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void processClick(View v) {

	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			MyNumber number = list.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View
						.inflate(ct, R.layout.item_phone, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.num = (TextView) convertView.findViewById(R.id.num);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(number.getThird());
			holder.num.setText(number.getNum().toString());
			return convertView;
		}

		class ViewHolder {
			TextView name;
			TextView num;
		}

	}

}
