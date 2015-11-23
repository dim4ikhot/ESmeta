package com.expertsoft.esmeta.file_work;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import android.util.Log;

public class UnZipBuild {

	private String _zipFile; 
	private String _location; 
	
	public UnZipBuild(String zipFile, String location) { 
		_zipFile = zipFile; 
    	_location = location; 
 
//    	_dirChecker(""); 
	} 
	//Unzipping All in Cp1251 
	public File ExUnzip() throws IOException {
	    int count=0;
	    File ReturnedFile = null;
	    String GlobalPath = "";
	    FileInputStream fis = null;
	    ZipArchiveInputStream zis = null;
	    FileOutputStream fos = null;
	    File file = null;
	    try {
	        byte[] buffer = new byte[8192];
	        fis = new FileInputStream(_zipFile);
	        zis = new ZipArchiveInputStream(fis, "Cp1251", true); // this supports non-USACII names
	        ArchiveEntry entry;	        
	        while ((entry = zis.getNextEntry()) != null) {	        	
	        	ReturnedFile = new File(_location);
	        	GlobalPath = ReturnedFile.getPath() ;
	            //File file = new File(_location, entry.getName());
	            file = new File(GlobalPath + "/tempFile.xml");
	            if (entry.isDirectory()) {
	                file.mkdirs();
	            } else {
	                count++;
	                file.getParentFile().mkdirs();
	                fos = new FileOutputStream(file);
	                int read;
	                while ((read = zis.read(buffer,0,buffer.length)) != -1)
	                    fos.write(buffer,0,read);
	                fos.close();
	                fos=null;
	            }
	        }
	    } finally {
	        try { zis.close(); } catch (Exception e) { }
	        try { fis.close(); } catch (Exception e) { }
	        try { if (fos!=null) fos.close(); } catch (Exception e) { }
	        ReturnedFile = file;
	        if(ReturnedFile.isFile())
	        	return ReturnedFile;
	    }
	    return ReturnedFile;
	}
	//Unzipping only in UTF-8
	public File unzip() {
		String unzippedFile = "";
	    try {	        
	        ZipInputStream zipStream = new ZipInputStream(new FileInputStream(_zipFile));	     
	        ZipEntry zEntry = null;	     	        
	        while ((zEntry = zipStream.getNextEntry()) != null) {
	            Log.d("myLogs", "Unzipping " + zEntry.getName() + " at "+ _location);

	            if (zEntry.isDirectory()) {
	            	_dirChecker(zEntry.getName());
	            } else {
	            	unzippedFile = this._location + "/" + "tempFile.xml";
	                FileOutputStream fout = new FileOutputStream(unzippedFile);
	                BufferedOutputStream bufout = new BufferedOutputStream(fout);
	                byte[] buffer = new byte[1024];
	                int read = 0;
	                while ((read = zipStream.read(buffer)) != -1) {
	                    bufout.write(buffer, 0, read);
	                }
	                zipStream.closeEntry();
	                bufout.close();
	                fout.close();
	            }
	        }
	        zipStream.close();
	        Log.d("myLogs", "Unzipping complete. path :  " + _location);
	    } catch (Exception e) {
	        Log.d("myLogs", "Unzipping failed:" + e.getMessage().toString());
	        e.printStackTrace();
	    }
	    return new File(unzippedFile);

	} 
	 
	  private void _dirChecker(String dir) { 
	    File f = new File(_location + dir); 	 
	    if(!f.isDirectory()) { 
	      f.mkdirs(); 
	    } 
	  } 
}
