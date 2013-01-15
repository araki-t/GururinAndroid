package jp.co.ricoh;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GLView extends GLSurfaceView{

	  GLRenderer glRenderer;
	  
	  public GLView(Context context) {
	    super(context);
	    glRenderer = new GLRenderer();
	    setRenderer(glRenderer);
	  }
}
