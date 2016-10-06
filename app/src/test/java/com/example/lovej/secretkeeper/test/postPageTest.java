package com.example.lovej.secretkeeper.test;
import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by SpencerLee on 18/09/2016.
 */
public class postPageTest extends TestSuite{
    public static Test suite(){
        return new TestSuiteBuilder(postPageTest.class)
                .includeAllPackagesUnderHere().build();
    }
    public postPageTest(){
        super();
    }

}
