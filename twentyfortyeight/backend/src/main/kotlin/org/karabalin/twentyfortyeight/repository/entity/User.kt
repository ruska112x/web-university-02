package org.karabalin.twentyfortyeight.repository.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Int? = null,

    @Column(name = "username", nullable = false, length = 255)
    var username: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var scores: MutableList<Score> = mutableListOf()
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
