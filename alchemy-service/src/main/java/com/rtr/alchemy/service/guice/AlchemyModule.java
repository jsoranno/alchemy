package com.rtr.alchemy.service.guice;

import com.google.inject.AbstractModule;
import com.rtr.alchemy.mapping.Mapper;
import com.rtr.alchemy.mapping.Mappers;
import com.rtr.alchemy.models.Experiments;
import com.rtr.alchemy.service.config.AlchemyServiceConfiguration;
import com.rtr.alchemy.service.mapping.CoreMappings;
import com.rtr.alchemy.service.metadata.IdentitiesMetadata;
import com.rtr.alchemy.service.metadata.IdentityMetadata;
import io.dropwizard.setup.Environment;

/**
 * Guice module for the service
 */
public class AlchemyModule extends AbstractModule {
    private final AlchemyServiceConfiguration configuration;
    private final Environment environment;
    private final IdentitiesMetadata metadata;

    public AlchemyModule(AlchemyServiceConfiguration configuration,
                         Environment environment,
                         IdentitiesMetadata metadata) {
        this.configuration = configuration;
        this.environment = environment;
        this.metadata = metadata;
    }

    @Override
    protected void configure() {
        try {
            configureImpl();
        } catch (final Exception e) {
            addError("failed to configure mapper", e);
        }
    }

    private void configureImpl() throws Exception {
        bind(IdentitiesMetadata.class).toInstance(metadata);
        bind(Environment.class).toInstance(environment);

        final Mappers mappers = buildMappers();
        bind(Mappers.class).toInstance(mappers);

        final Experiments experiments = new Experiments(configuration.getProvider().createProvider());
        bind(Experiments.class).toInstance(experiments);
    }

    private Mappers buildMappers() throws InstantiationException, IllegalAccessException {
        final Mappers mappers = new Mappers();

        // identity type mappers
        for (final IdentityMetadata identity : metadata.values()) {
            final Mapper mapper = identity.getMapperType().newInstance();
            mappers.register(identity.getDtoType(), identity.getIdentityType(), mapper);
        }

        CoreMappings.configure(mappers);

        return mappers;
    }
}
