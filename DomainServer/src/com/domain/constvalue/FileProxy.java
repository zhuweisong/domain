package com.domain.constvalue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class FileProxy {
    String encoding="GBK";
	File file = null;
	
	public boolean init(String filePath) {

	    file=new File(filePath);
	    return file.isFile() && file.exists(); //判断文件是否存在
	}
	
	public boolean getNext(int c, List<String> dl) {
		try {
			InputStreamReader read = new InputStreamReader(
			        new FileInputStream(file),encoding);
			
            String lineTxt = null;
            BufferedReader bufferedReader = new BufferedReader(read);
            while((lineTxt = bufferedReader.readLine()) != null){
            	dl.add(lineTxt);
            }
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return true;
	}


}
