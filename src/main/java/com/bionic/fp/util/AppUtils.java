package com.bionic.fp.util;

import com.bionic.fp.domain.IdEntity;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Contains common methods of the application
 *
 * @author Sergiy Gabriel
 */
public class AppUtils {

    private AppUtils() {
    }

    public static <T extends IdEntity<PK>, PK extends Serializable> List<PK> convert(final List<T> entities) {
        return entities.stream().parallel().map(IdEntity::getId).collect(toList());
    }
}
