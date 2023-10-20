package com.shuchi.springboot.demo.mycoolapp;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MycoolappApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(MycoolappApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MycoolappApplication.class, args);
		logger.info("Application started");
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application starting " + SimpleDateFormat.getDateInstance().format(new Date()));
		// new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
		System.setProperty("https.protocols", "TLSv1, TLSv1.1, TLSv1.2");

	}

	private static boolean validateArguments(String[] a, String args) {
		if (a != null && a.length > 0) {
			Arrays.stream(a).anyMatch(args::equalsIgnoreCase);
		}
		return false;
	}

}
