package com.dufther.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.dufther.config.AuditEventPublisher;
import com.dufther.config.Constants;
import com.dufther.domain.User;
import com.dufther.model.LoginModel;
import com.dufther.model.UserDTO;
import com.dufther.response.JwtResponse;
import com.dufther.searchrepository.UserSearchRepository;
import com.dufther.security.AuthoritiesConstants;
import com.dufther.security.SecurityUtils;
import com.dufther.security.jwt.JwtProvider;
import com.dufther.service.UserService;
import com.dufther.web.rest.util.HeaderUtil;
import com.dufther.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class UserController {
	
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	 
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserSearchRepository userSearchRepository;
    
    
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private AuditEventPublisher auditPublisher;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginModel loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserId(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("authentication *******************"+authentication.getAuthorities());
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value="Authorization" ,defaultValue="Unauthorised") String authorization) {
    	
    	String usertoken=authorization.split(" ")[1];
    	String principal = jwtProvider.getUserNameFromJwtToken(usertoken);
    	System.out.println("principal = "+principal);
//        String principal = SecurityUtils.getCurrentUserLogin().get();
        Map<String, Object> data = new HashMap<>();
		data.put("type", "LOGOUT");
		data.put("message", "");
        AuditEvent event = new AuditEvent(principal, "LOGOUT_START", data);
        auditPublisher.publish(event);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is logged out with identifier " + principal, principal)).build();
    }
    
    @RequestMapping(value = {"/getuser"}, method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@RequestParam(name = "email" ,required =false) String email) {
        ResponseEntity<Map<String, Object>> entity = null;
        Map<String, Object> response = new ManagedMap<>();

        System.out.println("CurrentUserJWT : "+SecurityUtils.getCurrentUserJWT());
        System.out.println("getCurrentUserLogin : "+SecurityUtils.getCurrentUserLogin());
        try {
            List<User> users = null;
            User user = null;
            if (email != null) {
                user = userService.findUserByEmail(email);
                response.put("output", user);
                response.put("msg", "Success");
                entity = new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                users = userService.findAll();

                response.put("output", users);
                response.put("msg", "Success");
                entity = new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("msg", "failed");
            entity = new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
        return entity;
    }
    
    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
   
    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param userId the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{userId:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.debug("REST request to delete User: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is deleted with identifier " + userId, userId)).build();
    }

    
    /**
     * SEARCH /_search/users/:query : search for the User corresponding
     * to the query.
     *
     * @param query the query to search
     * @return the result of the search
     */
    @GetMapping("/_search/users/{query}")
    public List<User> search(@PathVariable String query) {
        return StreamSupport
            .stream(userSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
}