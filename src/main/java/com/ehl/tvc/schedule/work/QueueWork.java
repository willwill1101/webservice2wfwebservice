package com.ehl.tvc.schedule.work;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ehl.tvc.config.ApplicationConfig;

@Component
public class QueueWork {
	private static Logger log = LogManager.getLogger(QueueWork.class);
	@Autowired
	private ApplicationConfig appconfig;
	private LinkedBlockingQueue outputQueue;
	private int addDataCount=0;
	private int taskDataCount=0;

	@PostConstruct
	public void init() {
		outputQueue = new LinkedBlockingQueue<>(appconfig.getQueueSize());
		// 启动打印日志
		new LogExecut().start();
	}

	public void add(List list) {
		if (list != null && list.size() > 0) {
			for (Object o : list) {
				try {
					outputQueue.put(o);
					addDataCount++;
				} catch (Exception e) {
					log.error("添加对象报错：", e);
				}
			}
		}
	}

	public Object task() {
		try {
			Object map = outputQueue.take();
			taskDataCount++;
			return map;
		} catch (Exception e) {
			log.error("从队列获取对象报错：", e);
		}
		return null;
	}

	class LogExecut extends Thread {
		//线程睡眠时间 设置 5000
		private long sleepTime =5000;
		@Override
		public void run() {
			while(true){
				log.info( MessageFormat.format("每{0}毫秒,进队列{1}条,出队列{2},队列总数{3}",sleepTime,addDataCount,taskDataCount,outputQueue.size()));
				addDataCount=0;
				taskDataCount=0;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
