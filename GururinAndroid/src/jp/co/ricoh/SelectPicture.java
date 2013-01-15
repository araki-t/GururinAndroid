package jp.co.ricoh;

import java.io.File;  
import java.util.ArrayList;  
import java.util.List;  
  
import android.app.Activity;  
import android.app.AlertDialog;  
import android.app.ProgressDialog;  
import android.content.ContentResolver;
import android.content.DialogInterface;  
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.net.Uri;
import android.os.Bundle;  
import android.os.Environment;  
import android.provider.MediaStore;
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.AdapterView;  
import android.widget.BaseAdapter;  
import android.widget.Gallery;  
import android.widget.ImageView;  
import android.widget.AdapterView.OnItemClickListener;  
import android.widget.LinearLayout;
  
public class SelectPicture extends Activity implements DialogInterface.OnClickListener,OnItemClickListener{  
 /** Called when the activity is first created. */  
  
	private ProgressDialog dialog;  
	private ImageView imageView;  
	private Gallery gallery;  
	private Bitmap[] thumbnail;  
	private List<String> dirList = new ArrayList<String>();  
	private List<String> tmp = new ArrayList<String>();  
	private List<String> path = new ArrayList<String>(); 


	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.selectpicture);  
		
		imageView = (ImageView)this.findViewById(R.id.imageView1);
		if(!sdcardReadReady()){
			new AlertDialog.Builder(this)  
		 		.setMessage("SDカードにアクセスできません。")  
		 		.setNeutralButton("OK", this)  
		 		.show();  
			return;  
		}
		ContentResolver cr = getContentResolver();
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Cursor c = this.managedQuery(uri, null, null, null, null);
		c.moveToFirst();
		thumbnail = new Bitmap[c.getCount()];
		for (int k = 0; k < c.getCount(); k++) {
			long id = c.getLong(c.getColumnIndexOrThrow("_id"));
			path.add(c.getString(c.getColumnIndexOrThrow("_data")));
			thumbnail[k] = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
			c.moveToNext();
		}


		if(path.isEmpty()){
			new AlertDialog.Builder(this)
				.setMessage("画像のデータがありません")  
				.setNeutralButton("OK", this)  
				.show();
			return;  
		}  

		gallery = (Gallery)findViewById(R.id.gallery1);  
		gallery.setSpacing(1);  
		gallery.setAdapter(new GalleryAdapter());  
		gallery.setOnItemClickListener(this); 
		
		Bundle extras=getIntent().getExtras();
		String strFile=  "";
		if(extras!=null){
			strFile = extras.getString("strFile");
			
		}
		
		Bitmap picture = LoadImageFile(strFile,256); 
		imageView.setImageBitmap(picture); 	
	}  
	private LinearLayout.LayoutParams createParam(int w, int h){
		return new LinearLayout.LayoutParams(w, h);
	}
	public	Bitmap	LoadImageFile(String strFile,int nMaxWidth)
	{
		Bitmap	bmpImage;
		
		//画像の解析（大きい画像が開けない対策）
		BitmapFactory.Options	options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(strFile,options);
		
		int		nWidth = options.outWidth;
		int		nHeight = options.outHeight;

		if(nWidth == 0 || nHeight == 0)
			return	null;

		if(nWidth <= nMaxWidth && nHeight <= nMaxWidth){
			//リサイズ不要。そのまま読み込んで返す
			options.inJustDecodeBounds = false;
			return	BitmapFactory.decodeFile(strFile,options);
		}

		int		nScale;
		{
			float	fScale;
			float	fScaleX = (float)nWidth / nMaxWidth;
			float	fScaleY = (float)nHeight / nMaxWidth;

			fScale = (fScaleX > fScaleY) ? fScaleX : fScaleY;

			nScale = 1;
			while(fScale >= nScale)
			{
				nScale *= 2;
			}
		}

		//画像の本読み込み
		options.inJustDecodeBounds = false;
		options.inSampleSize = nScale;
		bmpImage = BitmapFactory.decodeFile(strFile,options);

		return	bmpImage;
	}

  
	public void run(){  
		for(int i = 0 ; i < tmp.size(); i++){  
			BitmapFactory.Options options = new BitmapFactory.Options();  
			options.inJustDecodeBounds = true;  
			BitmapFactory.decodeFile(tmp.get(i),options);  
			int width = options.outWidth;  
			int scale = width /100;  
			options.inSampleSize = scale;  
			options.inJustDecodeBounds = false;  
			thumbnail[i] = LoadImageFile(tmp.get(i),256);
		}  
		dialog.dismiss();  
	}  
  
	private boolean sdcardReadReady(){  
		String state = Environment.getExternalStorageState();  
		return (Environment.MEDIA_MOUNTED.equals(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));  
	}  
  
	@Override  
	public void onClick(DialogInterface dialog, int which) {  
		switch(which){  
			case DialogInterface.BUTTON_NEUTRAL:  
				dialog.dismiss();  
				finish();  
		}  
	}  
  
	@Override  
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
		// TODO Auto-generated method stub  
		if(arg0 == gallery){  
			Bitmap picture =LoadImageFile(path.get(arg2),256); 
			imageView.setImageBitmap(picture);
			Intent data = new Intent();
			data.putExtra("file", path.get(arg2));
			setResult(RESULT_OK, data);
		}  
	}  
	public class GalleryAdapter extends BaseAdapter{  
  
		@Override  
		public int getCount() {  
			// TODO Auto-generated method stub  
			return path.size();  
		}  
  
		@Override  
		public Object getItem(int position) {  
			// TODO Auto-generated method stub  
			return null;  
		}  
  
		@Override  
		public long getItemId(int position) {  
			// TODO Auto-generated method stub  
			return 0;  
		}  
  
		@Override  
		public View getView(int position, View convertView, ViewGroup parent) {  
			// TODO Auto-generated method stub  
			ImageView view;  
			if(convertView == null){  
				view = new ImageView(SelectPicture.this);
				view.setImageBitmap(thumbnail[position]);  
				view.setScaleType(ImageView.ScaleType.FIT_CENTER);
				view.setLayoutParams(new Gallery.LayoutParams(100, 100));
			}else{  
				view = (ImageView)convertView;  
			}  
			return view;  
		}  
	}  
}  
