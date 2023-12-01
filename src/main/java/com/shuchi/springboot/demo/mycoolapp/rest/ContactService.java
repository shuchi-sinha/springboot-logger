package com.shuchi.springboot.demo.mycoolapp.rest;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ContactService {
    private static Logger log = LoggerFactory.getLogger(ContactService.class);

    int count = 0;
    private static RestTemplate resTemplate = new RestTemplate();
    private static HttpHeaders headers = new HttpHeaders();
    @Value("{login}")
    private String login;
    @Value("{authorizationURL}")
    private static String authURL;

    @Value("{password}")
    private String secret;

    @Value("{chunkSize}")
    private Integer chunkSize;

    private PaginatedResult readConsumerRecords(String url, HttpEntity<?> entity) {
        count++;
        log.info("read consumer records from API ");

        ResponseEntity<PaginatedResult> response = resTemplate.exchange(url, HttpMethod.GET, entity,
                PaginatedResult.class);
        if (response.getStatusCode().equals(200)) {
            PaginatedResult paginatedResult = response.getBody();
            return paginatedResult;
        }
        return null;
    }

    private void getContactResourceByAccountId(String url, String type) {

        String token = getPostAuthorization();
        headers.set("Authorization", token);
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "*/*");
        HttpEntity<?> entity = new HttpEntity<Object>(headers);
        if (type.equals("contactId") || type.equals("byEmail") || type.equals("byId") || type.equals("byExternalId")) {
            ResponseEntity<ContactRecords> response = resTemplate.exchange(url, HttpMethod.GET, entity,
                    ContactRecords.class);
            if (response.getStatusCode().equals(200)) {
                ContactRecords contactRecords = response.getBody();
                // processContactById(contactRecords);
            } else if (type.equals("all") || type.equals("modified")) {
                int resultCount = 0;
                PaginatedResult result = readConsumerRecords(url, entity);
                if (result != null && result.getTotalSize() != null && !result.getTotalSize().equalsIgnoreCase("0")) {
                    Integer totalSize = Integer.valueOf(result.getTotalSize());
                    List<ContactRecords> contactRecordsList = new ArrayList<>();
                    while (result != null && result.getDone() != null && !result.getDone().equalsIgnoreCase("true")) {
                        log.info("contact records size in result " + result.getRecords().size());
                        contactRecordsList.addAll(contactRecordsList);
                        if (resultCount == chunkSize) {
                            if (type.equals("all")) {
                                // importAllContact(contactRecordsList);
                            } else // processContactbyAllModified(contactRecordsList);

                                contactRecordsList.clear();
                            resultCount = 0;
                        }
                        String nextUrl = null;
                        if (url != null && url.contains("modified") && url.contains("?start")) {
                            nextUrl = url.replace("?start", "/" + result.getNextRecordsUrl() + "?start");
                        } else {
                            nextUrl = url + "/" + result.getNextRecordsUrl();
                        }
                        HttpHeaders headers1 = new HttpHeaders();
                        String token1 = getPostAuthorization();
                        headers1.set("Authorization", token1);
                        headers1.set("Accept", token1);
                        headers1.set("Accept", "application/json");
                        headers1.set("Content-Type", "*/*");
                        HttpEntity<?> entity1 = new HttpEntity<Object>(headers1);
                        result = readConsumerRecords(nextUrl, entity1);
                        resultCount++;
                    }
                    if (result != null && result.getRecords() != null) {
                        contactRecordsList.addAll(contactRecordsList);
                    }
                    if ("all".equals("type")) {
                        // importAllContact(contactRecordsList);
                    } else {// processContactbyAllModified(contactRecordsList);
                    }
                    contactRecordsList.clear();
                }
            }
        }

    }

    private String getPostAuthorization() {
        String token = " ";

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("login", login);
        map.add("password", secret);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<PostAuthorization> response = resTemplate.exchange(authURL, HttpMethod.POST, entity,
                PostAuthorization.class);
        if (response.getStatusCode().equals(200)) {
            PostAuthorization pa = response.getBody();
            token = pa.getToken();
        }
        return token;
    }

    public static String escapecommas(String s){
        if (s==null) return "";
        if (!s.contains(",")||s.contains(("\""))) return s;
        return "\""+s+"\"";
    }

    public static void saveContactRecords(List<ContactRecordsEntity> contactList){
        String query="""
                Insert into table_name (Columns..) values (?,?,?,?)
                """;
               
                    DriverManagerDataSource ds=new DriverManagerDataSource();
                    ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    ds.setUrl(authURL);
                    
                JdbcTemplate jdbcTemplate=new JdbcTemplate( ds);
                jdbcTemplate.batchUpdate(query,contactList, 500,new ParameterizedPreparedStatementSetter<ContactRecordsEntity>() {
                    public void setValues(PreparedStatement ps, ContactRecordsEntity contact) throws SQLException{
                        ps.setString(1, contact.getId());
                        ps.setString(2,contact.getFirstName());
                        ps.setString(3, contact.getLastName());
                        ps.setString(4, contact.getInitial());
                    }       
                });
    }
    @Entity
    @Table (name=" ")
    class ContactRecordsEntity{
        @Id
        @Column(name="Id")
    private String id;
    @Column(name="FirstName")
    private String firstName;
    @Column (name="Initial")
    private String initial;
    @Column(name="LastName")
    private String lastName;
    // @OneToMany(cascade = CascadeType.ALL)
    // @JoinColumn(name="Contact_ID")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getInitial() {
        return initial;
    }
    public void setInitial(String initial) {
        this.initial = initial;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    }
}
