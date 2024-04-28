package mk.ukim.finki.nbafantasy.web.controllers;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Pointcuts for aspects.
 */
public class Pointcuts {

    /**
     * Pointcut for all admin controller methods.
     */
    @Pointcut("execution(* *..web.controllers.AdminController.*(..))")
    public static void adminControllerMethods() {
    }
}
