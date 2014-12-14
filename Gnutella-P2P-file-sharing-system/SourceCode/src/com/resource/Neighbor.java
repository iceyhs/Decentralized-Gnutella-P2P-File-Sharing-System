package com.resource;


import java.io.*;
import java.net.*;
import java.util.*;
import java.io.IOException;
import java.io.Serializable;





import com.resource.message.Message;

@SuppressWarnings("serial")
public class Neighbor implements Serializable {

	    protected int id;
	   // protected String location;
	    //protected getFile filelist;
	    protected String IpAddress;
	    protected int ListeningPort;
	    
	   // public Socket socket; 
	    
	    protected ObjectInputStream SocketReader;
		protected ObjectOutputStream SocketWriter;
		//protected BufferedReader in;
		//protected PrintWriter out;
	    
	      
	    //constructor
	    public Neighbor(String id, String IpAddress, String ListeningPort ){
	    this.id=Integer.parseInt(id);
	    this.IpAddress=IpAddress;
	    this.ListeningPort=Integer.parseInt(ListeningPort); 	
	    }
		
		public void setid(int id){
			this.id=id;
		}
		
		public int getid(){
			return id;
		}
		
		public void setListeningPort(int port){
			this.ListeningPort=port;			
		}
		
		public int getListeningPort(){
			return ListeningPort;
		}
		
		public void setIpAddress(String ipaddress){
			this.IpAddress=ipaddress;
		}
		
		public String getIpAddress(){
			return IpAddress;
		}
		
		public void ConnectionSetUp(){
			try{
				@SuppressWarnings("resource")
			
				Socket socket = new Socket(this.IpAddress,this.ListeningPort);
				//in = new BufferedReader(new InputStreamReader(
						//socket.getInputStream()));
				//out = new PrintWriter(new BufferedWriter(
						//new OutputStreamWriter(socket.getOutputStream())), true);;
				//SocketReader = new ObjectInputStream(socket.getInputStream());
				SocketWriter = new ObjectOutputStream(socket.getOutputStream());
				Thread.sleep (1000);
			}catch (Exception e)
			{
			  e.printStackTrace();
			  System.out.println("Failed to connect IP: "+ this.IpAddress + "Port: " + this.ListeningPort);
			}
		}	
			
		public void DestroyConncetion( ) {
			try{
				//SocketReader.close();
				SocketWriter.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	/*
	 * 
	 * send message to neighbors 
	 * 
	 */
	
	public void SendMessage(Message mes) {
		
		this.ConnectionSetUp();
		try {
			SocketWriter.writeObject(mes);
			SocketWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.DestroyConncetion();
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
