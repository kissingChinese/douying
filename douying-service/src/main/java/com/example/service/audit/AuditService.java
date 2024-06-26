package com.example.service.audit;

import java.util.function.Supplier;

/**
 *  用于处理审核
 */
public interface AuditService<T,R> {

    /**
     *  审核规范
     * @param task
     * @return
     */
    R audit(T task);
}
