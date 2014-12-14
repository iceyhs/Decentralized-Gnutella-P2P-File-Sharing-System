package com.resource;

import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import com.resource.message.Message;
import com.resource.getFile;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;



public class ServerHandler implements Runnable {

	
    //used for storing messageID
	public static CopyOnWriteArrayList<String> Message_IDlist= new CopyOnWriteArrayList<String>();
	// used for handle each message
	public static Map<String, ServerHandler> Message_Handler = new HashMap<String, ServerHandler>();
	public static ArrayList<Integer> sourceId= new  ArrayList<Integer>();
	public static int TTL =7;
	
	protected ObjectInputStream SocketReader;
	protected ObjectOutputStream SocketWriter;
	
	private Socket socket;
	private Peer peer;
	
	private int Hit = -3;
	private int Miss = -4;
	


	ServerHandler handler;
	  // constructor 
		public ServerHandler(Socket socket, Peer peer) throws IOException{
			this.socket=socket;
			this.peer=peer;	
				
		}
			
	
		
	public void DestroyConncetion( ) {
		try{
			SocketReader.close();
			SocketWriter.close();
		}catch(Exception e)
		{
			System.out.println("Error destroying socket connection !" + e);
		}
		
	}
	
	/*
	 * add message id to list, and push it to map
	 */
	public void AddMessageID(String id, ServerHandler handler){
		
		Message_IDlist.add(id);
		Message_Handler.put(id,handler);		
	}

	/**
	 * Broadcast to neighbors except the source
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	

	private void braodcast(Message mes) throws InterruptedException, IOException{
		//BufferedReader in;
 		String peers ="";
		int sourceId = mes.getSourceId();	
		int ttl=mes.getTTL();
		mes.setTTL(ttl-1);
		for(Neighbor neighbor : peer.getNPeerList()){
			if(neighbor.getid() !=sourceId){
				System.out.println("Broadcast begin");
				 Message nmes= new Message("fromip",0,0,0,"filename","id","ip","SearchFile",0);
				   nmes.setFromip(peer.getIpAddress());
				   nmes.setFromport(peer.getListeningPort());
				   nmes.setTTL(mes.getTTL());
				   nmes.setPort(mes.getPort());
   			   String id = System.nanoTime()+"_"+peer.getid();
				   nmes.setid(id);
				   nmes.setSourceId(peer.getid());
				   nmes.setip(peer.getIpAddress());
				   nmes.setFilename(mes.getFIlename());
				   handler=new ServerHandler(socket,peer);
				   this.AddMessageID(id, handler);
				neighbor.SendMessage(nmes);
				peers += neighbor.getid()+"," ;
				
			}
			
		}
		if(!peers.equals("")){
			System.out.println("send request to peer: " + peers + "waiting for the reponse..."); 
			//in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//in.readLine();
			}
			else{
				System.out.println("No Neighbors to BroadCast ");
				this.DestroyConncetion();
			}
	}

	
	private void SearchLocalFile(Message mes) throws Exception{
		    Message mesout;
			String messageId = mes.getid();
			if(messageId.equals(""))
				return;
			if(Message_IDlist.contains(messageId)) 
				return;
			
			else { 
				
				handler=new ServerHandler(socket,peer);
				AddMessageID(messageId, handler);}
			String filename = mes.getFIlename();
			Message mes1= new Message("fromip",0,0,0,"filename","id","ip","SearchFile",0);
			if(peer.getFilelist().ifhit(filename)){				
				mes1.setFromip(mes.getFromip());
				mes1.setFromport(mes.getFromport());
				mes1.setip(peer.getIpAddress());
				mes1.setPort(peer.getListeningPort());
				mes1.setFilename(mes.getFIlename());
				mes1.setid(mes.getid());
				mes1.setTTL(Hit);
				mesout=mes1.packaforsearch(mes1.getip(), mes1.getFromport(), mes1.getTTL(), mes1.getSourceId(), mes1.getFIlename(), mes1.getid(), mes1.getip(),"SearchFile",mes1.getPort());			
				this.sendResult(mesout);
				
				
			}
			else {
				mes1.setFromip(mes.getFromip());
				mes1.setFromport(mes.getFromport());
				mes1.setip(peer.getIpAddress());
				mes1.setPort(peer.getListeningPort());
				mes1.setFilename(mes.getFIlename());
				mes1.setid(mes.getid());
				mes1.setTTL(Miss);
				//System.out.println(mes1.getTTL());
				mesout=mes1.packaforsearch(mes1.getip(), mes1.getFromport(), mes1.getTTL(), mes1.getSourceId(), mes1.getFIlename(), mes1.getid(), mes1.getip(),"SearchFile",mes1.getPort());			
				this.sendResult(mesout);
				
			}			
		}
		
		
		/*
		 * Search File
		 */
		
		private void searchFile(Message mes) throws Exception{
			if(mes.getTTL()<1) return;
			else{
				SearchLocalFile(mes);
				this.braodcast(mes);
				
			}			
		}
		
		
		/*
		 * send result 
		 */
		
		private void sendResult(Message mes) throws Exception{
	//		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
			
			String m_id = mes.getid();
			// get handler by message ID from map, use handler to send results back to client 
			ServerHandler handler = Message_Handler.get(m_id);
			//System.out.println(m_id);
			if (handler != null && handler.socket.isConnected()) {
				//System.out.println(mes.getTTL());
				if (mes.getTTL() == Hit)
					System.out.println(mes.getFIlename() + "  IP : " + mes.getip() + "  Port : " + mes.getPort() + " is Hit");
				if (mes.getTTL() == Miss)
					System.out.println(mes.getFIlename()+ "  Port :" + mes.getip() + "  Port : " + mes.getPort() + " is Miss");
			}
			// temporary connection，need to be closed		
		}
				
		
	/*
	 * Download File	(non-Javadoc)
	 * cannot download from self
	 * @see java.lang.Runnable#run()
	 */
		
		
		//DownLoadFile=filename,ip,port
		
    public void DownloadFIle(Message mes) {
    	try {
			
			System.out.println("DownLoad from IP : " +mes.getip() + "Port : " + mes.getPort());
			//SocketWriter = new ObjectOutputStream(socket.getOutputStream());
			// begin downloading 
			
			String savepath = peer.getFilelist().getPath();
			savepath += mes.getFIlename();
			File file = new File(savepath);			
			byte[][] temp=mes.bytetemp;
			int[] readbyte=mes.bytenumber;
			
			FileOutputStream Out= new FileOutputStream(file);
			
			int N = 0;
			for(int i = 0; i<readbyte.length; i++){
				if(readbyte[i] == -1){
					N = i;
				}
			}
			for(int i = 0;i<N;i++){
				Out.write(temp[i], 0, readbyte[i]);
			}
			   
			
			System.out.println("Download succeeded. File saved to " + savepath + "\n");
			Out.close();
	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Download failed");
		}    	
    	
    }
	
    public void UploadFile(Message mes) throws Exception, IOException{
    	 ObjectOutputStream SocketWriter1;
    	byte[][] tempbyte=null;
		int[] byteread=null;
    	
    	try{
        	//Socket socket = new Socket (mes.getFromip(),mes.getFromport());
			System.out.println("Upload from IP : " +mes.getFromip() + "Port : " + mes.getFromport());        	
        	
		
		
			
		if (!peer.getFilelist().ifhit(mes.getFIlename()) ){
			System.out.println("File dose not Exist");
		} else {
			
			String filePath = peer.getFilelist().getPath();
			filePath += mes.getFIlename();
			//File file = new File(filePath);
			@SuppressWarnings("resource")
			FileInputStream fis= new FileInputStream(filePath);
	    	 System.out.println(peer.getid() +" is Uploading File: " + mes.getFIlename());	
	         tempbyte= new byte[1024][1024];
	          byteread = new int[1024];
	    	 int m=0;
	    	 while((byteread[m]=fis.read(tempbyte[m]))!=-1){
	    		 m++;
	    	 }
	    	 
	    	 Message mes1= new Message ("filename",peer.getIpAddress(),peer.getListeningPort(),byteread, tempbyte,"DownloadFile",mes.getFromip(),mes.getFromport());
			 Message out=mes1.packagefordownload(mes.getFIlename(), mes.getip(), mes.getPort(), byteread, tempbyte, "DownloadFile",mes.getFromip(),mes.getFromport()); 
			 
			  
			  Socket socket1 = new Socket(mes.getFromip(),mes.getFromport());
			  SocketWriter1 = new ObjectOutputStream(socket1.getOutputStream());
			   
			 System.out.println("Uploading file to IP :" + mes.getFromip() + " Port : " + mes.getFromport());
			 SocketWriter1.writeObject(out);
			 SocketWriter1.flush();
			 socket1.close();
			 System.out.println("Upload Finished!");
	    	 
	    	}
		    
		
		    
			// close connection
			
			// temporary connection，need to be closed
			//in.close();
			
	
		
    	}catch(Exception e){
    		System.out.println("Upload Fialed ");
    		e.printStackTrace();
    	}
    } 
    
    
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
     
    	  while(true)  {
    		  //get command from client interface
    		 
    		  try{   
  			SocketReader = new ObjectInputStream(socket.getInputStream());
  			SocketWriter = new ObjectOutputStream(socket.getOutputStream());
		  
			  Message message= (Message)SocketReader.readObject();
		  
			   System.out.println(message.ToPackage());
			   Thread.sleep(3000);
			   Message mesout;
			   if(message.getrequest().equals("SearchFile")&&message.getFIlename()!=null){
				   System.out.println("Search from userClient.");
				   //public Message packaforsearch(String fromip, int fromport,int ttl, int sourceid, String filename, String id, String ip )
				  
				   mesout = message.packaforsearch(message.getFromip(),message.getFromport(), message.getTTL(), message.getSourceId(), message.getFIlename(), message.getid(),message.getip(),"SearchFile",message.getPort());
				   System.out.println("search begin");
				   searchFile(mesout);
				   
				   
				   
				   
				   
				   }
			   else if(message.getrequest().equals("DownloadFile")){
				   System.out.println("DownLoad a File");
				   this.DownloadFIle(message);
				   
				   
				   }
			   else if(message.getrequest().equals("UploadFile")){
				   System.out.println("Uploading a File");
				   this.UploadFile(message);}
			   
		   }catch(Exception e)
    			{
				
			}
		    
    	  }
			
		
     }
		
				
	

}
