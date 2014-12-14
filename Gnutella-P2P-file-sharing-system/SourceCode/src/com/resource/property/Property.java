package com.resource.property;

/**
 * This class is used for read the info
 * from INFO.properties
 */

import  java.io.*;
import java.util.*;


@SuppressWarnings("serial")
public class Property implements Serializable {
    
	
	
	public static Properties getProperties(){
		
	InputStream in;
	
	 Properties prop= new Properties();
	 try{
	 in = Property.class.getClassLoader().getResourceAsStream("INFO.properties");
	 prop.load(in);	
	 }catch(FileNotFoundException e){
		 e.printStackTrace();
	 }catch(IOException e){
		 e.printStackTrace();
	 }
	
	if(prop.isEmpty()){
		System.out.println("INFO.properties is null");
	}
	return prop;
	
	}	
	
	
}
