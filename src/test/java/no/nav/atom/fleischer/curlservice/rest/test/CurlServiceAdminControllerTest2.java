package no.nav.atom.fleischer.curlservice.rest.test;


import no.nav.atom.fleischer.curlservice.repository.mongo.CURLDocument;
import no.nav.atom.fleischer.curlservice.repository.mongo.Util;
import no.nav.atom.fleischer.curlservice.repository.mongo.dao.CURLDao;
import no.nav.atom.fleischer.curlservice.rest.CurlServiceAdminController;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers.*;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static no.nav.atom.fleischer.curlservice.repository.mongo.Util.serialize;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
//@RunWith(MockitoJUnitRunner.Silent.class)
public class CurlServiceAdminControllerTest2 {

    @Mock
    private CURLDao curlDaoMock;

    @InjectMocks
    private CurlServiceAdminController curlServiceAdminController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll_returnsAll() throws IOException {
        List<CURLDocument> curlDocuments = new ArrayList<>();
        CURLDocument curlDocument1 = new CURLDocument();
        curlDocument1.setActive(true);
        curlDocument1.setId(new ObjectId());
        curlDocument1.setCrontab("1 * * * * ?");
        curlDocument1.setUrl("www.google.no");
        curlDocuments.add(curlDocument1);
        CURLDocument curlDocument2 = new CURLDocument();
        curlDocument2.setActive(false);
        curlDocument2.setId(new ObjectId());
        curlDocument2.setCrontab("10 * * * * ?");
        curlDocument2.setUrl("http://www.vg.no");
        curlDocuments.add(curlDocument2);
        when(curlDaoMock.findAll()).thenReturn(curlDocuments);
        String allRes = curlServiceAdminController.findAll();
        assertThat(allRes, equalTo(serialize(curlDocuments)));
    }

    @Test
    @Ignore
    public void getCurls() throws IOException {
        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet("http://localhost:8080/curls");
        // When
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals(jsonMimeType, mimeType);
    }
}
