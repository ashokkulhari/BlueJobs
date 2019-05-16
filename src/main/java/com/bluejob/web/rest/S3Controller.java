package com.dufther.web.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dufther.response.DownloadResponse;
import com.dufther.security.AuthoritiesConstants;
import com.dufther.security.SecurityUtils;
import com.dufther.service.S3Services;
import com.dufther.web.rest.util.CommonHelper;
import com.dufther.web.rest.util.HeaderUtil;

 
@RestController
public class S3Controller {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private S3Services s3Services;
	
	
	// Candidate = profile_picture,address_proof,id_proof,fitness_certificate,
	
    @PostMapping("/api/file/upload/{userId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.CANDIDATE + "\")")
    public ResponseEntity<?> uploadMultipartFile(@PathVariable String userId,@RequestParam("fileName") String fileName,
    		@RequestParam("uploadfile") MultipartFile file) {
    	
    	log.debug("REST request to uploadMultipartFile : {}", userId,fileName);
    	ResponseEntity<?> entity=null;
        String msg="";boolean isUploaded=false;
    	if(SecurityUtils.isOwnerOfObjectByUserId(userId)){
    		isUploaded =s3Services.uploadFileForEntity(userId,fileName, file);
    		if(isUploaded){
    			msg="A "+fileName+" is uploaded for identifier " + userId ;
    			entity=ResponseEntity.ok().headers(HeaderUtil.createAlert( msg, userId)).build();
    		}else{
    			msg="A "+fileName+" is failed to upload for identifier " + userId ;
    			entity = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
    					.headers(HeaderUtil.createAlert( msg, userId)).build();
    		}
    	}else{
    		msg="You do not have access to upload file for identifier " + userId ;
    		entity =ResponseEntity.status(HttpStatus.FORBIDDEN)
    				.headers(HeaderUtil.createAlert( msg, userId)).build();
    	}
        return entity;
    }   
    
    /*
     * Download Files
     */
	@GetMapping("/api/file/{userId}")
	@PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.CANDIDATE + "\")")
	public ResponseEntity<?> downloadFile(@PathVariable String userId,@RequestParam("fileName") String fileName) {
		log.debug("REST request to downloadFile : {}", userId,fileName);
		ResponseEntity<?> entity=null;
        String msg="";
    	if(SecurityUtils.isOwnerOfObjectByUserId(userId)){
    		DownloadResponse downloadResponse= s3Services.downloadFile(userId,fileName);
    		if(downloadResponse.getIsSuccess()){
    			msg="A "+fileName+" is downloaded for identifier " + userId ;
    			entity =ResponseEntity.ok()
    					.contentType(CommonHelper.contentType(fileName))
    					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileName + "\"")
    					.headers(HeaderUtil.createAlert( msg, userId))
    					.body(downloadResponse.getByteArrayOutputStream().toByteArray());
    		}else{
    			msg="A "+fileName+" is failed to download for identifier " + userId +" || "+downloadResponse.getMsg();
    			entity = ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.headers(HeaderUtil.createAlert( msg, userId)).build();
    		}
    	}else{
    		msg="You do not have access to download file for identifier " + userId ;
    		entity =ResponseEntity.status(HttpStatus.FORBIDDEN)
    				.headers(HeaderUtil.createAlert( msg, userId)).build();
    	}
	
		return  entity;	
	}
	
	
}
