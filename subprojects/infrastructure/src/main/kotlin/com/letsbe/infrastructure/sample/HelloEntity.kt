package com.letsbe.infrastructure.sample

import com.letsbe.domain.sample.aggregate.HelloId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "hello")
@Entity
class HelloEntity(
    id: HelloId? = null,
    nickName: String,
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: HelloId? = id

	@Column(
		unique = true,
		nullable = false,
		name = "nick_name"
	)
	var nickName: String = nickName
		protected set
}
