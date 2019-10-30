package com.ugiant.job.test;

import com.ugiant.job.JobServerApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author aoki
 * @date ${date}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(JobServerApplication.class)
public class Tester {
}
