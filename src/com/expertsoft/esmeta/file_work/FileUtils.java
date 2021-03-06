package com.expertsoft.esmeta.file_work;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class FileUtils {

	static final char[]	lookup =
	    {
		0x0000, 0x0001, 0x0002, 0x0003, 0x0004,	0x0005,	0x0006,	0x0007, //7
		0x0008, 0x0009,	0x000A,	0x000B,	0x000C,	0x000D,	0x000E,	0x000F, //15 
		0x0010, 0x0011,	0x0012,	0x0013,	0x0014,	0x0015,	0x0016,	0x0017, //23 
		0x0018, 0x0019,	0x001A,	0x001B,	0x001C,	0x001D,	0x001E,	0x001F, //31
		0x0020, 0x0021, 0x0022,	0x0023,	0x0024,	0x0025,	0x0026,	0x0027, //39
		0x0028, 0x0029,	0x002A,	0x002B,	0x002C,	0x002D,	0x002E,	0x002F, //47
		0x0030, 0x0031,	0x0032,	0x0033,	0x0034,	0x0035, 0x0036,	0x0037, //55
		0x0038, 0x0039,	0x003A,	0x003B,	0x003C,	0x003D,	0x003E,	0x003F, //63
		0x0040, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047, //71
		0x0048, 0x0049, 0x004A, 0x004B, 0x004C, 0x004D, 0x004E, 0x004F, //79
		0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057, //87
		0x0058, 0x0059, 0x005A, 0x005B, 0x005C, 0x005D, 0x005E, 0x005F, //95
		0x0060, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067, //103
		0x0068, 0x0069, 0x006A, 0x006B, 0x006C, 0x006D, 0x006E, 0x006F, //111
		0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077, //119
		0x0078, 0x0079, 0x007A, 0x007B, 0x007C, 0x007D, 0x007E, 0x007F, //127
		
		0x0410, //�
		0x0411,	0x0412,	0x0413,	0x0414,	0x0415,	0x0416,	0x0417, //135
		0x0418, 0x0419,	0x041A,	0x041B,	0x041C,	0x041D,	0x041E,	0x041F, //143
		0x0420, //�
		0x0421, 0x0422,	0x0423,	0x0424,	0x0425,	0x0426, 0x0427, //151
		0x0428, 0x0429,	0x042A,	0x042B,	0x042C,	0x042D, 0x042E,	0x042F, //159
		0x0430, //�
        0x0431, 0x0432, 0x0433, 0x0434, 0x0435, 0x0436, 0x0437, //167
		0x0438, 0x0439,	0x043A, 0x043B, 0x043C, 0x043D, 0x043E, 
		0x043F, //175 �
		0x2591, 0x2592, 0x2593, 0x2502, 0x2524, 0x2561, 0x2562, 0x2556, //183
		0x2555, 0x2563, 0x2551, 0x2557, 0x255D, 0x255C, 0x255B, 0x2510, //191
		0x2514, 0x2534, 0x252C, 0x251C, 0x2500, 0x253C, 0x255E, 0x255F, //199
		0x255A, 0x2554, 0x2569, 0x2566, 0x2560, 0x2550, 0x256C, 0x2567, //207
		0x2568, 0x2564, 0x2565, 0x2559, 0x2558, 0x2552, 0x2553, 0x256B, //215
		0x256A, 0x2518, 0x250C, 0x2588, 0x2584, 0x258C, 0x2590, 0x2580, //223
		0x0440,//224 - � 
		0x0441, 0x0442, 0x0443, 0x0444, 0x0445, 0x0446, 0x0447, //231
		0x0448, 0x0449, 0x044A, 0x044B, 0x044C, 0x044D, 0x044E, 
		0x044F, //239 - �
		0x0401, 0x0451, 0x0404, 0x0454, 0x0407, 0x0457, 0x040E, 0x045E, //247
		0x00B0, 0x2219, 0x00B7, 0x221A, 0x2116, 0x00A4, 0x25A0, 0x00A0  //255
	    };
	
	private String Source, NewExtension;
	File GlobalFile;
	
	public FileUtils(String source, String newExtension)
	{				
		Source = source;
		NewExtension = newExtension;		
	}
	public File GetCopyedFile(){
		return GlobalFile;
	}				
	
	public File encodeCP866ToUTF8(String filepath, String Charset){
		File file = new File(filepath);			
		try{						
			BufferedInputStream buff = new BufferedInputStream(new FileInputStream(filepath)); 
			String readbuff = "";						
			int ch;
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(file.getParent()+ "/tempFile.txt"), "UTF-8");
			while((ch = buff.read()) > -1)						
			{															
				//readbuff += lookup[ch];
				writer.write(lookup[ch]);
			}
			buff.close();			
			//writer.write(readbuff.toString());
			writer.close();	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new File(file.getParent()+ "/tempFile.txt");
	}
	
	//Variant work slowly
	public File encodeCP866ToUTF82(String filepath, String Charset){
		File file = new File(filepath);			
		try{						
			BufferedInputStream buff = new BufferedInputStream(new FileInputStream(filepath)); 
			String readbuff = "";
			String TmpBuff = "";
			int count250 = 0;
			int ch;
			while((ch = buff.read()) > -1)						
			{											
				if(TmpBuff.length() > 5000){
					count250++;
					TmpBuff = "";
				}
				readbuff += lookup[ch];
				TmpBuff += lookup[ch];
			}
			buff.close();
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(file.getParent()+ "/tempFile.txt"), "UTF-8");
			writer.write(readbuff.toString());
			writer.close();	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new File(file.getParent()+ "/tempFile.txt");
	}
	
	//Variant which breaks
	public File encoddeAnyToUTF81(String filepath, String Charset, String ext){
		File f = new File(filepath);		
		String globalFileName = f.getParent() + "/tempFile"+ext;
		try{
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filepath), Charset));
			String ch;					
			ByteArrayOutputStream readbuff = new ByteArrayOutputStream();
			BufferedOutputStream buffOutS = new BufferedOutputStream(readbuff);
			while((ch = br.readLine())!= null)
			{																
				byte[]b = ch.getBytes("UTF-8");
				readbuff.write(b);												
			}
			br.close();									
			f.delete();			
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(f.getParent() + "/tempFile"+ext), "UTF-8");										                
			writer.write(readbuff.toString());
			writer.close();	
			readbuff.close();			
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		return new File(globalFileName);
	}
	
	public File encoddeAnyToUTF8(String filepath, String Charset, String ext){
		File f = new File(filepath);		
		String globalFileName = f.getParent() + "/tempFile1"+ext;
		try{
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filepath), Charset));
			String ch;	
			FileOutputStream mainStream = new FileOutputStream(globalFileName);
			BufferedOutputStream readbuff = new BufferedOutputStream(mainStream);
			while((ch = br.readLine())!= null)
			{																
				byte[]b = ch.getBytes("UTF-8");
				readbuff.write(b);
				byte[]b1 = {13,10};
				readbuff.write(b1);
			}
			readbuff.close();
			mainStream.close();
			br.close();			
			if(! ext.contains("txt")){
				f.delete();			
			}
			/*
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(f.getParent() + "/tempFile"+ext), "UTF-8");										                
			writer.write(readbuff.toString());
			writer.close();	*/					
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		return new File(globalFileName);
	}
	
	public File encoddeUTF8ToWin1251(String filepath,String fileName, String Charset, String ext){
		File f = new File(filepath);		
		String globalFileName = f.getParent() + "/"+ fileName + ext;
		try{
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filepath), Charset));
			String ch;	
			FileOutputStream mainStream = new FileOutputStream(globalFileName);
			BufferedOutputStream readbuff = new BufferedOutputStream(mainStream);
			while((ch = br.readLine())!= null)
			{																
				byte[]b = ch.getBytes("windows-1251");
				readbuff.write(b);
				byte[]b1 = {13,10};
				readbuff.write(b1);
			}
			readbuff.close();
			mainStream.close();
			br.close();			
			if(! ext.contains("txt")){
				f.delete();			
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		return new File(globalFileName);
	}
	
	public boolean renameFileExtension()
	{
	    String target;
	    File f = copyFile(Source);
	    Source = f.getPath();
	    String currentExtension = getFileExtension(Source);

	    if (currentExtension.equals("")){
	      target = Source + "." + NewExtension;
	    }
	    else {
	      target = Source.replaceFirst(Pattern.quote("." +
	          currentExtension) + "$", Matcher.quoteReplacement("." + NewExtension));
	    }			    			    
	    boolean Renamed = new File(Source).renameTo(new File(target));
	    if(Renamed)
	    	GlobalFile = new File(target);
	    else
	    	GlobalFile = new File(Source);
	    return Renamed;
	}								
	public File copyFile(String inputFile) {

	    InputStream in = null;
	    OutputStream out = null;
	    File dir = new File(inputFile);
	    try {
	        //create output directory if it doesn't exist
	        dir = new File(new File(inputFile).getParent()+"/temp/"); 
	        /*if (!dir.exists())			        
	            dir.mkdirs();	*/		      
	      //  File ExistFile = new File(inputFile);

	        in = new FileInputStream(inputFile);        
	        String newFile = dir.getPath() + "File.txt";
	        out = new FileOutputStream(newFile);

	        byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1) {	        	
	        	out.write(buffer, 0, read);
	        }
	        in.close();
	        in = null;
	            // write the output file (You have now copied the file)
	            out.flush();
	        out.close();
	        out = null;
	        dir = new File(newFile);
	        if (dir.isFile());			        
	          return dir;
	    }  catch (FileNotFoundException fnfe1) {
	        Log.e("tag", fnfe1.getMessage());
	    }
	            catch (Exception e) {
	        Log.e("tag", e.getMessage());
	    }
	    return dir;
	}
	public String getFileExtension(String f) {
	    String ext = "";
	    int i = f.lastIndexOf('.');
	    if (i > 0 &&  i < f.length() - 1) {
	      ext = f.substring(i + 1);
	    }
	    return ext;
	}			  
}	

