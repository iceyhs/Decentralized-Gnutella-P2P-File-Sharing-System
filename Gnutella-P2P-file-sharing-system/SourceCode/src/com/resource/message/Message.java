package com.resource.message;

import java.io.Serializable;
import com.resource.Peer;

/*
 *Parse message  
 */
@SuppressWarnings("serial")
public class Message implements Serializable{
	protected String fromip;
	protected int fromport;
	protected int port;
	protected String ip;
	protected String id;
	//protected String fromid;
	protected String filename;
	protected int sourceId;
	protected int TTL =	7;
	protected String requestFunction;
	public int[] bytenumber;
	public byte[][] bytetemp;
	
	public int getSourceId(){
		return this.sourceId;
	}
	
	public void setSourceId(int sourceId){
		this.sourceId=sourceId;
		
	}
	
	public int getTTL(){
		return this.TTL;
	}
	
	public void setTTL(int ttl){
		this.TTL=ttl;
	}
	
	public void setid(String id){
		this.id=id;
	}
	
	public String getid(){
		return id;
	}
	
	public void setip(String ip){
		this.ip=ip;
	}
	
	public String getip(){
		return ip;
	}
	
	public void setFilename(String filename){
		this.filename=filename;
	}
	
	public String getFIlename(){
		return filename;
		
		
	}
	public void setrequest(String RequestFunction){
		this.requestFunction=RequestFunction;
	}
	
	public String getrequest(){
		return requestFunction;
	}
	public int getPort(){
		return port;
	} 
	public void setPort(int port){
		this.port=port;
		
	}
	
	public void setFromip(String fromip){
		this.fromip=fromip;		
	}
	
	public String getFromip(){
		return fromip;
	}
	
	public void setFromport(int fromport){
		this.fromport=fromport;
	}
	
	public int getFromport(){
		 return fromport;
	}
	

	
	
	
	
	
	

	
	
	/*
	 * Parse message
	 */
	
	public Message(String mes){
		if(mes==null || mes.length()<1){
			return;
		}
		// eg: SearchFile=filename;
		// DownLoadFile=filename,ip,port
		try{
			String request = mes.split("=")[0]; //requestfunction
			String list[] = mes.split("=")[1].split(","); 
			if(request.equals("SearchFile")){
				this.requestFunction="SearchFile";
				this.setFilename(list[0]);
			}
			if(request.equals("DownloadFile")){
				this.requestFunction="DownloadFile";
				this.setFilename(list[0]);
				this.setFromip(list[1]);;
				this.setFromport(Integer.parseInt(list[2]));
			}
						
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Input Error!!");
		}
		
	}	
	
	  /*
	   * return : Download:filename=XXX, From IP=XXX, From Port=XXX
	   */
	
	
		public String ToPackage() {
			
			StringBuffer sb = new StringBuffer(100);
			sb.append(this.requestFunction);
			sb.append("=");
			if(this.getFIlename()!=null){
				sb.append(this.getFIlename());
			}
			if(this.getip()!=null){
				sb.append("," + this.getip());
				sb.append("," + this.getPort());
			}
		return sb.toString();
		}
		
	    public Message(String fromip, int fromport,int ttl, int sourceid, String filename,String id,String ip,String RequestFunc,int port){
			
			this.fromip=fromip;
			this.fromport=fromport;
			this.TTL=ttl;
			this.sourceId=sourceid;
			this.filename=filename;
			this.id=id;
			this.ip=ip;
			this.requestFunction=RequestFunc;
			this.port=port;
			
			
		}
		
		
		public Message packaforsearch(String fromip, int fromport,int ttl, int sourceid, String filename, String id, String ip,String RequestFunc,int port ){
			
			Message mes= new Message(fromip,fromport,ttl,sourceid,filename,id,ip,RequestFunc,port);
			
			return mes;
		}

     
		public Message(String filename, String ip, int port,int[] bytenumber, byte[][] bytetemp, String RequestFunction, String fromip, int fromport ){
			
			this.filename=filename;
			this.ip=ip;
			this.port=port;
			this.requestFunction=RequestFunction;
			this.bytenumber=bytenumber;
			this.bytetemp=bytetemp;
			this.fromip=fromip;
			this.fromport=fromport;
			
			
			
		}
	     public Message packagefordownload(String filename, String toip, int toport,int[] bytenumber, byte[][] bytetemp, String RequestFunction, String fromip, int fromport){
	    	 Message mes = new Message(filename,ip,port,bytenumber,bytetemp,RequestFunction,fromip,fromport);
	    	 return mes;
	    	 
	     }

	
	
	  //   public Message packageforupload(String filename, String ip,byte[][] bytetemp, String RequestFunction){
	    	// Message mes = new Message(filename,ip,port,RequestFunction);
	    	// return mes;
	    	 
	   //  }
	
	
	
	
    
}