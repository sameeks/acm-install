package com.acadaman.install;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.io.*;
import java.util.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class OpenShiftServiceInstall {

    private static final Logger log = LogManager.getLogger(OpenShiftServiceInstall.class);

    private String user;
    private String password;
    private File responseFile;
    private static final String OPENSHIFT_ENDPOINT = "https://openshift.redhat.com/broker/rest/domains/acms/applications";
    private static final String OPENSHIFT_DOMAIN = "openshift.redhat.com";

    public OpenShiftServiceInstall(String user, String password, File responseFile,  HashMap<String, String> params) {
        this.user = user;
        this.password = password;
        this.responseFile = responseFile;
    }

    public void createAppUsingHttp() throws Exception {
        // authenticate to openshift.
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials(user, password);
        credsProvider.setCredentials(new AuthScope(OPENSHIFT_DOMAIN, AuthScope.ANY_PORT), credentials);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(OPENSHIFT_ENDPOINT);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("name", ""));
        nvps.add(new BasicNameValuePair("catridge", ""));
        nvps.add(new BasicNameValuePair("initial_git_url", ""));
        nvps.add(new BasicNameValuePair("scale", new Boolean(true).toString()));

        postRequest.setEntity(new UrlEncodedFormEntity(nvps));
        
        CloseableHttpResponse response = httpClient.execute(postRequest);

        InputStream input = null;
        FileOutputStream output = null;
        try {
            // write response to file.
            input = response.getEntity().getContent();
            output = new FileOutputStream(responseFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }

        }
        catch(IOException e) {
            log.info("Error while parsing the response: " + e.getMessage());
        }
        finally {
           response.close(); 
        }
    }
}
