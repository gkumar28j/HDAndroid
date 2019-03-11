package com.mcn.honeydew.utils.rx;

import io.reactivex.Scheduler;

/**
 * Created by amit on 1/1/18.
 */

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler computation();

    Scheduler io();

}
