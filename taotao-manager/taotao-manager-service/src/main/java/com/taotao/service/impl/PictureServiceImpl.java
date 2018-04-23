package com.taotao.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.pojo.PictureResult;
import com.taotao.common.utils.FastDFSClient;
import com.taotao.service.PictureService;
/**
 * t图片上传service
 * @author Administrator
 *
 */
@Service
public class PictureServiceImpl implements PictureService {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@Override
	public PictureResult uploadPic(MultipartFile picFile) {
		PictureResult pictureResult=new PictureResult();
		
		//判断图片是否为空
		if(picFile.isEmpty()){
			pictureResult.setError(1);
			pictureResult.setMessage("图片为空");
			return pictureResult;
		}
		//上传到图片服务器
		try {
			//取图片扩展名
			String originalFilename = picFile.getOriginalFilename();
			//取扩展名不要.
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			FastDFSClient client=new FastDFSClient("classpath:properties/client.conf");
			String url = client.uploadFile(picFile.getBytes(),extName);
			//拼接图片服务器的IP地址
			url=IMAGE_SERVER_URL+url;
			//把url响应给客户端
			pictureResult.setError(0);
			pictureResult.setUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
			pictureResult.setError(0);
			pictureResult.setMessage("图片上传失败");
		}
		return pictureResult;
	}

}
