package com.trainlab.util;

import org.springframework.stereotype.Component;

@Component
public class IPUtil {
    public final static String IPv4_PATTERN = "\\[((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.)){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)]";
}
