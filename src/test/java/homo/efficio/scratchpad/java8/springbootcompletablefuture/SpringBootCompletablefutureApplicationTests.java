package homo.efficio.scratchpad.java8.springbootcompletablefuture;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SpringBootCompletablefutureApplicationTests {

	@Test
	public void contextLoads() {
		String test = "a";
		String[] splitted = test.split("\\|\\|\\|");
		System.out.println("length of 'a|||b' " + splitted.length);

		test = "\\_4";
		String tmp = test.replaceAll("\\\\", "\\\\\\\\");
		System.out.println(test + " " + tmp);
	}

	@Test
	public void split_테스트() throws Exception {
		final String splitter = ":::";

		assertThat(splitter.split(splitter).length).isEqualTo(0);

		assertThat("a:::".split(splitter).length).isEqualTo(1);
		assertThat("a:::".split(splitter)[0]).isEqualTo("a");

		assertThat(":::a".split(splitter).length).isEqualTo(2);
		assertThat(":::a".split(splitter)[0]).isEqualTo("");
		assertThat(":::a".split(splitter)[1]).isEqualTo("a");

		assertThat("abc".split(splitter).length).isEqualTo(1);
		assertThat("abc".split(splitter)[0]).isEqualTo("abc");

		assertThat("".split(splitter).length).isEqualTo(1);
		assertThat("".split(splitter)[0]).isEqualTo("");
	}

}
