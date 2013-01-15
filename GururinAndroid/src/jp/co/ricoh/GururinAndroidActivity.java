package jp.co.ricoh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class GururinAndroidActivity extends Activity implements DialogInterface.OnClickListener,OnItemClickListener,Runnable{
	GLView glView;
	private int cur[];
	private boolean curDown;
	private final int SELECT_VIEW_REQUEST_CODE = 111;
	private final int SELECT_PICTURE_REQUEST_CODE = 222;
	private ProgressDialog dialog;
	
	private Thread thread;
	private Runnable runnable;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);   
        cur = new int[2];
        glView = new GLView(this);
		setContentView(glView);
		/*new Thread(this).start();
		dialog = new ProgressDialog(this);  
		dialog.setMessage("読み込みを行います。");  
		dialog.show();  */
		ProgressDialog dialog;
		dialog = new ProgressDialog(this);  
		dialog.setMessage("読み込みを行います。");  
		dialog.show();
		for(int i =0 ; i < 100 ; i++){     
        	try{   
        	Thread.sleep(100);   
        	}catch(Exception e){}   
        	dialog.incrementProgressBy(1);   
        	}   
        	dialog.dismiss();   
        
     }
    private void startProgress(){   
    	dialog = new ProgressDialog(this);   
    	dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);   
    	dialog.setTitle(R.string.app_name);   
    	dialog.setMessage(getString(R.string.hello));   
    	dialog.setCancelable(false);   
    	dialog.setMax(100);   
    	dialog.setProgress(0);   
    	dialog.show();     
    	} 


    @Override
    protected void onResume(){
      super.onResume();
      glView.onResume();
    }
    public boolean onTouchEvent(MotionEvent event){
  	  int action = event.getAction(); 
  	  int curNow[] = new int[2];
  	  curNow[0] = (int)event.getX();            
  	  curNow[1] = (int)event.getY();            
  	  if(action == MotionEvent.ACTION_DOWN){                              
  		  curDown = true;            
  	}else if(action == MotionEvent.ACTION_UP){                
  		curDown = false;            
  	}else if(curDown && action == MotionEvent.ACTION_MOVE){ 
  		glView.glRenderer.onTouchMotion(cur, curNow);
  		//glView.glRenderer.xrot +=(-curX+curNowX)/5;  
  		//glView.glRenderer.yrot +=(-curY+curNowY)/5; 
  	}
  	cur[0]=curNow[0];
  	cur[1]=curNow[1];
  	  return true;
    }
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);
		menu.add(0 , Menu.FIRST , Menu.NONE , "画像の選択");
		menu.add(0 , Menu.FIRST + 1, Menu.NONE , "表示方法変更");
		menu.add(0 , Menu.FIRST + 2 ,Menu.NONE , "閉じる");
		return ret;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println(item.getGroupId());
		System.out.println(item.getItemId());
		if (item.getItemId() == Menu.FIRST){
	        Intent intent = new Intent();  
	        intent.setClassName(  
	            "jp.co.ricoh",
	            "jp.co.ricoh.SelectPicture");  
	        intent.putExtra("strFile",glView.glRenderer.strFile );
	        startActivityForResult(intent,SELECT_PICTURE_REQUEST_CODE);
	    }else if (item.getItemId() == Menu.FIRST + 1){
	        Intent intent = new Intent();  
	        intent.setClassName(  
	            "jp.co.ricoh",
	            "jp.co.ricoh.SelectView");
	        intent.putExtra("keyword",glView.glRenderer.renderMode );
	        startActivityForResult(intent,SELECT_VIEW_REQUEST_CODE);

	    }
		return super.onOptionsItemSelected(item);
	}
    @Override
    protected void onPause(){
      super.onPause();
      glView.onPause();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == SELECT_VIEW_REQUEST_CODE){
    		if(resultCode == RESULT_OK){
    			glView.glRenderer.renderMode=Integer.parseInt(data.getCharSequenceExtra("keyword").toString());
    		}
    	}
    	if (requestCode == SELECT_PICTURE_REQUEST_CODE){
    		if(resultCode == RESULT_OK){
    			glView.glRenderer.strFile=data.getCharSequenceExtra("file").toString();
    		}
    	}
    }
	@Override
	public void run() {
		boolean tmp = true;
		dialog.dismiss();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ
		
	}  

}