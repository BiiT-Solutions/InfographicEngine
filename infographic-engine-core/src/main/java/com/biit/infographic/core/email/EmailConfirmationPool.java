package com.biit.infographic.core.email;

import com.biit.utils.pool.BasePool;
import org.springframework.stereotype.Component;

@Component
public class EmailConfirmationPool extends BasePool<String, String> {
    private static final Long WARNING_POOL_TIME = (long) (5 * 60 * 1000);

    @Override
    public long getExpirationTime() {
        return WARNING_POOL_TIME;
    }

    @Override
    public boolean isDirty(String element) {
        return false;
    }
}
