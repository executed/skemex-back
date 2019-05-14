package com.devserbyn.skemex.utility;

import javax.persistence.NoResultException;
import java.util.Optional;
import java.util.function.Supplier;

public class DaoUtility {

    private DaoUtility() {}

    public static <T> Optional<T> findOrEmpty(final Supplier<T> sup) {
        try {
            return Optional.ofNullable(sup.get());
        }catch (NoResultException ex) {
            //log
        }
        return Optional.empty();
    }
}
