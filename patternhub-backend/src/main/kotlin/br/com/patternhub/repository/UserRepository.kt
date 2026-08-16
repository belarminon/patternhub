package br.com.patternhub.repository

import br.com.patternhub.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>

@Modifying
@Query("UPDATE User u SET u.status = 'INACTIVE' WHERE u.id = :id")
fun inactivateUserById(@Param("id") id: Long): Int