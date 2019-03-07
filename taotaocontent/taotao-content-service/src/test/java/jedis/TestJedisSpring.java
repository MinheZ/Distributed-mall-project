package jedis;

import com.taotao.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {
    @Test
    public void testJedisClientPoll(){
        // 初始化Spring容器
        ApplicationContext applicationContext = new
                ClassPathXmlApplicationContext("classpath:/spring/ApplicationContext-redis.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        // 使用JedisClient对象操作Jedis
        jedisClient.set("jedisClient","myTest");
        System.out.println(jedisClient.get("jedisClient"));
    }
}
