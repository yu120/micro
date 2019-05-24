package org.micro.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AntPathMatcherTest {

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Test
    public void test() {
        // test exact matching
        Assertions.assertTrue(pathMatcher.match("test", "test"));
        Assertions.assertTrue(pathMatcher.match("/test", "/test"));
        // SPR-14141
        Assertions.assertTrue(pathMatcher.match("http://example.org", "http://example.org"));
        Assertions.assertFalse(pathMatcher.match("/test.jpg", "test.jpg"));
        Assertions.assertFalse(pathMatcher.match("test", "/test"));
        Assertions.assertFalse(pathMatcher.match("/test", "test"));

        // test matching with ?'s
        Assertions.assertTrue(pathMatcher.match("t?st", "test"));
        Assertions.assertTrue(pathMatcher.match("??st", "test"));
        Assertions.assertTrue(pathMatcher.match("tes?", "test"));
        Assertions.assertTrue(pathMatcher.match("te??", "test"));
        Assertions.assertTrue(pathMatcher.match("?es?", "test"));
        Assertions.assertFalse(pathMatcher.match("tes?", "tes"));
        Assertions.assertFalse(pathMatcher.match("tes?", "testt"));
        Assertions.assertFalse(pathMatcher.match("tes?", "tsst"));

        // test matching with *'s
        Assertions.assertTrue(pathMatcher.match("*", "test"));
        Assertions.assertTrue(pathMatcher.match("test*", "test"));
        Assertions.assertTrue(pathMatcher.match("test*", "testTest"));
        Assertions.assertTrue(pathMatcher.match("test/*", "test/Test"));
        Assertions.assertTrue(pathMatcher.match("test/*", "test/t"));
        Assertions.assertTrue(pathMatcher.match("test/*", "test/"));
        Assertions.assertTrue(pathMatcher.match("*test*", "AnothertestTest"));
        Assertions.assertTrue(pathMatcher.match("*test", "Anothertest"));
        Assertions.assertTrue(pathMatcher.match("*.*", "test."));
        Assertions.assertTrue(pathMatcher.match("*.*", "test.test"));
        Assertions.assertTrue(pathMatcher.match("*.*", "test.test.test"));
        Assertions.assertTrue(pathMatcher.match("test*aaa", "testblaaaa"));
        Assertions.assertFalse(pathMatcher.match("test*", "tst"));
        Assertions.assertFalse(pathMatcher.match("test*", "tsttest"));
        Assertions.assertFalse(pathMatcher.match("test*", "test/"));
        Assertions.assertFalse(pathMatcher.match("test*", "test/t"));
        Assertions.assertFalse(pathMatcher.match("test/*", "test"));
        Assertions.assertFalse(pathMatcher.match("*test*", "tsttst"));
        Assertions.assertFalse(pathMatcher.match("*test", "tsttst"));
        Assertions.assertFalse(pathMatcher.match("*.*", "tsttst"));
        Assertions.assertFalse(pathMatcher.match("test*aaa", "test"));
        Assertions.assertFalse(pathMatcher.match("test*aaa", "testblaaab"));

        // test matching with ?'s and /'s
        Assertions.assertTrue(pathMatcher.match("/?", "/a"));
        Assertions.assertTrue(pathMatcher.match("/?/a", "/a/a"));
        Assertions.assertTrue(pathMatcher.match("/a/?", "/a/b"));
        Assertions.assertTrue(pathMatcher.match("/??/a", "/aa/a"));
        Assertions.assertTrue(pathMatcher.match("/a/??", "/a/bb"));
        Assertions.assertTrue(pathMatcher.match("/?", "/a"));

        // test matching with **'s
        Assertions.assertTrue(pathMatcher.match("/**", "/testing/testing"));
        Assertions.assertTrue(pathMatcher.match("/*/**", "/testing/testing"));
        Assertions.assertTrue(pathMatcher.match("/**/*", "/testing/testing"));
        Assertions.assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla"));
        Assertions.assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla"));
        Assertions.assertTrue(pathMatcher.match("/**/test", "/bla/bla/test"));
        Assertions.assertTrue(pathMatcher.match("/bla/**/**/bla", "/bla/bla/bla/bla/bla/bla"));
        Assertions.assertTrue(pathMatcher.match("/bla*bla/test", "/blaXXXbla/test"));
        Assertions.assertTrue(pathMatcher.match("/*bla/test", "/XXXbla/test"));
        Assertions.assertFalse(pathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));
        Assertions.assertFalse(pathMatcher.match("/*bla/test", "XXXblab/test"));
        Assertions.assertFalse(pathMatcher.match("/*bla/test", "XXXbl/test"));

        Assertions.assertFalse(pathMatcher.match("/????", "/bala/bla"));
        Assertions.assertFalse(pathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

        Assertions.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
        Assertions.assertTrue(pathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
        Assertions.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
        Assertions.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));

        Assertions.assertTrue(pathMatcher.match("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing/"));
        Assertions.assertTrue(pathMatcher.match("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing"));
        Assertions.assertTrue(pathMatcher.match("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing"));
        Assertions.assertFalse(pathMatcher.match("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing/testing"));

        Assertions.assertFalse(pathMatcher.match("/x/x/**/bla", "/x/x/x/"));
        Assertions.assertTrue(pathMatcher.match("/foo/bar/**", "/foo/bar"));
        Assertions.assertTrue(pathMatcher.match("", ""));
        Assertions.assertTrue(pathMatcher.match("/{bla}.*", "/testing.html"));
    }

}
