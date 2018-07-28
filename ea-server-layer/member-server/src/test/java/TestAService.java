import com.bugjc.ea.member.MemberServerApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: qingyang
 * @Date: 2018/7/25 23:00
 * @Description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MemberServerApplication.class)
public class TestAService {

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testRestTemplate(){
        String body = restTemplate.getForObject("http://API-V2/service-b-message", String.class);
        log.info(body);
    }

}
