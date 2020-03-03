/*
 * Copyright (c) 2014, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.graalvm.compiler.lir;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Maps variables to a generic type.
 *
 * TODO (je) evaluate data structure
 */
public class VariableMap<T> {

    private final ArrayList<T> content;

    public VariableMap() {
        content = new ArrayList<>();
    }

    public T get(Variable key) {
        if (key == null || key.index >= content.size()) {
            return null;
        }
        return content.get(key.index);
    }

    public T put(Variable key, T value) {
        assert key != null : "Key cannot be null";
        assert value != null : "Value cannot be null";
        while (key.index >= content.size()) {
            content.add(null);
        }
        return content.set(key.index, value);
    }

    public T remove(Variable key) {
        assert key != null : "Key cannot be null";
        if (key.index >= content.size()) {
            return null;
        }
        return content.set(key.index, null);
    }

    public void forEach(Consumer<T> action) {
        for (T e : content) {
            if (e != null) {
                action.accept(e);
            }
        }
    }

    /**
     * Keeps only keys which match the given predicate.
     */
    public void filter(Predicate<T> predicate) {
        for (int i = 0; i < content.size(); i++) {
            T e = content.get(i);
            if (e != null && !predicate.test(e)) {
                content.set(i, null);
            }
        }
    }

    public boolean contains(Predicate<T> predicate) {
        for (int i = 0; i < content.size(); i++) {
            T e = content.get(i);
            if (e != null && predicate.test(e)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        if (content.isEmpty()) {
            return true;
        }

        for (T e : content) {
            if (e != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (content.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (T e : content) {
            if (e != null) {
                sb.append(e.toString());
                sb.append(",");
            }
        }

        if (sb.indexOf(",") != -1) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();
    }
}
