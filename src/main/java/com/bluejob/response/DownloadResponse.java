package com.bluejob.response;

import java.io.ByteArrayOutputStream;

import lombok.Data;

@Data
public class DownloadResponse {

	
	private Boolean isSuccess;
	
	private ByteArrayOutputStream byteArrayOutputStream;
	
	private String msg;
}
