package com.ehl.tvc.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "pullconfig")
public class PullConfig {
	private Map<String, String> configMap;

	public Map<String, String> getConfigMap() {
		return configMap;
	}
	
	private HashMap<String, String> heads;

	public void setConfigMap(Map<String, String> configMap) {
		this.configMap = configMap;
	}

	public String   getConfig4Key(String key){
		return configMap.get(key);
	}
	public Integer getConfig4KeyInt(String key){
		return Integer.valueOf(configMap.get(key));
	}
	
	public HashMap<String, String> getHeads() {
		return heads;
	}

	public void setHeads(HashMap<String, String> heads) {
		this.heads = heads;
	}
}
