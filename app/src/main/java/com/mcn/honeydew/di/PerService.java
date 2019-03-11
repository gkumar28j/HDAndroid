package com.mcn.honeydew.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by amit on 16/3/18.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerService {
}
