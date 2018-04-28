package com.ehl.tvc.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DictManager {
	private Logger log = LogManager.getLogger(DictManager.class);
	@Autowired
	private SqlSessionFactory sessionFactory;
	private static Map<String ,String> dicMap = null;
	
	
	public static String DIC_CLTXZT = "260004_";// 车辆通行状态
	public static String DIC_HPZL = "261006_";// 号牌种类
	public static String DIC_HPYS = "261005_";// 号牌颜色
	public static String DIC_CXFX = "261004_";// 车行方向
	public static String DIC_CDBH = "260010_";// 车道编号
	public static String DIC_ZPFX = "260003_";// 抓拍方向
	public static String DIC_CSYS = "261003_";// 车身颜色
	public static String DIC_CLPP = "261012_";// 车辆品牌
	
	public static String DIC_WFXW = "WFXW_";//违法行为
	public static String DIC_WFDD="WFDD_"; //违法地点
	public static String DIC_CJJG= "CJJG_"; //创建机构
	
	@PostConstruct
	public void init(){
		SqlSession session = sessionFactory.openSession();
		dicMap=new HashMap<>();
		List<Map<String, String>> datas = null;
		try {
			datas = session.selectList("tvcDic.select");
			log.info("查询字典总数："+datas.size());
			for(Object  o :datas){
				Map<String, String > map = (Map<String, String>) o;
				dicMap.put(map.get("FROMCODE"), map.get("TOCODE"));
			}
		} catch (Exception e) {
			log.error("数据获取异常!!!", e);
		}finally{
			session.close();
		}
	}
	
	
	public static String converName2Code(String name){
		return dicMap.get(name);
	}
}
