package jp.co.ysd.sample_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jp.co.ysd.spring_form_keeper.EnableFormKeeper;

@SpringBootApplication
@EnableFormKeeper
public class SampleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleAppApplication.class, args);
	}

}
