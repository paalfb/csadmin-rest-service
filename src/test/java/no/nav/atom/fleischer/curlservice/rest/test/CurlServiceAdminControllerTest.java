package no.nav.atom.fleischer.curlservice.rest.test;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurlServiceAdminControllerTest {

    private static Logger logger = LoggerFactory.getLogger(CurlServiceAdminControllerTest.class);

    @LocalServerPort
    int randomPort;

    @Before
    public void setUp() {

    }

    /*
    HTTP GET:
        curl http://{host:port}/curls/
        curl http://{host:port}/curls/{id}
        curl http://{host:port}/curls/active
        curl http://{host:port}/curls/deactivated


    HTTP PUT:
        curl -X PUT -d 'id={id}&url={url}' http://{host:port}/curls/update/url
        curl -X PUT -d 'id={id}&crontab={crontab}' http://{host:port}/curls/update/crontab
        curl -X PUT -d 'id={id}&active={false|true}' http://{host:port}/curls/update/active

    HTTP DELETE:
        curl -X DELETE http://{host:port}/curls/{id}

    HTTP POST:
        curl -X POST -d "url={url}&crontab={crontab}&active={true|false}" -H "Content-Type: application/x-www-form-urlencoded" http://{host:port}/curls


     */

    @Test
    public void getCurls() throws IOException {
        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet("http://localhost:" + randomPort + "/curls");
        // When
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals(jsonMimeType, mimeType);
    }

    @Test
    public void getCurlsActive() throws IOException {
        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet("http://localhost:" + randomPort + "/curls/active");
        // When
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals(jsonMimeType, mimeType);
    }

    @Test
    public void getCurlsDeactivated() throws IOException {
        logger.info("getCurlsDeactivated()");
        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet("http://localhost:" + randomPort + "/curls/deactivated");
        // When
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals(jsonMimeType, mimeType);
        logger.debug(EntityUtils.toString(response.getEntity(), "UTF-8"));
    }

    @Test
    @Ignore
    public void postCurlURLEncodedFormEntity() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:" + randomPort + "/curls");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("url", "http://www.vgggg.no"));
        params.add(new BasicNameValuePair("crontab", "5 * * * * ?"));
        params.add(new BasicNameValuePair("active", "false"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = client.execute(httpPost);
        assertEquals(201, response.getStatusLine().getStatusCode());
        client.close();

    }

}
