package top.zanghongmin.blockchain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import top.zanghongmin.blockchain.core.ReturnT;
import top.zanghongmin.blockchain.dto.Block;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

public class Test {


    public static void main(String[] args) throws Exception {

        ObjectMapper om=new ObjectMapper();
        //Include.NON_NULL 属性为NULL 不序列化
        om.setSerializationInclusion(JsonInclude.Include.ALWAYS  );
        Block block = new Block();
        System.out.println(om.writeValueAsString(block));


//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setReadTimeout(5000);//单位为ms
//        factory.setConnectTimeout(5000);//单位为ms
//
//        RestTemplate restTemplate =  new RestTemplate(factory);

        RestTemplate restTemplate =  Test.ksRestTemplate();

        ResponseEntity<ReturnT> responseEntity = restTemplate.getForEntity("http://127.0.0.1:8080/api/chain?signature=123456&source=FrontEnd&transeq=123456789",ReturnT.class);

        List<Block> Blocks1 =   (List<Block> )responseEntity.getBody().getData();

        ObjectMapper ObjectMapper = new ObjectMapper();
        List<Block> Blocks =  ObjectMapper.convertValue(responseEntity.getBody().getData(), List.class);
        System.out.println(responseEntity.getBody().getData());



    }


        public static RestTemplate ksRestTemplate() throws KeyStoreException, NoSuchAlgorithmException {
            final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            //设置过期时间
            factory.setConnectionRequestTimeout(5000);
            factory.setReadTimeout(5000);
            factory.setReadTimeout(5000);
            final SSLContextBuilder builder = new SSLContextBuilder();
            try {
                //全部信任 不做身份鉴定
                builder.loadTrustMaterial(null, (X509Certificate[] x509Certificate, String s) -> true);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw e;
            } catch (KeyStoreException e) {
                throw e;
            }
            SSLConnectionSocketFactory socketFactory = null;
            try {
                //客户端支持SSLv2Hello，SSLv3,TLSv1，TLSv1
                socketFactory = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

            //为自定义连接器注册http与https
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", new PlainConnectionSocketFactory()).register("https", socketFactory).build();
            PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
            phccm.setMaxTotal(500);
            final CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).setConnectionManager(phccm).setConnectionManagerShared(true).build();
            factory.setHttpClient(httpClient);
            final RestTemplate restTemplate = new RestTemplate(factory);
            return restTemplate;
        }






}
