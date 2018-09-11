package com.cb.producer;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProducerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	String content = "";
	String path = "";
	File file = null;
	@Before
	public void setUp() throws Exception {
		BASE64Encoder encoder = new BASE64Encoder();
		content = encoder.encode("I had to keep breathing,even though there was no reason to hope.".getBytes());
		path = "C:\\Users\\cb\\Documents\\WXWork\\1688853601350164\\Cache\\File\\2018-08\\1111\\temp";
		file = new File(path).listFiles()[new Random().ints(1,2000).findFirst().getAsInt()];
	}

	/**
	 * 向"/upload"地址发送请求，并打印返回结果
	 * @throws Exception
	 */
	@Test
	public void testUpload1() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		FileSystemResource fileSystemResource = new FileSystemResource(file);
		MediaType type = MediaType.parseMediaType("multipart/form-data");
		headers.setContentType(type);
		MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
		form.add("file", fileSystemResource);
		HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
		String response = restTemplate.postForObject("http://localhost:"+ port + "/upload", files, String.class);
		System.out.println("上传结果为：%s" + response);
		JSONObject js = new JSONObject(response);
		if(js.getInt("result") == 0) {
			response = restTemplate.exchange("http://localhost:"+ port + "/delete/name/" + file.getName(), HttpMethod.DELETE,null,String.class).getBody();
			System.out.println("删除结果为：%s" + response);
		}

	}

	/**
	 * 向"/upload/string"地址发送请求，并打印返回结果
	 * @throws Exception
	 */
	@Test
	public void testUpload2() throws Exception {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("fileName", LocalDateTime.now() + "ForTest.txt");
		map.put("fileData", content);
		String response = restTemplate.postForObject("http://localhost:"+ port + "/upload/string", map, String.class);
		System.out.println("测试结果为：%s" + response);
	}
}
