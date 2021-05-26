package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository: JpaRepository<Image, Long> {
}