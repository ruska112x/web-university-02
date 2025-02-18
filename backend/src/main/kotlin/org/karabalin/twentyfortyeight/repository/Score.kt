package org.karabalin.twentyfortyeight.repository

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

@Entity
@Table(name = "scores")
data class Score(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "score_id") var id: Int? = null,

    @Column(name = "value", nullable = false) var value: Int,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(
        name = "user_id",
        nullable = false
    ) @JsonIgnore var user: User? = null
) {
    constructor() : this(id = null, value = 0, user = null)
}

data class LeaderboardEntry(
    val username: String, val maxScore: Int
)

interface ScoreRepository : JpaRepository<Score, Int> {
    @Query(
        """
        SELECT u.username as username, MAX(s.value) as maxScore
        FROM users u
        JOIN scores s ON s.user_id = u.user_id
        GROUP BY u.user_id
        ORDER BY maxScore DESC
    """, nativeQuery = true
    )
    fun selectLeaderboard(): List<LeaderboardEntry>
}
