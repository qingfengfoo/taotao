package com.taotao.common.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLDecoder;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class FastDFSClient {

	private TrackerClient trackerClient = null;
	private TrackerServer trackerServer = null;
	private StorageServer storageServer = null;
	private StorageClient1 storageClient = null;
	public FastDFSClient(String conf) throws Exception {
		if (conf.contains("classpath:")) {
			System.out.println(this.getClass().getResource("/").getPath());
			conf = conf.replace("classpath:", URLDecoder.decode(this.getClass().getResource("/").getPath(),"utf-8"));
		}
		ClientGlobal.init(conf);
		trackerClient = new TrackerClient();
		trackerServer = trackerClient.getConnection();
		storageServer = null;
		storageClient = new StorageClient1(trackerServer, storageServer);
	}
	
	/**
	 * 上传文件方法
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileName 文件全路径
	 * @param extName 文件扩展名，不包含（.）
	 * @param metas 文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
		String result = storageClient.upload_file1(fileName, extName, metas);
		return result;
	}
	
	public String uploadFile(String fileName) throws Exception {
		return uploadFile(fileName, null, null);
	}
	
	public String uploadFile(String fileName, String extName) throws Exception {
		return uploadFile(fileName, extName, null);
	}
	
	/**
	 * 上传文件方法
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileContent 文件的内容，字节数组
	 * @param extName 文件扩展名
	 * @param metas 文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {
		
		String result = storageClient.upload_file1(fileContent, extName, metas);
		return result;
	}
	
	public String uploadFile(byte[] fileContent) throws Exception {
		return uploadFile(fileContent, null, null);
	}
	
	public String uploadFile(byte[] fileContent, String extName) throws Exception {
		return uploadFile(fileContent, extName, null);
	}
	
	/**
	 * 文件的下载方法
	 * @param group fastdfs的组别
	 * @param storagePath 文件的storagePath路径
	 * @param diskpath 磁盘路径
	 * @throws Exception 
	 * @throws IOException 
	 */
	public void download_file(String path,BufferedOutputStream output) throws IOException, Exception{
		//byte[] b = storageClient.download_file(group, path);
		byte[] b = storageClient.download_file1(path);
			try{
				if(b != null){
					output.write(b);
				}
			}catch (Exception e){} //用户可能取消了下载
			finally {
				if (output != null)
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		}
	}
 /*   //将字节流写到磁盘生成文件
    private void saveFile(byte[] b,BufferedOutputStream output) {
    	
         //
		try{
			if(b != null){
				output.write(b);
			}
		}catch (Exception e){} //用户可能取消了下载
		finally {
			if (output != null)
				try {
					output.close();
			    //  storageServer.close();
				//	trackerServer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
    	
  	}*/
    
	/**
	 * 文件的删除方法
	 * 返回值：0删除成功
	 * @param group fastdfs的组名
	 * @param storagePath 文件的storagePath路径
	 * @throws Exception 
	 * @throws IOException 
	 */
	public Integer delete_file(String group ,String storagePath) throws IOException, Exception{
		 return  storageClient.delete_file(group, storagePath);
	}
}
