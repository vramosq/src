package com.ice.sgpr.ui.utils;

import java.io.File;

import com.ice.sgpr.R;
import com.ice.sgpr.comun.Constantes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;


public class ImageChooseHandler 
{
	
	public Uri _statusImageUri;	
	private final Activity _activity;
	
	public ImageChooseHandler(Activity pActivity) 
	{
		_activity=pActivity;
	}
		 
	@SuppressLint("NewApi")
	public AlertDialog createChooseImageDialog()
	{		
		String [] dialogItems = new String [] {_activity.getResources().getString(R.string.choose_image_from_camera),
				_activity.getResources().getString(R.string.choose_image_from_gallery)};
		ArrayAdapter<String> adapter  = new ArrayAdapter<String> (_activity, android.R.layout.select_dialog_item, dialogItems);        
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_activity);
        dialogBuilder.setTitle(R.string.choose_image_title);        
		dialogBuilder.setAdapter( adapter, new DialogInterface.OnClickListener() 
		{
            public void onClick(DialogInterface dialog, int item ) 
            {
                if (item == 0) 
                {	
                	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);                
                	_statusImageUri = Uri.fromFile(getPictureFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, _statusImageUri);
                    _activity.startActivityForResult(intent, Constantes.IMAGEN_DESDE_CAMARA);
                }
                else 
                {
                	Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");                    
                    _activity.startActivityForResult(intent, Constantes.IMAGEN_DESDE_ARCHIVO);
                    dialog.dismiss();
                }
            }
        }); 
        return dialogBuilder.create();
	}
	
	public String getPathFromCamera()
	{		
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    mediaScanIntent.setData(_statusImageUri);
	    _activity.sendBroadcast(mediaScanIntent);		    
	    return _statusImageUri.getPath();
	}
	
	public String getPathFromFile(Intent data)
	{
		_statusImageUri = data.getData();			
		String[] filePathColumn = { MediaStore.Images.Media.DATA };	 
        Cursor cursor =_activity.getContentResolver().query(_statusImageUri,filePathColumn, null, null, null);
        cursor.moveToFirst();	 
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path=cursor.getString(columnIndex);
        cursor.close();
        return  path;
	}

	public static File getPictureFile()
	{
		String root=Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + Constantes.SGPR_FOLDER_IMAGEN);
    	if (!myDir.exists()) 
    	{                   	
        	myDir.mkdirs();
    	}                    
        File file = new File(myDir,String.valueOf(Constantes.SGPR_PREFIJO_IMAGEN+System.currentTimeMillis()) + Constantes.SGPR_EXTENSION_IMAGEN);
		return file;        
	}
}
