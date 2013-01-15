package jp.co.ricoh;

import javax.microedition.khronos.opengles.GL10;



public class Rotater {
	double rotateAxis[][];
	double rotX,rotY;
	public Rotater() {
		rotateAxis = new double[3][3];
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(i==j){
					rotateAxis[i][j]=1.0;
					
				}else{
					rotateAxis[i][j]=0.0;
				}
			}
		}	
		rotX=0.0;rotY=0.0;
	}
	public double vectorCos(double v1[], double v2[]) {
	    double cs = v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
	    double stock = (v1[0] * v1[0] + v1[1] * v1[1] + v1[2] * v1[2]) * (v2[0] * v2[0] + v2[1] * v2[1] + v2[2] * v2[2]);
	    if (stock > 0) {
	        cs /= Math.sqrt(stock); // 内積からもとめた cosθ
	    }
	    return cs;
	}
	public void	vectorExterior(double v1[], double v2[], double v3[]) {
	    v3[0] = v1[1] * v2[2] - v1[2] * v2[1]; // 外積ベクトル
	    v3[1] = v1[2] * v2[0] - v1[0] * v2[2];
	    v3[2] = v1[0] * v2[1] - v1[1] * v2[0];
		double r = v3[0]*v3[0] + v3[1]*v3[1] + v3[2]*v3[2];
		if (r > 0) {
	        v3[0] = v3[0] / Math.sqrt(r);
	        v3[1] = v3[1] / Math.sqrt(r);
	        v3[2] = v3[2] / Math.sqrt(r);
	    }
	}
	public double vectorSize(double v1[]) {
	    double cs = v1[0] * v1[0] + v1[1] * v1[1] + v1[2] * v1[2];
	    return Math.sqrt(cs);
	}
	public void	vectorRotation(double v1[], double ax[], double c) {
	    // v1 回転対象のベクトル ; ax 軸 ; c 角度（cos）
		if(vectorSize(ax)!=0.0){
			double s = Math.sqrt(1-c*c);
			double v2[]=new double[3];
			v2[0] = v1[0] * (c + (1-c) * ax[0] * ax[0])  +  v1[1] * ((1-c) * ax[0] * ax[1] - ax[2] * s)  +  v1[2] * ((1-c) * ax[0] * ax[2] + ax[1]  *  s);
			v2[1] = v1[0] * ((1-c) * ax[0] * ax[1] + ax[2] * s)  +  v1[1] * (c + (1-c) * ax[1] * ax[1])  +  v1[2] * ((1-c) * ax[1] * ax[2] - ax[0] * s);
			v2[2] = v1[0] * ((1-c) * ax[0] * ax[2] - ax[1] * s)  +  v1[1] * ((1-c) * ax[1] * ax[2] + ax[0] * s)  +  v1[2] * (c + (1-c) * ax[2] * ax[2]);
			for (int i=0; i < 3; i++) {
				v1[i] = v2[i];
			}
		}else{
			if(c==-1.0){
				for (int i=0; i < 3; i++) {
					v1[i] = -v1[i];
				}
			}

		}
	}
	public double vectorNormalize(double v1[]) {
		double r = Math.sqrt(v1[0]*v1[0] + v1[1]*v1[1] + v1[2]*v1[2]);
		if (r > 0) {
			v1[0] = v1[0] / r;
			v1[1] = v1[1] / r;
			v1[2] = v1[2] / r;
		}
		return r;
	}
	public void renderRotate(GL10 gl){
		double v1[]=new double[3];
		double v2[]=new double[3];
		double axis[][]=new double[2][3];
		double cs[]=new double[2];
		for(int i=0;i<3;i++){
			v1[i]=0.0;
			v2[i]=0.0;
			axis[0][i]=0.0;
			axis[1][i]=0.0;
		}
		v1[0]=1.0;
		cs[0]=vectorCos(rotateAxis[0],v1);
		vectorExterior(v1,rotateAxis[0],axis[0]);
		for(int i=0;i<3;i++){
			v1[i]=rotateAxis[1][i];
			axis[1][i]=-axis[0][i];
		}
		vectorRotation(v1,axis[1],cs[0]);

		for(int i=0;i<3;i++){
			v2[i]=0.0;
		}
		v2[1]=1.0;
		cs[1]=vectorCos(v1,v2);
		vectorExterior(v1,v2,axis[1]);
		cs[1]=Math.acos(cs[1]);
		v1[0]=1.0;v1[1]=0.0;v1[2]=0.0;
		vectorRotation(v1,axis[0],cs[0]);
		if(axis[1][0]>0.0){
			for(int i=0;i<3;i++){
				axis[1][i]=-v1[i];
			}
		}else{
			for(int i=0;i<3;i++){
				axis[1][i]=v1[i];
			}
		}
		cs[0]=Math.acos(cs[0]);

		if(cs[1]!=0.0){
			if(vectorSize(axis[1])>0.0){
				if(cs[1]>0){
					gl.glRotatef((float)(cs[1]/Math.PI*180),(float)(axis[1][0]),(float)(axis[1][1]),(float)(axis[1][2]));
				}else{
					gl.glRotatef((float)(-cs[1]/Math.PI*180),(float)(-axis[1][0]),(float)(-axis[1][1]),(float)(-axis[1][2]));
				}
			}
		}
		if(cs[0]!=0.0){
			if(vectorSize(axis[0])>0.0){
				if(cs[0]>0){
					gl.glRotatef((float)(cs[0]/Math.PI*180),(float)(axis[0][0]),(float)(axis[0][1]),(float)(axis[0][2]));
				}else{
					gl.glRotatef((float)(-cs[0]/Math.PI*180),(float)(-axis[0][0]),(float)(-axis[0][1]),(float)(-axis[0][2]));
				}
			}
		}
	}
	public void rotate(double from[],double to[]){
		vectorNormalize(from);
		vectorNormalize(to);

		double axis[]=new double[3];
		vectorExterior(from,to,axis);
		double cs=vectorCos(from,to);

		if(vectorSize(axis)>0.0){
			for(int i=0;i<3;i++){
				vectorRotation(rotateAxis[i],axis,cs);
			}
		}
		
	}
	public void renderRotateLock(GL10 gl){
		gl.glPushMatrix();
		if(rotX>0){
			gl.glRotatef((float)rotX,0.0f,-1.0f,0.0f);
		}else{
			gl.glRotatef(-(float)rotX,0.0f,1.0f,0.0f);
		}
		gl.glPushMatrix();
		if(rotY>0){
			gl.glRotatef((float)rotY,0.0f,0.0f,-1.0f);
		}else{
			gl.glRotatef(-(float)rotY,0.0f,0.0f,1.0f);
		}
	}
}
