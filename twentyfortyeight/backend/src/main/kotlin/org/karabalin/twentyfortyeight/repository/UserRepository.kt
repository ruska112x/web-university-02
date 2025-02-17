package org.karabalin.twentyfortyeight.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.karabalin.twentyfortyeight.repository.entity.User

interface UserRepository : JpaRepository<User, Int>