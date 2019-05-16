package com.bluejob.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.bluejob.domain.UserType;
import com.bluejob.response.DownloadResponse;
import com.bluejob.web.rest.errors.InternalServerErrorException;
import com.bluejob.web.rest.util.CommonHelper;

@Service
public class S3Services {

private Logger logger = LoggerFactory.getLogger(S3Services.class);
	
	@Autowired
	private AmazonS3 s3client;
	@Autowired 
	private UserService userService;
	
	@Autowired
	private CandidateService candidateService;
	
	@Value("${bluejob.s3.bucket}")
	private String bucketName;
 
	 private static final String SEPERATOR = "/";
	 
	public DownloadResponse downloadFile(String keyName,String fileName) {
		DownloadResponse downloadResponse = new DownloadResponse();
		downloadResponse.setIsSuccess(false);
		try {
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName +SEPERATOR +fileName));
            
            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            
            downloadResponse.setIsSuccess(true);
            downloadResponse.setByteArrayOutputStream(baos);
		} catch (IOException ioe) {
			downloadResponse.setMsg(ioe.getMessage());
			logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException ase) {
			downloadResponse.setMsg(ase.getStatusCode()+" "+ase.getMessage());
        	logger.info("sCaught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
//			throw ase;
        } catch (AmazonClientException ace) {
        	downloadResponse.setMsg("Caught an AmazonClientException: " +ace.getMessage());
        	logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }
		
		return downloadResponse;
	}
 
	/**
     * Create S3 bucket folder if parent does not exists
     * 
     * @param parentFolder
     *            parent
     * @return success/failure
     */
    public boolean createFolder(final String parentFolder) {
        // create meta-data for your folder and set content-length to 0
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        // create empty content
        final InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        s3client.putObject(new PutObjectRequest(bucketName, parentFolder, emptyContent, metadata)
                .withCannedAcl(CannedAccessControlList.BucketOwnerFullControl));
        return objectExists(parentFolder);
    }
    public boolean objectExists(final String objectName) {
        return s3client.doesObjectExist(bucketName, objectName);
    }
    
	// keyname will be used here as file name.
	public boolean uploadFile(String userId,String fileName, MultipartFile file) {
		boolean isUploaded=false;
		try {
			logger.info("AWS s3 uploadFile , Name "+file.getName() +" ,OriginalFilename = "+file.getOriginalFilename());
			 boolean exists = validateUserFolderOrCreate(userId);
	            if (exists) {
	            	String newFileName=fileName+"."+FilenameUtils.getExtension(fileName);
	            	ObjectMetadata metadata = new ObjectMetadata();
	    			metadata.setContentLength(file.getSize());
	    			s3client.putObject(bucketName+ SEPERATOR + userId, newFileName, 
	    					file.getInputStream(), metadata);
	    			isUploaded=true;
	            }else{
	            	logger.info("No Folder found or created for userId "+ userId );
	            }
			
		} catch(IOException ioe) {
			logger.error("IOException: " + ioe.getMessage());
		} catch (AmazonServiceException ase) {
			logger.debug("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.debug("Error Message:    " + ase.getMessage());
			logger.debug("HTTP Status Code: " + ase.getStatusCode());
			logger.debug("AWS Error Code:   " + ase.getErrorCode());
			logger.debug("Error Type:       " + ase.getErrorType());
			logger.debug("Request ID:       " + ase.getRequestId());
			throw ase;
        } catch (AmazonClientException ace) {
            logger.debug("Caught an AmazonClientException: ");
            logger.debug("Error Message: " + ace.getMessage());
            throw ace;
        }
		return isUploaded ;
	}

	public boolean validateUserFolderOrCreate(String userId) {
		boolean exists = s3client.doesObjectExist(bucketName, userId);
		    if (exists) {	logger.info("Object \"" + bucketName + "/" + userId + "\" exists!");
		    }else {	logger.info("Object \"" + bucketName + "/" + userId + "\" does not exist!");
		    exists =createFolder(userId);//create folder for user by user Id
		    }
		return exists;
	}
	public boolean uploadFileForEntity(String userId,String fileName, MultipartFile file){
		Optional<String> userType =userService.getUserTypeByUserId(userId);
		boolean isUpdated=false;
		if(userType.isPresent()){
			if(userType.get().equals(UserType.CANDIDATE.name())){
				isUpdated = candidateService.updateCandidateProfilePic(userId, fileName);
			}
			if(isUpdated){
				isUpdated = uploadFile(userId,fileName,file);
			}
		}else{
			throw new InternalServerErrorException("UserType not found for userId "+userId);
		}
			
		return isUpdated;
	}
	
}