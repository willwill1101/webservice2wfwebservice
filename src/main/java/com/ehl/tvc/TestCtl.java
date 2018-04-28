package com.ehl.tvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ehl.tvc.schedule.work.pull.PullResultBean;
import com.ehl.tvc.schedule.work.pull.ResultData;
import com.ehl.tvc.schedule.work.pull.Row;

@RestController
public class TestCtl {
	private Logger log =LogManager.getLogger(TestCtl.class);
	private static Long id = 0l;
	@RequestMapping("/dahuaIS/picrecord/search*")
	@ResponseBody
	public PullResultBean  makeData(String q){
		log.info("测试请求q："+q);
		PullResultBean bean  = new PullResultBean();
		ResultData data =  new ResultData();
		List<Row> list = new ArrayList<>();
		for(int i=0;i<10;i++){
			id++;
			Row r = new Row();
			r.setId(id);
			r.setDevChnName("eeeee");
			r.setCarNum("津HLD066");
			r.setCarNumType("0");
			r.setCarNumColor("1");
			r.setCarSpeed("175");
			r.setCarType("2");
			r.setCarColor("1");
			r.setCarDirect("3");
			r.setCarWayCode("2");
			r.setCarImgUrl("https://wefe");
			r.setCarNumPic("http://lsjeifjiejf:sefjiejfiejs");
			r.setCombinedPicUrl("http://lsjeifjiejf:sefjiejfiejs");
			r.setDevChnId("testcode");
			r.setCapDate("2012-10-25 12:29:13");
			list.add(r);
		}
		bean.setData(data);
		data.setRows(list);
		return bean;
	}
	
	
	@RequestMapping("/tvc-webservice/services/TgsService")
	@ResponseBody
	public String  webservicetest( HttpServletRequest request){
		try {
			log.info("测试接口接收数据："+IOUtils.toString( request.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ok";
	}
	
	

}
