package com.jason.es.plugin;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.rest.RestModule;

import com.jason.es.rest.RestExceptionHandler;

public class ExceptionPlugin extends AbstractPlugin {

	private final String name = "elasticsearch-exception";
	private final String desc = "elasticsearch-exception";
	private Settings settings;

	public ExceptionPlugin(Settings settings) {
		this.settings = settings;
	}
	
	public String name() {
		return name;
	}

	public String description() {
		return desc;
	}

	public void onModule(RestModule restModule){
		restModule.addRestAction(RestExceptionHandler.class);
	}
}
