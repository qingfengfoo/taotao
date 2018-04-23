package com.taotao.fastdfs;

import java.io.File;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastdfsTest {

	@Test
	public void testUpload() throws IOException, MyException {
		//初始化全局配置
		ClientGlobal.init("D:\\doc\\taotao\\workspace\\taotao-manager\\taotao-manager-web\\src\\main\\resources\\properties\\client.conf");
		//创建一个TrackerClient对象
		TrackerClient client=new TrackerClient();
		//创建一个TrackerServer对象
		TrackerServer server = client.getConnection();
		//声明一个StorageServer对象，null
		StorageServer storageServer=null;
		//获得StorageClient对象
		StorageClient storageClient=new StorageClient(server, storageServer);
		String[] strings = storageClient.upload_file("D:\\doc\\picture\\0HZP45790.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
		
		
	}
	
	@Test
	public void testFastDFSClient() throws Exception {
		FastDFSClient client=new FastDFSClient("D:\\doc\\taotao\\workspace\\taotao-manager\\taotao-manager-web\\src\\main\\resources\\properties\\client.conf");
		String uploadFile = client.uploadFile("D:\\doc\\picture\\u=2852854259,2515271574&fm=27&gp=0.jpg","jpg");
		System.out.println(uploadFile);
	}
}
