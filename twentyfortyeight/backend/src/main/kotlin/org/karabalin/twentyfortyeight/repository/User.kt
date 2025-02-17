package org.karabalin.twentyfortyeight.repository

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "users")
data class User(
    @Id @Column(name = "user_id") @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null,

    @Column(name = "username", unique = true, nullable = false, length = 255) var username: String,

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    ) var scores: MutableList<Score> = mutableListOf()
) {
    constructor() : this(id = null, username = "", scores = mutableListOf())

    fun addScore(score: Score) {
        scores.add(score)
        score.user = this
    }

    fun removeScore(score: Score) {
        scores.remove(score)
        score.user = null
    }
}

interface UserRepository : JpaRepository<User, Int>
