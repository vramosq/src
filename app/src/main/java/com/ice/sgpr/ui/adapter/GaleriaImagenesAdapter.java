package com.ice.sgpr.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class GaleriaImagenesAdapter extends BaseAdapter {
	
	private Context _context;
	private Bitmap[] _images;
	private int _height;
	private int _width;
	
	public GaleriaImagenesAdapter(Context pContext, Bitmap[] pImages)
	{
		_context = pContext;
		_images = pImages;
		
		//Get height and width in pixels
		_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DPI, pContext.getResources().getDisplayMetrics());
		_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DPI, pContext.getResources().getDisplayMetrics());
		
	}
	
	public void setImages(Bitmap[] pImages)
	{
		_images = pImages;
	}

	@Override
	public int getCount() 
	{
		return _images.length;
	}

	@Override
	public Bitmap getItem(int pPos) {
		if (pPos < _images.length)
			return _images[pPos];
		return null;
	}

	@Override
	public long getItemId(int pPos) 
	{
		return pPos;
	}

	@Override
	public View getView(int pPosition, View pConvertView, ViewGroup pParent) 
	{
		ImageView imageView = new ImageView(_context);
		if (_images[pPosition] != null)
			imageView.setImageBitmap(_images[pPosition]);
		imageView.setLayoutParams(new Gallery.LayoutParams(_width, _height));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setPadding(1, 1, 1, 1);
		return imageView;
	}
	
	private static int HEIGHT_DPI = 70;
	private static int WIDTH_DPI = 85;

}

