/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dataformat.soap;

import java.io.IOException;

import com.example.customerservice.GetCustomersByName;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Works like SoapRoundTripTest but uses a spring configuration instead of the java dsl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SoapSpringRoundtripTest {
    
    @EndpointInject("mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce("direct:start")
    protected ProducerTemplate producer;

    @Test
    public void testRoundTrip() throws IOException, InterruptedException {
        resultEndpoint.expectedMessageCount(1);
        GetCustomersByName request = new GetCustomersByName();
        request.setName("Mueller");
        producer.sendBody(request);
        resultEndpoint.assertIsSatisfied();
        Exchange exchange = resultEndpoint.getExchanges().get(0);
        GetCustomersByName received = exchange.getIn().getBody(
                GetCustomersByName.class);
        Assert.assertNotNull(received);
        Assert.assertEquals("Mueller", received.getName());
    }
}
