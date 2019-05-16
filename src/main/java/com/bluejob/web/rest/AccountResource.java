package com.dufther.web.rest;



import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dufther.domain.User;
import com.dufther.model.PasswordChangeDTO;
import com.dufther.model.UserDTO;
import com.dufther.repository.UserJPARepository;
import com.dufther.security.SecurityUtils;
import com.dufther.service.MailService;
import com.dufther.service.UserService;
import com.dufther.web.rest.errors.EmailAlreadyUsedException;
import com.dufther.web.rest.errors.EmailNotFoundException;
import com.dufther.web.rest.errors.InternalServerErrorException;
import com.dufther.web.rest.errors.InvalidPasswordException;
import com.dufther.web.rest.errors.LoginAlreadyUsedException;
import com.dufther.web.rest.util.CommonHelper;
import com.dufther.web.rest.vm.KeyAndPasswordVM;
import com.dufther.web.rest.vm.ManagedUserVM;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserJPARepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserJPARepository userRepository, UserService userService, MailService mailService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * POST  /register : register the user.
     *
     * @param managedUserVM the managed user View Model
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already used
     */
       
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<?>  registerAccount(@Valid @RequestBody UserDTO managedUserVM) {
        ResponseEntity<?> entity = null;
        
            try {
            	if (!CommonHelper.checkPasswordLength(managedUserVM.getPassword())) {
                    throw new InvalidPasswordException();
                }
            	User user = userService.saveUser(managedUserVM);
                mailService.sendActivationEmail(user);
                entity=CommonHelper.customResponse(true , "Success" , HttpStatus.CREATED,managedUserVM.getUserId());
            } catch(LoginAlreadyUsedException e){
            	entity =CommonHelper.customResponse(false , "User already exist "+e.getMessage() , HttpStatus.FAILED_DEPENDENCY,managedUserVM.getUserId());
            }catch(EmailAlreadyUsedException e){
            	entity=CommonHelper.customResponse(false ,"Email already exist "+e.getMessage() , HttpStatus.FAILED_DEPENDENCY,managedUserVM.getUserId());
            }catch (Exception e) {
            	entity=CommonHelper.customResponse(false ,"Exception : "+e.getMessage() , HttpStatus.EXPECTATION_FAILED,managedUserVM.getUserId());
                e.printStackTrace();
            }
        
        return entity;
    }
    
    
    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this activation key");
        }
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
//    @GetMapping("/account")
//    public UserDTO getAccount() {
//        return userService.getUserWithAuthorities()
//            .map(User::new)
//            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
//    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws RuntimeException 500 (Internal Server Error) if the user login wasn't found
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getUserId().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByUserId(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUser( userDTO.getEmail(),userDTO.getLangKey());
    }

    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param passwordChangeDto current and new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!CommonHelper.checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * POST   /account/reset-password/init : Send an email to reset the password of the user
     *
     * @param mail the mail of the user
     * @throws EmailNotFoundException 400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
       mailService.sendPasswordResetMail(
           userService.requestPasswordReset(mail)
               .orElseThrow(EmailNotFoundException::new)
       );
    }

    /**
     * POST   /account/reset-password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws RuntimeException 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!CommonHelper.checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
    }

    
}
