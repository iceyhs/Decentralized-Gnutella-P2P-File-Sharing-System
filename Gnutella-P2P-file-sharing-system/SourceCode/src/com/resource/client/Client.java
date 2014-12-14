package com.resource.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import com.resource.message.*;
import com.resource.property.Property;



public class Client {
	
	   static Socket socket;
	   static String read;
	  static ObjectInputStream SocketReader;
	   static  ObjectOutputStream SocketWriter;
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 Properties pro = Property.getProperties();
		 String ip= pro.getProperty("LOCAL_IP");
         int port = Integer.parseInt(pro.getProperty("LOCAL_PORT"));
		
	   try {
				socket= new Socket(ip,port);
				System.out.println("Bind to client IP : " +ip+ " Port : " +port );
				//SocketReader = new ObjectInputStream(socket.getInputStream());
				SocketWriter = new ObjectOutputStream(socket.getOutputStream());
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Unknown Host");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      String filename;
	      String text;
	      String IPAddress;
	      int portN;
		Message mesOut;   	   	 
			     do {
					System.out.println("*************What do you want to do?***************");
					System.out.println("SearchFile");
					System.out.println("DownloadFile");
					System.out.println("Exit");
				     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						text=br.readLine();
					if(text.equals("SearchFile")){
								System.out.println("Search File, Please Input a name:");
								filename = br.readLine();
								Message mes= new Message("fromip",0,0,0,"filename","id","ip","Request",0);
								mesOut=mes.packaforsearch(ip, port, 7, 0, filename, "0", ip, "SearchFile",port);
								SocketWriter.writeObject(mesOut);
								SocketWriter.flush();
							
							}
					
					
				
					   
				     
				   if(text.equals("DownloadFile")){
					   int[] bytenumber = null;
					   byte[][] bytetemp = null;
					   System.out.println("Search File, Please Input a filename:");
					   filename = br.readLine();
					   System.out.println("Please Input IP address :");
					   IPAddress=br.readLine();
					   System.out.println("Please Input Port Number :");
					   portN=Integer.parseInt(br.readLine());
					   //public Message packagefordownload(String filename, String ip, int port,int[] bytenumber, byte[][] bytetemp, String RequestFunction)
					   Message mes= new Message (filename,IPAddress,portN,bytenumber,bytetemp,"UploadFile",ip,port);
					   mesOut=mes.packagefordownload(filename, IPAddress, portN, bytenumber,bytetemp, "UploadFile",ip,port);
					  ObjectOutputStream SocketWriter1;
					  socket= new Socket(IPAddress,portN);
					  SocketWriter1 = new ObjectOutputStream(socket.getOutputStream());
					   
					   SocketWriter1.writeObject(mesOut);
					   SocketWriter1.flush();
				   }
				     
				     
		} while (!text.equals("Exit"));

			}
			   
		
		
		
		
		
		
		
	}


