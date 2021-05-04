package k.co.willynganga.codematatasessions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CodematataSessionsApplicationTests {

	private val underTest = Calculator()

	@Test
	fun `it should add two integer numbers`() {
		// given
		val a = 20;
		val b = 30;

		// when
		val result = underTest.add(a, b)

		//then
		assertThat(result).isEqualTo(50)
	}

	class Calculator {
		fun add(a: Int, b: Int): Int {
			return a + b
		}
	}

}
