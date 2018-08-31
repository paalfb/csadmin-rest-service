package no.nav.atom.fleischer.curlservice.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import no.nav.atom.fleischer.curlservice.repository.mongo.CURLDocument;
import no.nav.atom.fleischer.curlservice.repository.mongo.dao.CURLDao;
import no.nav.atom.fleischer.curlservice.repository.mongo.dao.CURLDaoImpl;
import org.bson.types.ObjectId;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static no.nav.atom.fleischer.curlservice.repository.mongo.Util.serialize;


@RestController
@RequestMapping("/curls")
public class CurlServiceAdminController {

    @Value("${curlservice.hostname}")
    String DB_SERVER;
    @Value("${curlservice.db.name}")
    String DB_NAME;
    @Value("${curlservice.curls.collection}")
    String CURLS_COLLECTION;
    @Value("${curlservice.db.port}")
    int PORT;

    private static CURLDao curlDao = null;

    @PostConstruct
    public void init() {
        curlDao = new CURLDaoImpl(DB_SERVER, PORT, DB_NAME, CURLS_COLLECTION);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String create(CURLDocument curlDocument) {
        curlDao.create(curlDocument);
        return "redirect:/curls";
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String findAll() throws IOException {
        return serialize(curlDao.findAll());
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String findById(@PathVariable("id") String id) {
        try {
            return serialize(curlDao.findById(new ObjectId(id)));
        } catch (JsonProcessingException e) {
            return "redirect:error";
        }
    }

    @GetMapping(path = "active", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String findActive() throws IOException {
        return serialize(curlDao.findActive());
    }

    @GetMapping(path = "deactivated", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String findDeactivated() throws IOException {
        return serialize(curlDao.findDeactivated());
    }

    @PutMapping(path = "update/url", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String updateURL(String id, String url) {
        if (curlDao.updateUrl(new ObjectId(id), url).wasAcknowledged()) return "redirect:/curls";
        else return "redirect:/error";

    }

    @PutMapping(path = "update/crontab", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String updateCrontab(String id, String crontab) {
        if (curlDao.updateCrontab(new ObjectId(id), crontab).wasAcknowledged()) return "redirect:/curls";
        else return "redirect:/error";

    }

    @PutMapping(path = "update/active", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String setActive(String id, String active) {
        if (curlDao.setActive(new ObjectId(id), Boolean.getBoolean(active)).wasAcknowledged()) return "redirect:/curls";
        else return "redirect:/error";
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable("id") String id) {
        try {
            curlDao.delete(new ObjectId(id));
        } catch (Exception e) {
            return "redirect:/error";
        }
        return "redirect:/curls";
    }
}
