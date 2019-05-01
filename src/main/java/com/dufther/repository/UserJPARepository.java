package com.dufther.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dufther.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJPARepository extends JpaRepository<User,Long> {
    User findUserByEmail(String email);

    @Query(value = "select r.name from user u inner join user_authority ur on(u.user_id=ur.user_id) " +
            " inner join authority r on(ur.authority_id=r.authority_id) inner join authority_permission rp on(r.authority_id=rp.authority_id) " +
            " inner join permission p on(rp.permission_id=p.permission_id) where u.email=? ", nativeQuery = true)
    List<String> getAuthoritiesByEmail(String email);
    
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

//    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);
    @Query(value = "select e.userType from User e where e.userId =:userId")
    Optional<String> getUserTypeById(@Param("userId") String userId);
    
    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByUserId(String userId);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUserId(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Page<User> findAllByUserIdNot(Pageable pageable, String userId);
    
}
