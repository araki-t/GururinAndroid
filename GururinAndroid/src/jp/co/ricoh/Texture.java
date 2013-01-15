package jp.co.ricoh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Texture {
	public int nWidth=1;
	public int nHeight =1;
	public int nTexture=512;
	public int tWidth=1024;
	public int tHeight=1024;
	public int texId[];
	public int slices=18;		//z
	public int stacks=18;
	public FloatBuffer faceBuff;
	public FloatBuffer texBuff;
	Bitmap ��extureBmp[][];
	public Texture() {
		
	}
	public void createTexturePicture(String strFile){
		BitmapFactory.Options	options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(strFile,options);
		
		//�e�N�X�`���̖��������߂�
		int	width = options.outWidth;
		int	height = options.outHeight;
		nWidth = (int)Math.ceil((double)width/nTexture);
		nHeight= (int)Math.ceil((double)height/nTexture);
		
		//�e�N�X�`��������̑傫�������߂�B������ӂ�Ō덷���ł���
		tWidth = width/nWidth; 
		tHeight = height/nHeight;
		
		//�e�N�X�`���摜�̓ǂݍ���
		options.inJustDecodeBounds = false;
		Bitmap	bmpImage = BitmapFactory.decodeFile(strFile,options);
		
		��extureBmp = new Bitmap[nWidth][nHeight];
		
		int initpixels[] = new int[nTexture * nTexture];
		for(int i=0;i<nWidth;i++){
			for(int j=0;j<nHeight;j++){
				��extureBmp[i][j]=Bitmap.createBitmap(nTexture, nTexture,Bitmap.Config.ARGB_8888);				
				//�e��e�N�X�`���摜���쐬
				for(int x=0;x<tWidth;x++){
					for(int y=0;y<tHeight;y++){
						if(i*tWidth+x < bmpImage.getWidth() && j*tHeight+y < bmpImage.getHeight()){
							int pix = bmpImage.getPixel(i*tWidth+x,j*tHeight+y);
							initpixels[x+y*nTexture]=pix;
						}
					}
				}
				��extureBmp[i][j].setPixels(initpixels, 0, nTexture, 0, 0, nTexture, nTexture);					
			}
		}
		bmpImage.recycle();
	}
	public void setTexture(GL10 gl){
		//�e�N�X�`��ID�̍쐬
		texId = new int[nWidth*nHeight];
		gl.glGenTextures(nWidth*nHeight, texId, 0);
		for(int i=0;i<nWidth;i++){
			for(int j=0;j<nHeight;j++){
				//�e�N�X�`���̓o�^
				gl.glBindTexture(GL10.GL_TEXTURE_2D, texId[i*nHeight+j]);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, ��extureBmp[i][j], 0);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);	//�@�k������Ƃ��s�N�Z���̒��S�ɍł��߂��e�N�X�`���v�f�ŕ⊮       
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST);	//�@�g�傷��Ƃ��s�N�Z���̒��S�t�߂̐��`�ŕ⊮      
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_REPEAT);	//�@s���W��1�𒴂���[���������[�v�ɂ��Ȃ�      
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_REPEAT);	//�@t���W��1�𒴂���[���������[�v�ɂ��Ȃ�       
				gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,GL10.GL_REPLACE);	//�@�e�N�X�`���ƃ��f���̍������@�̎w��i���̏ꍇ�u�������j
				��extureBmp[i][j].recycle();
			}
		}
		
	}
	public void createTexture(GL10 gl,String strFile){
		BitmapFactory.Options	options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(strFile,options);
		
		//�e�N�X�`���̖��������߂�
		int	width = options.outWidth;
		int	height = options.outHeight;
		nWidth = (int)Math.ceil((double)width/nTexture);
		nHeight= (int)Math.ceil((double)height/nTexture);
		
		//�e�N�X�`��������̑傫�������߂�B������ӂ�Ō덷���ł���
		tWidth = width/nWidth; 
		tHeight = height/nHeight;
		
		//�e�N�X�`���摜�̓ǂݍ���
		options.inJustDecodeBounds = false;
		Bitmap	bmpImage = BitmapFactory.decodeFile(strFile,options);
		
		//�e�N�X�`��ID�̍쐬
		texId = new int[nWidth*nHeight];
		gl.glGenTextures(nWidth*nHeight, texId, 0);

		Bitmap ��extureBmp=Bitmap.createBitmap(nTexture, nTexture,Bitmap.Config.ARGB_8888);
		int initpixels[] = new int[nTexture * nTexture];
		for(int i=0;i<nWidth;i++){
			for(int j=0;j<nHeight;j++){
				
				//�e��e�N�X�`���摜���쐬
				for(int x=0;x<tWidth;x++){
					for(int y=0;y<tHeight;y++){
						if(i*tWidth+x < bmpImage.getWidth() && j*tHeight+y < bmpImage.getHeight()){
							int pix = bmpImage.getPixel(i*tWidth+x,j*tHeight+y);
							initpixels[x+y*nTexture]=pix;
						}
					}
				}
				��extureBmp.setPixels(initpixels, 0, nTexture, 0, 0, nTexture, nTexture);	
				
				//�e�N�X�`���̓o�^
				gl.glBindTexture(GL10.GL_TEXTURE_2D, texId[i*nHeight+j]);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, ��extureBmp, 0);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);	//�@�k������Ƃ��s�N�Z���̒��S�ɍł��߂��e�N�X�`���v�f�ŕ⊮       
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST);	//�@�g�傷��Ƃ��s�N�Z���̒��S�t�߂̐��`�ŕ⊮      
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_REPEAT);	//�@s���W��1�𒴂���[���������[�v�ɂ��Ȃ�      
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_REPEAT);	//�@t���W��1�𒴂���[���������[�v�ɂ��Ȃ�       
				gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,GL10.GL_REPLACE);	//�@�e�N�X�`���ƃ��f���̍������@�̎w��i���̏ꍇ�u�������j
			}
		}
			
		//�e��e�N�X�`���摜���쐬
		/*Bitmap[] ��extureBmp;
		��extureBmp=new Bitmap[nWidth*nHeight]; 		
		for(int i=0;i<nWidth*nHeight;i++){
			��extureBmp[i]=Bitmap.createBitmap(nTexture, nTexture,Bitmap.Config.ARGB_8888);
		}
		for(int i=0;i<nWidth;i++){
			for(int j=0;j<nHeight;j++){
				int initpixels[] = new int[nTexture * nTexture];
				for(int x=0;x<tWidth;x++){
					for(int y=0;y<tHeight;y++){
						if(i*tWidth+x < bmpImage.getWidth() && j*tHeight+y < bmpImage.getHeight()){
							int pix = bmpImage.getPixel(i*tWidth+x,j*tHeight+y);
							initpixels[x+y*nTexture]=pix;
						}
					}
				}
				��extureBmp[i*nHeight+j].setPixels(initpixels, 0, nTexture, 0, 0, nTexture, nTexture);				
			}
		}
			
		
		//�e�N�X�`���̓o�^
		for(int i=0;i<nWidth;i++){
			for(int j=0;j<nHeight;j++){
				gl.glBindTexture(GL10.GL_TEXTURE_2D, texId[i*nHeight+j]);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, ��extureBmp[i*nHeight+j], 0);
			}
		}*/

	}
	public void createBallFaces(double _r){

		float ballFace[] = new float[nWidth*nHeight*slices*stacks*3*4];
		float ballTex[] = new float[nWidth*nHeight*slices*stacks*2*4];

		for(int i=0;i<nWidth;i++){
			for(int j=0;j<nHeight;j++){
				for(int k=0;k<slices;k++){
					for(int l=0;l<stacks;l++){
						int faseBasePointa=(i*nHeight+j)*slices*stacks*4*3+(k*stacks+l)*4*3;
						ballFace[faseBasePointa+(0*3)+0]=(float)(_r*Math.cos(-Math.PI/2+Math.PI/slices/nHeight*(k+0.0)+Math.PI/nHeight*j)*Math.sin(2*Math.PI/stacks/nWidth*(l+0.0)+2*Math.PI/nWidth*i));
						ballFace[faseBasePointa+(0*3)+1]=(float)(_r*Math.cos(-Math.PI/2+Math.PI/slices/nHeight*(k+0.0)+Math.PI/nHeight*j)*Math.cos(2*Math.PI/stacks/nWidth*(l+0.0)+2*Math.PI/nWidth*i));
						ballFace[faseBasePointa+(0*3)+2]=(float)(_r*Math.sin(-Math.PI/2+Math.PI/slices/nHeight*(k+0.0)+Math.PI/nHeight*j));

						ballFace[faseBasePointa+(1*3)+0]=(float)(_r*Math.cos(-Math.PI/2+Math.PI/slices/nHeight*(k+0.0)+Math.PI/nHeight*j)*Math.sin(2*Math.PI/stacks/nWidth*(l+1.0)+2*Math.PI/nWidth*i));
						ballFace[faseBasePointa+(1*3)+1]=(float)(_r*Math.cos(-Math.PI/2+Math.PI/slices/nHeight*(k+0.0)+Math.PI/nHeight*j)*Math.cos(2*Math.PI/stacks/nWidth*(l+1.0)+2*Math.PI/nWidth*i));
						ballFace[faseBasePointa+(1*3)+2]=(float)(_r*Math.sin(-Math.PI/2+Math.PI/slices/nHeight*(k+0.0)+Math.PI/nHeight*j));

						ballFace[faseBasePointa+(2*3)+0]=(float)(_r*Math.cos(-Math.PI/2+Math.PI/slices/nHeight*(k+1.0)+Math.PI/nHeight*j)*Math.sin(2*Math.PI/stacks/nWidth*(l+1.0)+2*Math.PI/nWidth*i));
						ballFace[faseBasePointa+(2*3)+1]=(float)(_r*Math.cos(-Math.PI/2+Math.PI/slices/nHeight*(k+1.0)+Math.PI/nHeight*j)*Math.cos(2*Math.PI/stacks/nWidth*(l+1.0)+2*Math.PI/nWidth*i));
						ballFace[faseBasePointa+(2*3)+2]=(float)(_r*Math.sin(-Math.PI/2+Math.PI/slices/nHeight*(k+1.0)+Math.PI/nHeight*j));

						ballFace[faseBasePointa+(3*3)+0]=(float)(_r*Math.cos(-Math.PI/2+Math.PI/slices/nHeight*(k+1.0)+Math.PI/nHeight*j)*Math.sin(2*Math.PI/stacks/nWidth*(l+0.0)+2*Math.PI/nWidth*i));
						ballFace[faseBasePointa+(3*3)+1]=(float)(_r*Math.cos(-Math.PI/2+Math.PI/slices/nHeight*(k+1.0)+Math.PI/nHeight*j)*Math.cos(2*Math.PI/stacks/nWidth*(l+0.0)+2*Math.PI/nWidth*i));
						ballFace[faseBasePointa+(3*3)+2]=(float)(_r*Math.sin(-Math.PI/2+Math.PI/slices/nHeight*(k+1.0)+Math.PI/nHeight*j));
						
						int texBasePointa=(i*nHeight+j)*slices*stacks*4*2+(k*stacks+l)*4*2;
						ballTex[texBasePointa+(0*2)+0]=(float)((double)(tWidth)/nTexture/(stacks+0.0)*(l+0.0));
						ballTex[texBasePointa+(0*2)+1]=(float)((double)(tHeight)/nTexture/(slices+0.0)*(k+0.0));

						ballTex[texBasePointa+(1*2)+0]=(float)((double)(tWidth)/nTexture/(stacks+0.0)*(l+1.0));
						ballTex[texBasePointa+(1*2)+1]=(float)((double)(tHeight)/nTexture/(slices+0.0)*(k+0.0));

						ballTex[texBasePointa+(2*2)+0]=(float)((double)(tWidth)/nTexture/(stacks+0.0)*(l+1.0));
						ballTex[texBasePointa+(2*2)+1]=(float)((double)(tHeight)/nTexture/(slices+0.0)*(k+1.0));

						ballTex[texBasePointa+(3*2)+0]=(float)((double)(tWidth)/nTexture/(stacks+0.0)*(l+0.0));
						ballTex[texBasePointa+(3*2)+1]=(float)((double)(tHeight)/nTexture/(slices+0.0)*(k+1.0));
					}				
				}				
			}
		}
		faceBuff = makeFloatBuffer(ballFace);
		texBuff = makeFloatBuffer(ballTex);
	}
	public void drawFaces(GL10 gl){
		gl.glActiveTexture( GL10.GL_TEXTURE0 );
		for(int k=0;k<nWidth;k++){
			for(int l=0;l<nHeight;l++){
				gl.glBindTexture(GL10.GL_TEXTURE_2D, texId[k*nHeight+l]);
				int base=(k*nHeight+l)*slices*stacks*4;
				for(int i=0;i<slices*stacks;i++){
					gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, base+4*i, 4);
				}
			}
		}
	}
	
	protected static FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}



}
