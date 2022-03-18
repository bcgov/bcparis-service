package ca.bc.gov.iamp.bcparis.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailRepositoryTest {

    @InjectMocks
    private EmailRepository repo = new EmailRepository();

    @Mock
    private RestTemplate rest;

    @Before
    public void initMocks() throws NoSuchFieldException, SecurityException{
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(repo,"path", "mock_path");
        ReflectionTestUtils.setField(repo,"url", "mock_url");
        ReflectionTestUtils.setField(repo, "username", "mock_username");
        ReflectionTestUtils.setField(repo,"password", "mock_password");
    }

    @Test
    public void request_details_success() {
        when(
                rest.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        ResponseEntity<String> response = repo.sendEmail("subject", "to", "body");

        Assert.assertEquals(response.getBody(), "Success");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
