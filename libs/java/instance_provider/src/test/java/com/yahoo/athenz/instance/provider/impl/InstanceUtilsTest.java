/*
 * Copyright The Athenz Authors
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
package com.yahoo.athenz.instance.provider.impl;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class InstanceUtilsTest {

    @Test
    public void testClassConstructor() {
        InstanceUtils utils = new InstanceUtils();
        assertNull(utils.getInstanceProperty(null,  "cloudAccount"));
    }

    @Test
    public void testGetInstanceProperty() {

        assertNull(InstanceUtils.getInstanceProperty(null,  "cloudAccount"));

        HashMap<String, String> attributes = new HashMap<>();
        assertNull(InstanceUtils.getInstanceProperty(attributes,  "cloudAccount"));

        attributes.put("testAccount", "1235");
        assertNull(InstanceUtils.getInstanceProperty(attributes,  "cloudAccount"));

        attributes.put("cloudAccount", "1235");
        assertEquals(InstanceUtils.getInstanceProperty(attributes,  "cloudAccount"), "1235");
    }

    @Test
    public void testValidateCertRequestHostnamesNullSuffix() {
        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(null,  null,  null,  null,
                false, new StringBuilder(256)));
    }

    @Test
    public void testValidateCertRequestHostnamesEmptySuffix() {
        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(null,  null,  null,
                Collections.emptySet(), false, new StringBuilder(256)));
    }

    @Test
    public void testValidateCertRequestHostnamesInvalidCount() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "service.athenz.athenz.cloud,service2.athenz.athenz.cloud,service3.athenz.athenz.cloud");

        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, new StringBuilder(256)));
    }

    @Test
    public void testValidateCertRequestHostnamesInvalidInstanceId() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "api.athenz.athenz.cloud,i-1234.instanceid.athenz.athenz2.cloud");

        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, new StringBuilder(256)));
    }

    @Test
    public void testValidateCertRequestHostnamesInvalidHost() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "storage.athenz.athenz.cloud,i-1234.instanceid.athenz.athenz.cloud");

        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, new StringBuilder(256)));
    }

    @Test
    public void testValidateCertRequestHostnamesMissingInstanceId() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "api.athenz.athenz.cloud,api.athenz.athenz.cloud");

        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, new StringBuilder(256)));
    }

    @Test
    public void testValidateCertRequestHostnamesMissingHost() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "i-1234.instanceid.athenz.athenz.cloud,i-1234.instanceid.athenz.athenz.cloud");

        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, new StringBuilder(256)));
    }

    @Test
    public void testValidateCertRequestHostnames() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "api.athenz.athenz.cloud,i-1234.instanceid.athenz.athenz.cloud");
        StringBuilder id = new StringBuilder(256);
        assertTrue(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, id));
        assertEquals(id.toString(), "i-1234");
    }

    @Test
    public void testValidateCertRequestHostnamesWithInstanceIdURI() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "api.athenz.athenz.cloud");
        attributes.put("sanURI", "spiffe://athenz/sa/cloud,athenz://instanceid/zts/i-1234");
        StringBuilder id = new StringBuilder(256);
        assertTrue(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, id));
        assertEquals(id.toString(), "i-1234");
    }

    @Test
    public void testValidateCertRequestHostnamesWithInvalidInstanceIdURI() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "api.athenz.athenz.cloud");
        attributes.put("sanURI", "spiffe://athenz/sa/cloud,athenz://instanceid/zts");
        StringBuilder id = new StringBuilder(256);
        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, id));
    }

    @Test
    public void testValidateCertRequestHostnamesWithEmptyInstanceIdURI() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "api.athenz.athenz.cloud");
        attributes.put("sanURI", "spiffe://athenz/sa/cloud,athenz://instanceid/zts/");
        StringBuilder id = new StringBuilder(256);
        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, id));
    }

    @Test
    public void testValidateCertRequestHostnamesNullHostnames() {
        HashMap<String, String> attributes = new HashMap<>();
        StringBuilder id = new StringBuilder(256);
        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, id));
    }

    @Test
    public void testValidateCertRequestHostnamesEmptyHostnames() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "");
        StringBuilder id = new StringBuilder(256);
        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), false, id));
    }

    @Test
    public void testValidateCertRequestHostnamesInvalidHostname() {

        // first without hostname in the attributes, the request is valid

        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "api.athenz.athenz.cloud,i-1234.instanceid.athenz.athenz.cloud");
        StringBuilder id = new StringBuilder(256);
        assertTrue(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), true, id));
        assertEquals(id.toString(), "i-1234");

        // now let's set the hostname to a valid value and verify

        id.setLength(0);
        attributes.put("sanDNS", "api.athenz.athenz.cloud,i-1234.api.athenz.athenz.cloud,i-1234.instanceid.athenz.athenz.cloud");
        attributes.put("hostname", "i-1234.api.athenz.athenz.cloud");
        assertTrue(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), true, id));
        assertEquals(id.toString(), "i-1234");

        // now let's set the hostname to a non-matching value

        id.setLength(0);
        attributes.put("hostname", "i-1235.api2.athenz.athenz.cloud");
        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), true, id));
    }

    @Test
    public void testValidateCertRequestHostnamesOnlyInstanceId() {

        // with only instance id dns name, we should get failure

        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("sanDNS", "i-1234.instanceid.athenz.athenz.cloud");
        StringBuilder id = new StringBuilder(256);
        assertFalse(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), true, id));

        // now let's set the hostname to a valid value and verify

        id.setLength(0);
        attributes.put("sanDNS", "i-1234.api.athenz.athenz.cloud,i-1234.instanceid.athenz.athenz.cloud");
        attributes.put("hostname", "i-1234.api.athenz.athenz.cloud");
        assertTrue(InstanceUtils.validateCertRequestSanDnsNames(attributes, "athenz", "api",
                Collections.singleton("athenz.cloud"), true, id));
        assertEquals(id.toString(), "i-1234");
    }

    @Test
    public void testDnsSuffixMatchIndex() {
        List<String> dnsSuffixes = Arrays.asList(".athenz.cloud", ".athenz.us");
        assertEquals(InstanceUtils.dnsSuffixMatchIndex("abc.athenz.cloud", dnsSuffixes), 3);
        assertEquals(InstanceUtils.dnsSuffixMatchIndex("test.athenz.us", dnsSuffixes), 4);
        assertEquals(InstanceUtils.dnsSuffixMatchIndex("test.athenza.cloud", dnsSuffixes), -1);
    }

    @Test
    public void testValidateSanDnsName() {
        List<String> dnsSuffixes = Arrays.asList(".athenz.cloud", ".athenz.us");

        assertFalse(InstanceUtils.validateSanDnsName("test.athenza.cloud", "api", dnsSuffixes));
        assertFalse(InstanceUtils.validateSanDnsName("test.api2.athenz.cloud", "api", dnsSuffixes));
        assertFalse(InstanceUtils.validateSanDnsName("test.api2.athenz.us", "api", dnsSuffixes));
        assertFalse(InstanceUtils.validateSanDnsName("api2.athenz.us", "api", dnsSuffixes));
        assertFalse(InstanceUtils.validateSanDnsName("api2.test.athenz.cloud", "api", dnsSuffixes));

        assertTrue(InstanceUtils.validateSanDnsName("api.athenz.cloud", "api", dnsSuffixes));
        assertTrue(InstanceUtils.validateSanDnsName("test.api.athenz.cloud", "api", dnsSuffixes));
        assertTrue(InstanceUtils.validateSanDnsName("test.api.test2.athenz.cloud", "api", dnsSuffixes));
        assertTrue(InstanceUtils.validateSanDnsName("api.test3.test2.athenz.cloud", "api", dnsSuffixes));
        assertTrue(InstanceUtils.validateSanDnsName("test.api.test3.test4.athenz.cloud", "api", dnsSuffixes));

        assertTrue(InstanceUtils.validateSanDnsName("api.athenz.us", "api", dnsSuffixes));
        assertTrue(InstanceUtils.validateSanDnsName("test.api.athenz.us", "api", dnsSuffixes));
        assertTrue(InstanceUtils.validateSanDnsName("test.api.test2.athenz.us", "api", dnsSuffixes));
        assertTrue(InstanceUtils.validateSanDnsName("api.test3.test2.athenz.us", "api", dnsSuffixes));
        assertTrue(InstanceUtils.validateSanDnsName("test.api.test3.test4.athenz.us", "api", dnsSuffixes));
    }
}
