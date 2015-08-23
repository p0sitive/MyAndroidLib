package com.example.myandroidlib.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Bitmap与Drawable、InputStream相互转化
 * 
 * @author Administrator
 * 
 */
public class BitmapFormatUtil {

	private static BitmapFormatUtil bitmapUtil = new BitmapFormatUtil();

	public static BitmapFormatUtil getInstance() {
		if (bitmapUtil == null) {
			bitmapUtil = new BitmapFormatUtil();
		}
		return bitmapUtil;
	}

	/**
	 * byte[]转化为InputStream
	 * 
	 * @param b
	 * @return
	 */
	public InputStream Byte2InputStream(byte[] b) {
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		return bis;
	}

	/**
	 * InputStream转化为byte[]
	 * 
	 * @param is
	 * @return
	 */
	public byte[] InputStream2Byte(InputStream is) {
		String string = "";
		byte[] readByte = new byte[1024];
		int readCount = -1;
		try {
			while ((readCount = is.read(readByte, 0, 1024)) != -1) {
				string += new String(readByte).trim();
			}
			return string.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * bitmap转化为InputStream
	 * 
	 * @param bitmap
	 * @return
	 */
	public InputStream Bitmap2InputStream(Bitmap bitmap) {
		return Bitmap2InputStream(bitmap, 100);
	}

	/**
	 * bitmap转化为InputStream
	 * 
	 * @param bitmap
	 * @param Quality
	 * @return
	 */
	public InputStream Bitmap2InputStream(Bitmap bitmap, int Quality) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, Quality, bos);
		InputStream is = new ByteArrayInputStream(bos.toByteArray());
		return is;
	}

	/**
	 * InputStream转化为Bitmap
	 * 
	 * @param is
	 * @return
	 */
	public Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * Bitmap转化为byte[]
	 * @param bitmap
	 * @return
	 */
	public byte[] Bitmap2Bytes(Bitmap bitmap){
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
		return bs.toByteArray();
	}
	
	/**
	 * byte[]转化为Bitmap
	 * @param b
	 * @return
	 */
	public Bitmap Bytes2Bitmap(byte[]b){
		if(b.length!=0){
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}
	
	/**
	 * Drawable 转化为InputStream
	 * @param d
	 * @return
	 */
	public InputStream Drawable2InputStream(Drawable d) {
		Bitmap bitmap = Drawable2Bitmap(d);
		return Bitmap2InputStream(bitmap);
	}

	/**
	 * Drawable转化为byte[]
	 * @param d
	 * @return
	 */
	public byte[] Drawable2Bytes(Drawable d){
		Bitmap bitmap=Drawable2Bitmap(d);
		return Bitmap2Bytes(bitmap);
	}
	
	/**
	 * Drawable转化为Bitmap
	 * @param d
	 * @return
	 */
	public Bitmap Drawable2Bitmap(Drawable d) {
		Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d
				.getIntrinsicHeight(),
				d.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
						: Bitmap.Config.RGB_565);
		Canvas canvas =new Canvas(bitmap);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		d.draw(canvas);
		return bitmap;
	}

	/**
	 * bitmap转化为Drawable
	 * @param bitmap
	 * @return
	 */
	public Drawable Bitmap2Drawable(Bitmap bitmap){
		@SuppressWarnings("deprecation")
		BitmapDrawable bd=new BitmapDrawable(bitmap);
		return (Drawable)bd;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
