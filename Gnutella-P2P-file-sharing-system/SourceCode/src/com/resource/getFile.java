package com.resource;

import java.io.*;
import java.util.*;



/**
 * get file list
 * 
 */

public class getFile {
   protected int peerid;
   
   //constructor 
   public getFile(int peerid){
	   this.peerid=peerid;
   }
	
   public ArrayList<String> getfilelist (){
   	
   	ArrayList<String> filelist = new ArrayList<String>();
   	
   	try{
   		
   		File file=new File(this.getPath());
   		
   		File list[]=file.listFiles();
   		System.out.println(list.length);
   		for(int i=0; i<list.length; i++)
   		{
   			filelist.add(list[i].getName());
   		}
   	}catch(Exception e)
   	{
   		e.printStackTrace();
   	}
   	return filelist;	
   } 
   
   public String getPath(){
	   File directory = new File(System.getProperty("user.dir"));
	   return directory.getAbsolutePath()+"/";
   }
   
   public boolean ifhit(String filename ){
	   for(String file: this.getfilelist() ){
		   if(file.equals(filename))
			   return true;
	   }
	   
	   return false;
	   
   }
   
   
   
   
   
	
}
