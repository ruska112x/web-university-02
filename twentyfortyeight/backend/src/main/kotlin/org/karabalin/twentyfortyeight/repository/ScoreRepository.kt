package org.karabalin.twentyfortyeight.repository

import org.karabalin.twentyfortyeight.repository.entity.LeaderboardEntry
import org.karabalin.twentyfortyeight.repository.entity.Score
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ScoreRepository : JpaRepository<Score, Int> {
    @Query("""
        SELECT u.username as username, MAX(s.value) as maxScore
        FROM users u
        JOIN scores s ON s.user_id = u.user_id
        GROUP BY u.user_id
        ORDER BY maxScore DESC
    """, nativeQuery = true)
    fun selectLeaderboard(): List<LeaderboardEntry>
}
