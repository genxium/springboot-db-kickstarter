package com.mytrial.ha.hot;

import org.springframework.stereotype.Service;

@Service
public class MagicMath {
    public int add(final int a, final int b) {
        return a+b;
    }

    public int getVersion() {
        return 1; // Update this by a class patch and test
    }
}
