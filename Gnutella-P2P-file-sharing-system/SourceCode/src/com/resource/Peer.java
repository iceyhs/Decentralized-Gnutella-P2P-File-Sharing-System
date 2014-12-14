package com.resource;
/*
 * initialize peer
 * create thread for each query
 * 
 */
import java.io.*;
import java.net.*;
import java.util.*;

import com.resource.getFile;

import java.io.Serializable;
import java.util.ArrayList;



@SuppressWarnings("serial")
public class Peer implements Serializable {
    protected int id;
   // protected String location;
    protected getFile filelist;
    protected String IpAddress;
    protected int ListeningPort;
    private ServerSocket serversocket; //run as server
	private ArrayList<Neighbor> npeerlist = new ArrayList<Neighbor>();
	static int TTL=7;
    
      
    //constructor
    public Peer(String id, String IpAddress, String ListeningPort ){
    this.id=Integer.parseInt(id);
   // this.location=location;
    //this.filelist=filelist;
    this.IpAddress=IpAddress;
    this.ListeningPort=Integer.parseInt(ListeningPort); 	
    filelist = new getFile(Integer.parseInt(id));
    }
	
	public void setFilelist(getFile filelist){
		this.filelist=filelist;
	}
	
	public getFile getFilelist(){
		return filelist;
	}
	
	public void setide(int id){
		this.id=id;
	}
	
	public int getid(){
		return id;
	}
	
	public int getListeningPort(){
		return ListeningPort;
	}
	
	public String getIpAddress(){
		return IpAddress;
	}
	


	/** add neighbor */
	public void addNPeer(Neighbor npper) {
		this.npeerlist.add(npper);
	}
	public List<Neighbor> getNPeerList() {
		return this.npeerlist;
	}
	
	/*
	 *create new thread for each service 
	 *Server
	 */
	public void start(){
	 try {
		serversocket = new ServerSocket(this.getListeningPort());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 System.out.println("Peer: "+ this.getid() + "  Port: " + this.getListeningPort() + "  is running");
	 while(true)
	 {
		 try {
			 
			 Socket socket; 
			socket = serversocket.accept();
			ConnectionHandler(socket,this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
			
		}
		 
		 
		 
	 }
		
	 
	 
		
	

	private void ConnectionHandler(Socket socket, Peer peer) throws IOException {
		// TODO Auto-generated method stub
		new Thread( new ServerHandler(socket,peer)).start();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
  	
}
