package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.ImageUrl
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageUrlRepository: JpaRepository<ImageUrl, Long> {
}