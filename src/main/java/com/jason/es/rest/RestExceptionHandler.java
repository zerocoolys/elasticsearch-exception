package com.jason.es.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.StringRestResponse;
import org.elasticsearch.rest.XContentThrowableRestResponse;

public class RestExceptionHandler extends BaseRestHandler {

	@Inject
	protected RestExceptionHandler(Settings settings, Client client,
			RestController controller) {
		super(settings, client);

		controller.registerHandler(RestRequest.Method.GET, "/_exception", this);
	}

	public void handleRequest(RestRequest request, RestChannel channel) {
		String logPath = settings.get("path.logs");
		String nodeName = settings.get("cluster.name");
		File logFile = new File(logPath + File.separator + nodeName + ".log");
		FileChannel fileChannel = null ;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(logFile);
			fileChannel = fis.getChannel();

			ByteBuffer buffer = ByteBuffer.allocateDirect(128 * 1024);
			StringBuilder sb = new StringBuilder();

			long len = 0;
			while ((len = fileChannel.read(buffer)) != -1) {
				buffer.flip();
				sb.append(buffer.array());
				buffer.clear();
			}
			buffer.clear();
			channel.sendResponse(new StringRestResponse(RestStatus.OK, sb
					.toString()));
		} catch (Exception e) {
			try {
				channel.sendResponse(new XContentThrowableRestResponse(request, e));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally{
			if(fileChannel != null)
				try {
					fileChannel.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(fis != null){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		}
	}

	
}
