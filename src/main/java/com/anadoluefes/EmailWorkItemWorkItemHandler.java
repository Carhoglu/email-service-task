/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anadoluefes;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.jbpm.process.workitem.core.AbstractLogOrThrowWorkItemHandler;
import org.jbpm.process.workitem.core.util.RequiredParameterValidator;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.jbpm.process.workitem.core.util.Wid;
import org.jbpm.process.workitem.core.util.WidParameter;
import org.jbpm.process.workitem.core.util.WidResult;
import org.jbpm.process.workitem.core.util.service.WidAction;
import org.jbpm.process.workitem.core.util.service.WidAuth;
import org.jbpm.process.workitem.core.util.service.WidService;
import org.jbpm.process.workitem.core.util.WidMavenDepends;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

@Wid(widfile = "EmailWorkItemDefinitions.wid", name = "EmailWorkItemDefinitions",
        displayName = "EmailWorkItemDefinitions",
        defaultHandler = "mvel: new com.anadoluefes.EmailWorkItemWorkItemHandler()",
        documentation = "email-service-task/index.html",
        category = "email-service-task",
        icon = "EmailWorkItemDefinitions.png",
        parameters = {
                @WidParameter(name = "to", required = true),
                @WidParameter(name = "cc"),
                @WidParameter(name = "subject", required = true),
                @WidParameter(name = "body", required = true)
        },
        results = {
                @WidResult(name = "SampleResult")
        },
        mavenDepends = {
                @WidMavenDepends(group = "com.anadoluefes", artifact = "email-service-task", version = "7.33.0.Final-redhat-00003")
        },
        serviceInfo = @WidService(category = "email-service-task", description = "${description}",
                keywords = "",
                action = @WidAction(title = "Sample Title"),
                authinfo = @WidAuth(required = true, params = {"to", "cc", "subject", "body"},
                        paramsdescription = {"to", "cc", "subject", "body"},
                        referencesite = "referenceSiteURL")
        )
)
public class EmailWorkItemWorkItemHandler extends AbstractLogOrThrowWorkItemHandler  {

    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String CONTENT_TYPE = "Content-Type";

    public void executeWorkItem(WorkItem workItem,
                                WorkItemManager manager) {
        try {
            RequiredParameterValidator.validate(this.getClass(),
                    workItem);

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:7070/demo/exception");
            httpPost.setHeader(CONTENT_TYPE, JSON_CONTENT_TYPE);

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("naber","ibo");
            JSONObject JSON_STRING = new JSONObject(hashMap);
            StringEntity entity = new StringEntity(JSON_STRING.toString());
            httpPost.setEntity(entity);

            CloseableHttpResponse response = client.execute(httpPost);
            System.out.println("---- >>> status response  --- " + response.getStatusLine());

            // assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
            client.close();

            // sample parameters
            String to = (String) workItem.getParameter("to");
            String cc = (String) workItem.getParameter("cc");
            String subject = (String) workItem.getParameter("subject");
            String body = (String) workItem.getParameter("body");

            // complete workitem impl...

            // return results
            String sampleResult = "sample result";
            Map<String, Object> results = new HashMap<String, Object>();
            results.put("SampleResult", sampleResult);


            System.out.println("---- >>> to --- " + to);
            System.out.println("---- >>> cc --- " + cc);
            System.out.println("---- >>> subject --- " + subject);
            System.out.println("---- >>> body --- " + body);

            manager.completeWorkItem(workItem.getId(), results);
        } catch (Throwable cause) {
            handleException(cause);
        }
    }

    @Override
    public void abortWorkItem(WorkItem workItem,
                              WorkItemManager manager) {
        // stub
    }
}


