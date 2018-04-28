package com.ehl.tvc.schedule.work.push;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.ehl.tvc.schedule.task.PushThread;
import com.ehl.tvc.schedule.work.QueueWork;

import freemarker.template.Template;

@Component
public class PushWorkImp extends Thread  {
	public static Log log = LogFactory.getLog(PushWorkImp.class);

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private QueueWork queueWork;
	@Autowired
	private Template template;

	@PostConstruct
	public void init() {
		/*
		 * 启动读取文件读取线程
		 * 
		 */
		this.start();

	}

	@Override
	public void run() {
		while (true) {
			try {
				Object O= queueWork.task();
				PushThread basicTask = applicationContext.getBean(PushThread.class);
				basicTask.setPushThread(template, O);
				threadPoolTaskExecutor.execute(basicTask);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	

	
	
	




	

}
