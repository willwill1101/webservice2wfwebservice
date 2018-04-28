package com.ehl.tvc.schedule.work.pull;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ehl.tvc.common.DictManager;
import com.ehl.tvc.common.HttpUtil;
import com.ehl.tvc.config.PullConfig;
import com.ehl.tvc.schedule.work.QueueWork;
import com.google.gson.Gson;
@Component
public class PullWorkImp extends  Thread {  
	private Logger log = LogManager.getLogger(PullWorkImp.class);
	// 抓取到的数据放在队列中
	@Autowired
	private QueueWork queueWork;
	// 加载的配置文件
	@Autowired
	private PullConfig pullConfig;
	// 抓取数据的url
	private String pullUrl;
	// 每一批次抓取的数据数量
	private String batchSize;
	// 过车记录的开始id
	private String startId;
	// 过车记录开始id记录的文件地址
	private String startIdFile;

	private RandomAccessFile startRandom;
	
	
	
	@Autowired
	private HttpUtil httpUtil;

	@PostConstruct
	public void init() {
		pullUrl = pullConfig.getConfig4Key("url");
		batchSize = pullConfig.getConfig4Key("batchSize");
		startIdFile = pullConfig.getConfig4Key("startIdFile");
		String fileUrl = null;
		try {
			fileUrl = this.getClass().getResource(startIdFile).toURI().getPath();
			startRandom = new RandomAccessFile(this.getClass().getResource(startIdFile).toURI().getPath(), "rw");
		} catch (Exception e) {
			log.error("加载开始记录配置文件报错：" + fileUrl, e);
			System.exit(0);
		}
		//开始调度
		this.start();

	}

	@Override
	public void run() {
		Gson gson = new Gson();
		
		while(true){
			//读取开始id
			startId = readStartId();
			try {
				String url = null;
				if(pullConfig.getConfig4Key("urlencode") != null&&"true".equals(pullConfig.getConfig4Key("urlencode")) ){
					
					 url = pullUrl+java.net.URLEncoder.encode("{\"startId\":"+startId+",\"page\":{\"pageNo\":1,\"pageSize\":"+batchSize+"}}");
				}else{
					
				 url = pullUrl+"{\"startId\":"+startId+",\"page\":{\"pageNo\":1,\"pageSize\":"+batchSize+"}}";
				}
				log.info("获取数据url:"+url);
				//获取json内容
				
				HttpGet post = new HttpGet(url);
				Map<String, String> postParam = pullConfig.getHeads();
				if (postParam != null) {
					for (String key : postParam.keySet()) {
						post.setHeader(key, postParam.get(key));
					}
				}
//				HttpEntity entity = new StringEntity(message, "UTF-8");
//				post.SET
				HttpResponse response = httpUtil.execute(post);
				String responseText = null;
				if (response != null) {
					responseText = IOUtils.toString(response.getEntity().getContent(),"UTF-8");
				}
				post.releaseConnection();
				log.info("接收数据内容："+responseText);
				//解析内容
				PullResultBean resultBean = gson.fromJson(responseText,PullResultBean.class);
				List<Row> list =  resultBean.getData().getRows();
				//执行字典翻译
				translateList(list);
				//提交到发送队列中
				queueWork.add(list);
				Long maxId = 0l;
				for(Row r :list){
					Long id = r.getId();
					if(maxId<id){
						maxId=id;
					}
				}
				//写入startid
				if(list.size()>0){
					writeStartId(""+(maxId+1));
				}
			} catch (Exception e) {
				log.error("解析内容错误：",e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}

	private String readStartId() {
		try {
			startRandom.seek(0);
			String l = startRandom.readLine();
			return l;
		} catch (IOException e) {
			log.error("读取startid报错", e);
			System.exit(0);
		}
		return null;
	}

	private void writeStartId(String startId) throws IOException {
		try {
			startRandom.seek(0);
			startRandom.write(startId.getBytes("UTF-8"));
		} catch (Exception e) {
			log.error("写入startid报错", e);
			System.exit(0);
		}
	}
	
	
	private void translateList(List<Row> list){
		for(Row r:list){
			String cartype = DictManager.converName2Code(DictManager.DIC_HPZL+r.getCarType());
			if(cartype==null){
				cartype="99";
			}
			r.setCarType(cartype);
			
			String numc = DictManager.converName2Code(DictManager.DIC_HPYS+r.getCarNumColor());
			if(numc==null){
				numc="4";
			}
			r.setCarNumColor(numc);
			
			String carColor = DictManager.converName2Code(DictManager.DIC_CSYS+r.getCarColor());
			if(carColor==null){
				carColor="Z";
			}
			r.setCarColor(carColor);
			
			String carDirect = DictManager.converName2Code(DictManager.DIC_CXFX+r.getCarDirect());
			if(carDirect==null){
				carDirect="99";
			}
			r.setCarDirect(carDirect);
			
			
			String rectype = DictManager.converName2Code(DictManager.DIC_WFXW+r.getRecType());
			if(rectype==null){
				rectype = DictManager.converName2Code(DictManager.DIC_WFXW+"default");
			}
			r.setRecType(rectype);
			
			
			String wfdd = DictManager.converName2Code(DictManager.DIC_WFDD+r.getDevChnId());
			if(wfdd==null){
				wfdd = DictManager.converName2Code(DictManager.DIC_WFDD+"default");
			}
			r.setWFDD(wfdd);
			
			String cjjg = DictManager.converName2Code(DictManager.DIC_CJJG+wfdd);
			if(cjjg==null){
				wfdd = DictManager.converName2Code(DictManager.DIC_CJJG+"default");
			}
			r.setCJJG(cjjg);
			
		}
	}

}
