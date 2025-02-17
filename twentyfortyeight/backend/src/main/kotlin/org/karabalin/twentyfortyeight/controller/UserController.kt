package org.karabalin.twentyfortyeight.controller

import org.karabalin.twentyfortyeight.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import kotlin.jvm.optionals.getOrElse

@RestController("/users")
class UserController(
    @Autowired private val userRepository: UserRepository, @Autowired private val scoreRepository: ScoreRepository
) {
    @PostMapping("/users")
    fun addUser(@RequestBody user: UserDto) {
        val userToSave = User(id = null, username = user.username)
        userToSave.addScore(Score(id = null, value = user.score))
        userRepository.save(userToSave)
    }

    @Transactional
    @PostMapping("/users/{id}")
    fun addScoreForUser(@PathVariable id: Int, @RequestBody score: ScoreDto): ResponseEntity<Void> {
        val userToAddScore = userRepository.findById(id)
        val user = userToAddScore.getOrElse {
            return ResponseEntity.notFound().build()
        }
        scoreRepository.save(Score(id = null, value = score.value, user = user))
        val location = UriComponentsBuilder.fromPath("/users/").path(id.toString()).path("/scores").build().toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping("/users/{id}/scores")
    fun getScoresByUser(@PathVariable id: Int): ResponseEntity<List<Score>> {
        val user = userRepository.findById(id).getOrElse {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(user.scores)
    }

    @GetMapping("/leaderboard")
    fun getLeaderboard(): ResponseEntity<List<LeaderboardEntry>> {
        val leaderboard = scoreRepository.selectLeaderboard()
        return ResponseEntity.ok(leaderboard)
    }
}
