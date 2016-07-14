package com.stingray.stingrayandroid.bookshelf.xml;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.stingray.stingrayandroid.Constants;
import com.stingray.stingrayandroid.Model.School;
import com.stingray.stingrayandroid.Model.Yearbook;
import com.stingray.stingrayandroid.bookshelf.activity.CommonClass;
import com.stingray.stingrayandroid.bookshelf.activity.CommonUtils;
import com.stingray.stingrayandroid.bookshelf.adapter.BooksAdapter;
import com.stingray.stingrayandroid.bookshelf.view.BookshelfView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DownloadBooksTask extends AsyncTask<String, Void, List<Yearbook>> {

	private Context context;
	private BookshelfView view;
	private CommonClass mCommonClass;
	private String m_response = "";
	private List<Yearbook> bookData;
	public DownloadBooksTask(Context context, BookshelfView view) {
		super();
		this.context = context;
		this.view = view;
		mCommonClass = new CommonClass();
		bookData = new ArrayList<Yearbook>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected List<Yearbook> doInBackground(String... params) {
//		if ((params == null) || (params.length != 1)) {
//			throw new IllegalArgumentException("The URL is expected as a parameter.");
//		}

		/*
		 * InputStream is = null; try { is = download(params[0]); if (is !=
		 * null) { return (new BooksXmlParser()).parse(is); }
		 * 
		 * } catch (XmlPullParserException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace();
		 * 
		 * } finally { if (is != null) { try { is.close(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } } }
		 * return null;
		 */
		if (mCommonClass.CheckNetwork(context)) {
			m_response = CommonUtils
					.parseJSON("https://stingray-parwanta.rhcloud.com/api/Yearbooks");
			Log.v("DATA", "All Data ====== " + m_response);
		}
		try {
			JSONArray m_arr = new JSONArray(m_response);
			for (int i = 0; i < m_arr.length(); i++) {
				Log.e("TAG", "book id  = " + m_arr.getJSONObject(i).getString("id"));
				JSONObject m_jObj = m_arr.getJSONObject(i);
				Yearbook yearbook = new Yearbook();
				yearbook.setCoverUrl(Constants.BASE_URL+m_jObj.getString("cover_url"));
				yearbook.setEpubUrl(Constants.BASE_URL+m_jObj.getString("epub_url"));
				yearbook.setId(m_jObj.getString("id"));
				yearbook.setYear(m_jObj.getInt("year"));

				School school = new School();
				JSONObject schoolObj = m_jObj.getJSONObject("school");
				school.setAddress(schoolObj.getString("address"));
				school.setName(schoolObj.getString("name"));
				school.setPhone(schoolObj.getString("phone"));
				school.setId(schoolObj.getString("id"));

				yearbook.setSchool(school);
				bookData.add(yearbook);
			}
			System.err.println("Size of array====>"+m_arr.length());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bookData;
	}

	@Override
	protected void onPostExecute(List<Yearbook> result) {
		super.onPostExecute(result);

		if (result != null) {
			view.setAdapter(new BooksAdapter(context, result));
		} else {
			(Toast.makeText(context, "An error occured.", Toast.LENGTH_LONG)).show();
		}
	}

//	protected InputStream download(String urlString) throws IOException {
//		HttpURLConnection conn = (HttpURLConnection) (new URL(urlString)).openConnection();
//		conn.setReadTimeout(10000);
//		conn.setConnectTimeout(15000);
//		conn.setRequestMethod("GET");
//		conn.setDoInput(true);
//
//		conn.connect();
//		return conn.getInputStream();
//	}
}
