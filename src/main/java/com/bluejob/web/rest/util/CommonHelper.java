package com.bluejob.web.rest.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.ResponseEntity.HeadersBuilder;

import com.bluejob.web.rest.vm.ManagedUserVM;

public class CommonHelper {

	
	
	public static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
	
	public static ResponseEntity<?> customResponse(
			Object output , String msg ,HttpStatus status,String param ) {
		ResponseEntity<?> entity= ResponseEntity.status(status)
		.headers(HeaderUtil.createAlert( msg, param))
		.body(output);
		return entity ;
	}
	public static ResponseEntity<?> createEntityResponse(
			Object output , String msg ,HttpStatus status,String param ) {
		ResponseEntity<?> entity= ResponseEntity.status(status)
		.headers(HeaderUtil.createEntityCreationAlert( msg, param))
		.body(output);
		return entity ;
	}
	public static ResponseEntity<?> updateEntityResponse(
			Object output , String msg ,HttpStatus status,String param ) {
		ResponseEntity<?> entity= ResponseEntity.status(status)
		.headers(HeaderUtil.createEntityUpdateAlert( msg, param))
		.body(output);
		return entity ;
	}
	public static ResponseEntity<?> deleteEntityResponse(
			Object output , String msg ,HttpStatus status,String param ) {
		ResponseEntity<?> entity= ResponseEntity.status(status)
		.headers(HeaderUtil.createEntityDeletionAlert( msg, param))
		.body(output);
		return entity ;
	}
	public static MediaType contentType(String fileName) {
		String[] arr = fileName.split("\\.");
		String type = arr[arr.length-1];
		switch(type) {
			case "txt": return MediaType.TEXT_PLAIN;
			case "png": return MediaType.IMAGE_PNG;
			case "jpg": return MediaType.IMAGE_JPEG;
			default: return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
	
	public static String fileExtension(String fileName) {
		String[] arr = fileName.split("\\.");
		String type = arr[arr.length-1];
		return type;
	}
}
