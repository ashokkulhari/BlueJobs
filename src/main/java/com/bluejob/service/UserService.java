package com.dufther.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dufther.config.Constants;
import com.dufther.domain.Authority;
import com.dufther.domain.User;
import com.dufther.domain.UserType;
import com.dufther.model.UserDTO;
import com.dufther.repository.ProfileStatusRepository;
import com.dufther.repository.UserJPARepository;
import com.dufther.searchrepository.UserSearchRepository;
import com.dufther.security.SecurityUtils;
import com.dufther.util.RandomUtil;
import com.dufther.web.rest.errors.EmailAlreadyUsedException;
import com.dufther.web.rest.errors.InvalidPasswordException;
import com.dufther.web.rest.errors.LoginAlreadyUsedException;


@Service
@Transactional
public class UserService {

	public static final int ACTIVATED_USER = 1;
	public static final int NOT_ACTIVATED_USER = 0;

	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	 
	@Autowired
	private UserJPARepository userRepository;
	@Autowired
	private UserSearchRepository userSearchRepository;
	
	@Autowired
	private AuthorityService authorityService;
//	@Autowired
//	 private  UserSearchRepository userRepository;
	
   @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;
   @Autowired
   private ProfileStatusRepository profileStatusRepository;
  
   

	public User saveUser(UserDTO userDTO) {
		
		userRepository.findOneByUserId(userDTO.getUserId().toLowerCase()).ifPresent(existingUser -> {
               throw new LoginAlreadyUsedException();
       });
       userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
               throw new EmailAlreadyUsedException();
       });
       User newUser = new User();
       newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
       newUser.setProfileStatus(profileStatusRepository.getOne(Constants.PROFILE_STATUS_PENDING));
       newUser.setEmail(userDTO.getEmail());
       newUser.setUserId(userDTO.getUserId());
       newUser.setLangKey(userDTO.getLangKey());
       newUser.setMobileNumber(userDTO.getMobileNumber());
       if(userDTO.getUserType()!=null && !userDTO.getUserType().name().trim().equals("")){
    	   newUser.setUserType(userDTO.getUserType()); 
       }else{
    	   newUser.setUserType(UserType.USER);
       }
       
       Set<Authority> roles = authorityService.findAllRoleById(userDTO.getRoleId()).stream().collect(Collectors.toSet());
       newUser.setAuthorities(roles);
       /*  Set<Permission> permissions = new HashSet<>();
       Permission rolePermission = permissionService.findPermissionById(appData.getPermissionId());
       permissions.add(rolePermission);
       userRole.setPermissions(permissions);*/
       
       newUser.setActivationKey(RandomUtil.generateActivationKey());
		userRepository.save(newUser);
		userSearchRepository.save(newUser);
//		mailService.sendActivationMail(user);
		 log.debug("Created Information for User: {}", newUser);
		 return newUser;
	}

	
	
	public List<String> getAuthoritiesByEmail(String email) {
		return userRepository.getAuthoritiesByEmail(email);
	}


	public User findUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<User> findAll() {
		
	    List<User> usersList = new ArrayList<>();
        Iterable<User> userses = userSearchRepository.findAll();
        userses.forEach(usersList::add);
        return usersList;
        
	}


	public User findUserByEmail(String email) {
		return userSearchRepository.findByEmail(email);
	}

	public Optional<User> findOneByUserId(String userId){
		log.debug("Get user for userId {}", userId);
	       return userRepository.findOneByUserId(userId);
	}
	
	public Optional<User> activateRegistration(String key) {
       log.debug("Activating user for activation key {}", key);
       return userRepository.findOneByActivationKey(key)
           .map(user -> {
               // activate given user for the registration key.
               user.setProfileStatus(profileStatusRepository.getOne(Constants.PROFILE_STATUS_ACTIVE));
               user.setActivationKey(null);
//               userRepository.save(user);
               log.debug("Activated user: {}", user);
               return user;
           });
   }
	
	
	public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);
       return userRepository.findOneByResetKey(key)
           .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
           .map(user -> {
               user.setPassword(bCryptPasswordEncoder.encode(newPassword));
               user.setResetKey(null);
               user.setResetDate(null);
               return user;
           });
   }
	
	public Optional<User> requestPasswordReset(String mail) {
       return userRepository.findOneByEmailIgnoreCase(mail)
           .filter(d-> d.getProfileStatus().getProfileStatusId().equals(Constants.PROFILE_STATUS_ACTIVE))
           .map(user -> {
               user.setResetKey(RandomUtil.generateResetKey());
               user.setResetDate(Instant.now());
               return user;
           });
   }

	
   /**
    * Update basic information (first name, last name, email, language) for the current user.
    *
    * @param firstName first name of user
    * @param lastName last name of user
    * @param email email id of user
    * @param langKey language key
    * @param imageUrl image URL of user
    */
   public void updateUser(String email, String langKey) {
       SecurityUtils.getCurrentUserLogin()
           .flatMap(userRepository::findOneByUserId)
           .ifPresent(user -> {
               user.setEmail(email.toLowerCase());
               user.setLangKey(langKey);
//               user.setImageUrl(imageUrl);
//               userRepository.save(user);
               log.debug("Changed Information for User: {}", user);
           });
   }
   
   @Transactional
   public void deleteUser(String userId) {
       userRepository.findOneByUserId(userId).ifPresent(user -> {
           userRepository.delete(user);
           userSearchRepository.delete(user);
           log.debug("Deleted User: {}", user);
       });
   }
   public void changePassword(String currentClearTextPassword, String newPassword) {
       SecurityUtils.getCurrentUserLogin()
           .flatMap(userRepository::findOneByUserId)
           .ifPresent(user -> {
               String currentEncryptedPassword = user.getPassword();
               if (!bCryptPasswordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                   throw new InvalidPasswordException();
               }
               String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
               user.setPassword(encryptedPassword);
               log.debug("Changed password for User: {}", user);
           });
   }
   

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthoritiesByUserId(String login) {
       return userRepository.findOneWithAuthoritiesByUserId(login);
   }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthorities(Long id) {
       return userRepository.findOneWithAuthoritiesById(id);
   }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthorities() {
       return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByUserId);
   }

   @Transactional(readOnly = true)
   public Page<UserDTO> getAllManagedUsers(Pageable pageable) {	  
       return userRepository.findAllByUserIdNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
   }

   public Optional<User> findOneByEmailIgnoreCase(String email) {
	return userRepository.findOneByEmailIgnoreCase(email);
   }


	public Optional<String> getUserTypeByUserId(String userId){
		return userRepository.getUserTypeById(userId);
	}
}
