package org.karabalin.twentyfortyeight.repository.entity

import jakarta.persistence.*

@Entity
@Table(name = "scores")
data class Score(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    var id: Int? = null,

    @Column(name = "value", nullable = false)
    var value: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null
) {
    constructor() : this(id = null, value = 0, user = null)
}
