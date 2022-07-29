package com.mytrial.ha.hot;

import org.springframework.beans.factory.annotation.Value;

public class MagicMath {
    public int add(final int a, final int b) {
        return a+b;
    }

    public int getVersion() {
        return 2; // Update this by a class patch and test
    }
}
