package com.ehl.tvc.schedule.task;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ehl.tvc.common.HttpUtil;
import com.ehl.tvc.config.PushConfig;

import freemarker.template.Template;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PushThread implements Runnable {

	private   Logger log = LogManager.getLogger(PushThread.class);
	@Autowired
	private HttpUtil httpUtil;
	private Template template;
	private Object data;
	@Autowired
	private PushConfig pushconfig;
	
	public void setPushThread(Template template, Object data) {
		this.template = template;
		this.data = data;
	}

	@Override
	public void run() {
		try {

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			template.process(data, new OutputStreamWriter(out));
			String message = IOUtils.toString(out.toByteArray(), "UTF-8");
			out.close();
			log.info("准备发送数据:" + message);
			String url = pushconfig.getConfig4Key("url");
			HttpPost post = new HttpPost(url);
			Map<String, String> postParam = pushconfig.getHeads();
			if (postParam != null) {
				for (String key : postParam.keySet()) {
					post.setHeader(key, postParam.get(key));
				}
			}
			HttpEntity entity = new StringEntity(message, "UTF-8");
			post.setEntity(entity);
			HttpResponse response = httpUtil.execute(post);
			String responseText = null;
			if (response != null) {
				responseText = IOUtils.toString(response.getEntity().getContent());
			}
			post.releaseConnection();
			log.info("返回数据为:" + responseText);
		} catch (Exception e) {
			log.error("IO异常!!!", e);
		} finally {
		}

	}

	
	





}
