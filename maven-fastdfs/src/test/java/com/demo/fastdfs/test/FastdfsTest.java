package com.demo.fastdfs.test;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FastdfsTest {
	private static final String upload_file = "D:\\doc\\picture\\tortoise.jpg";
	private StorageClient1 client;
	private TrackerServer trackerServer;

	@Before
	public void init() throws IOException, MyException {
		ClientGlobal.init("fdfs_client.conf");

		trackerServer = new TrackerClient().getConnection();
		client = new StorageClient1(trackerServer, null);
	}

	@Test
	public void fdfs() throws IOException, MyException {

		NameValuePair[] metaList = new NameValuePair[1];
		metaList[0] = new NameValuePair("fileName", upload_file);

		// �ϴ��ļ�
		String fileId = client.upload_file1(upload_file, null, metaList);
		System.out.println("upload success. file id is: " + fileId);

		// �����ļ�
		byte[] result = client.download_file1(fileId);
		System.out.println("download result is: " + result.length);

		// ɾ���ļ�
		int code = client.delete_file1(fileId);
		System.out.println("0 for success, none zero for fail (error code): " + code);

		closeFastdfs();
	}

	@After
	public void closeFastdfs() throws IOException {
		if (trackerServer != null) {
			trackerServer.close();
		}

	}

}
