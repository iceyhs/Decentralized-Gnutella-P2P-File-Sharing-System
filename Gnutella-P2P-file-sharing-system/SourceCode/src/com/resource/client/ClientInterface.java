package com.resource.client;

/*
 * This class provide interface for user, and read info from property 
 * to initialize all peers 
 * Run as A Client
 */

import java.io.BufferedReader;

import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import com.resource.Peer;
import com.resource.Neighbor;
import com.resource.property.Property;;

@SuppressWarnings("serial")
public class ClientInterface implements Serializable{
   public static String LOCAL_IP="LOCAL_IP";
   public static String LOCAL_PORT="LOCAL_PORT";
   public static String LOCAL_ID="LOCAL_ID";
   public static String N_IDS="N_IDS";
   public static String N_IPS="N_IPS";
   public static String N_PORTS="N_PORTS";
   static BufferedReader in; // socket input
   static PrintWriter out ; // socket output
   static Socket socket;
   static String read;
   
   
   
  public static void main(String[] args){	
	   Peer peer = getpeer();
	   Init_Npeer(peer);
	   peer.start();	   
	  
	}
	   

     
 /*
  * get local peer info
  */
private static Peer getpeer(){
	  Properties pro = Property.getProperties();
	  if(pro.isEmpty()){
		  System.out.println("INFO.properties is empty");
		  return null;
	  }
	  else{
		String ip= pro.getProperty(LOCAL_IP);
		String port = pro.getProperty(LOCAL_PORT);
		String id = pro.getProperty(LOCAL_ID);
		//id ip port
		Peer peer = new Peer(id,ip,port);
		return peer;
		  
	  }
	   
   }
   
  /*
   * initialize the neighbors
   */
   

private  static  void Init_Npeer(Peer peer){
	  Properties pro = Property.getProperties();
	  if(pro.isEmpty()){
		  System.out.println("INFO.properties is empty!");
	  }
	  
	String N_IP =pro.getProperty(N_IPS);
	String N_ID = pro.getProperty(N_IDS);
	String N_PORT= pro.getProperty(N_PORTS);
	String N_IPS_List[] = N_IP.split(",");
	String N_IDS_List[] = N_ID.split(",");
	String N_PORT_List[] = N_PORT.split(",");
	for(int i=0; i<N_IPS_List.length;i++){
		// id ip port
		Neighbor neighbor = new Neighbor(N_IDS_List[i],N_IPS_List[i],N_PORT_List[i]);
		peer.addNPeer(neighbor);		
	}	  
  }
	
  
   
   
   
		
}
