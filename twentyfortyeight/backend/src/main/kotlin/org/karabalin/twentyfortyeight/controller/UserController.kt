package org.karabalin.twentyfortyeight.controller

import org.karabalin.twentyfortyeight.controller.dto.UserDto
import org.karabalin.twentyfortyeight.repository.UserRepository
import org.karabalin.twentyfortyeight.repository.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/users")
class UserController(
    @Autowired private val userRepository: UserRepository
) {
    @GetMapping("/users")
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    @PostMapping("/users")
    fun addUser(@RequestBody user: UserDto) {
        val userToSave = User(id = null, username = user.username)
        userRepository.save(userToSave)
    }
}
