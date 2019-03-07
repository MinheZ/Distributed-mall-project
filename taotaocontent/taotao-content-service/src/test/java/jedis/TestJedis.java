package jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;


public class TestJedis {

//    @Test
//    public void testJedis(){
//        // 创建一个Jedis对象，需要定制服务的ip和端口号
//        Jedis jedis = new Jedis("192.168.1.200",6379);
//        // 直接操作数据库
//        jedis.set("jedis-key","1234");
//        System.out.println(jedis.get("jedis-key"));
//        jedis.close();
//    }
//
//    @Test
//    public void testJedisPool(){
//        // 创建一个数据库连接池对象，需要指定服务的ip和端口号
//        JedisPool jedisPool = new JedisPool("192.168.1.200",6385);
//        // 从连接池中获取连接
//        Jedis jedis = jedisPool.getResource();
//        // 使用Jedis操作数据库
//        jedis.set("jedis-key1","12345");
//        System.out.println(jedis.get("jedis-key1"));
//        // 关闭Jedis连接
//        jedis.close();
//        jedisPool.close();
//    }
    /*测试redis集群连接*/
    @Test
    public void testJedisCluster(){
        // 创建一个JedisCluster对象，构造参数set类型，集合中每个元素是HostAndPort类型
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        // 向集合中添加节点
        hostAndPorts.add(new HostAndPort("192.168.1.200",6379));
        hostAndPorts.add(new HostAndPort("192.168.1.200",6380));
        hostAndPorts.add(new HostAndPort("192.168.1.200",6381));
        hostAndPorts.add(new HostAndPort("192.168.1.200",6382));
        hostAndPorts.add(new HostAndPort("192.168.1.200",6383));
        hostAndPorts.add(new HostAndPort("192.168.1.200",6384));

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts);
        // 直接使用JedisCluster操作redis，自带连接池。jedisCluster对象可以是单例的
        jedisCluster.set("key4","jedisClusterTest");
        System.out.println(jedisCluster.get("key4"));
        jedisCluster.close();

    }
}
