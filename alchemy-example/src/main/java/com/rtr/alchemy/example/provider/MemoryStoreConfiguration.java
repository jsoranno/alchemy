package com.rtr.alchemy.example.provider;

import com.rtr.alchemy.db.ExperimentsStoreProvider;
import com.rtr.alchemy.db.memory.MemoryStoreProvider;
import com.rtr.alchemy.service.config.StoreProviderConfiguration;

public class MemoryStoreConfiguration extends StoreProviderConfiguration {
    @Override
    public ExperimentsStoreProvider createProvider() {
        return new MemoryStoreProvider();
    }
}