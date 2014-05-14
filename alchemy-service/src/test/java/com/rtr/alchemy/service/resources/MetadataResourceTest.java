package com.rtr.alchemy.service.resources;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.google.common.collect.Maps;
import org.junit.Test;

import javax.ws.rs.core.Response.Status;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MetadataResourceTest extends ResourceTest {
    private static final String METADATA_IDENTITY_TYPES_ENDPOINT = "/metadata/identityTypes";
    private static final String METADATA_IDENTITY_TYPE_ENDPOINT = "/metadata/identityTypes/{identityType}";

    @Test
    public void testGetIdentityTypes() {
        final Map<String, Class> expected = Maps.newHashMap();
        expected.put("user", UserDto.class);
        expected.put("device", DeviceDto.class);

        final Map<String, Class> actual =
            get(METADATA_IDENTITY_TYPES_ENDPOINT)
                .assertStatus(Status.OK)
                .result(map(String.class, Class.class));

        assertEquals(expected, actual);
    }

    @Test
    public void testGetIdentitySchema() {
        get(METADATA_IDENTITY_TYPE_ENDPOINT, "foobar")
            .assertStatus(Status.NOT_FOUND);

        final JsonSchema schema =
            get(METADATA_IDENTITY_TYPE_ENDPOINT, "user")
                .assertStatus(Status.OK)
                .result(JsonSchema.class);

        assertNotNull(schema);
        assertTrue(
            schema
                .asObjectSchema()
                .getProperties()
                .get("name")
                .isStringSchema()
        );
    }
}