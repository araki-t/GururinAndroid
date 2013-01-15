package jp.co.ricoh;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
//import android.opengl.GLUtils;

import android.opengl.GLSurfaceView.Renderer;

public class GLRenderer implements Renderer{
	private final int RENDER_MODE_CENTER = 0;
	private final int RENDER_MODE_OUTER = 1;
	private final int RENDER_MODE_CENTER_FREE = 2;
	private final int RENDER_MODE_ANAGLYPH = 3;
	int renderMode=0;
	float xrot = 0.0f;
	float yrot = 0.0f;
	Rotater rotater;
	int displayWidth = 500;
	int displayHeight = 500;
	
	boolean initialize = true;
	String	strFile = "/sdcard/earthcloudmap.jpg";
	//String	strFile = "/sdcard/sample.jpg";
	
	Texture texture;	
	
	public GLRenderer() {
		texture = new Texture();
		rotater = new Rotater();
		//texture.createTexturePicture(strFile);
		//texture.createBallFaces(5.0);
		
	}
	public void onDrawFrame(GL10 gl) { 
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		if(renderMode == RENDER_MODE_CENTER || renderMode == RENDER_MODE_CENTER_FREE){
			GLU.gluLookAt(gl, 0, 0, 0, 0, 0, 15, 0, 1, 0);
		}else{
			GLU.gluLookAt(gl, 0, 0, 10, 0, 0, 0, 0, 1, 0);
		}
		
		rotater.renderRotate(gl);
		gl.glPushMatrix();
		if(renderMode != RENDER_MODE_CENTER_FREE){
			gl.glRotatef(90, 1, 0, 0);
		}
		
	
		gl.glColor4f(1.0f, 1, 1, 1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		texture.drawFaces(gl);
		gl.glPopMatrix();
		//xrot += 1.0f;
		//yrot += 0.5f;
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		displayWidth = width;
		displayHeight = height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();    
		GLU.gluPerspective(gl, 60f,(float) width / height, 1f, 50f);
	}
		
	public void init(GL10 gl){
		texture.createTexture(gl, strFile);
		texture.createBallFaces(5.0);
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
			
		gl.glEnable(GL10.GL_TEXTURE_2D);
			
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepthf(1.0f);
			
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, texture.faceBuff);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.texBuff);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				
		gl.glShadeModel(GL10.GL_SMOOTH);
	}
	public void onSurfaceCreated(GL10 gl, EGLConfig config){
		init(gl);
		
	}
	public void onTouchMotion(int lastCur[],int nowCur[]){

		double lastPos[]=new double[3];
		double nowPos[]=new double[3];
		
		convertTrackball(lastCur,lastPos);
		convertTrackball(nowCur,nowPos);

		rotater.rotate(lastPos, nowPos);
	}
	private void convertTrackball(int cur[],double pos[]){
		if(renderMode == RENDER_MODE_CENTER){
			pos[0] = ((displayWidth-cur[0]) * 2.0 / displayWidth - 1.0);
			pos[1] = ((displayHeight - cur[1]) * 2.0 / displayHeight - 1.0);
			
		}else if(renderMode == RENDER_MODE_OUTER){
			pos[0] = (cur[0] * 2.0 / displayWidth - 1.0);
			pos[1] = ((displayHeight - cur[1]) * 2.0 / displayHeight - 1.0);
			
		}else{
			pos[0] = (cur[0] * 2.0 / displayWidth - 1.0);
			pos[1] = ((cur[1]) * 2.0 / displayHeight - 1.0);
		}
		
		//pos[1] = ((cur[1]) * 2.0 / displayHeight - 1.0);
		pos[2] = projectToTrackball(1.0, pos[0], pos[1]);
	
		
	}
	private double projectToTrackball(double radius, double x, double y)
	{
	    double kUnitSphereRadius2D = Math.sqrt(2.0);
	    double z;
		radius=Math.sqrt(5.0);
	    double dist = Math.sqrt(x * x + y * y);
	    if (dist < radius )
	    {
	        // Solve for sphere case.
	        z = Math.sqrt(radius * radius - dist * dist);
	    }
	    else
	    {
	        // Solve for hyperbolic sheet case.
	        double t = radius / kUnitSphereRadius2D;
	        z = t * t / dist;
	    }

	    return z;
	}





}
		