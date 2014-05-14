package com.rtr.alchemy.service.exceptions;

import com.google.common.collect.Maps;
import com.sun.jersey.api.container.MappableContainerException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Map;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Map<Class<?>, Response.Status> EXCEPTION_MAPPINGS;

    static {
        EXCEPTION_MAPPINGS = Maps.newLinkedHashMap();

        // 400 - Bad Request
        register(Response.Status.BAD_REQUEST,
            IllegalArgumentException.class,
            IllegalStateException.class,
            NullPointerException.class
        );
    }

    private static void register(Response.Status status, Class<?> ... types) {
        for (Class<?> type : types) {
            EXCEPTION_MAPPINGS.put(type, status);
        }
    }

    @Override
    public Response toResponse(RuntimeException e) {
        if (e instanceof WebApplicationException) {
            return ((WebApplicationException) e).getResponse();
        }

        final Response.Status status = EXCEPTION_MAPPINGS.get(e.getClass());

        if (status == null) {
            throw new MappableContainerException(e);
        }

        return Response
                .status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(e.getMessage())
                .build();
    }
}