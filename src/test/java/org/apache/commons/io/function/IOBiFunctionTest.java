/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.function.Function;

import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link IOBiFunction}.
 */
public class IOBiFunctionTest {

    @SuppressWarnings("unused")
    private boolean not(final boolean value) throws IOException {
        return !value;
    }

    /**
     * Tests {@link IOBiFunction#andThen(Function)}.
     *
     * @throws IOException thrown on test failure
     */
    @Test
    public void testAndThenFunction() throws IOException {
        final IOBiFunction<Path, LinkOption[], Boolean> isDirectory = Files::isDirectory;
        final Function<Boolean, Boolean> not = b -> !b;
        assertEquals(true, isDirectory.apply(PathUtils.current(), PathUtils.EMPTY_LINK_OPTION_ARRAY));
        final IOBiFunction<Path, LinkOption[], Boolean> andThen = isDirectory.andThen(not);
        assertEquals(false, andThen.apply(PathUtils.current(), PathUtils.EMPTY_LINK_OPTION_ARRAY));
    }

    /**
     * Tests {@link IOBiFunction#andThen(IOFunction)}.
     *
     * @throws IOException thrown on test failure
     */
    @Test
    public void testAndThenIOFunction() throws IOException {
        final IOBiFunction<Path, LinkOption[], Boolean> isDirectory = Files::isDirectory;
        final IOFunction<Boolean, Boolean> not = this::not;
        assertEquals(true, isDirectory.apply(PathUtils.current(), PathUtils.EMPTY_LINK_OPTION_ARRAY));
        final IOBiFunction<Path, LinkOption[], Boolean> andThen = isDirectory.andThen(not);
        assertEquals(false, andThen.apply(PathUtils.current(), PathUtils.EMPTY_LINK_OPTION_ARRAY));
    }

    /**
     * Tests {@link IOBiFunction#apply(Object, Object)}.
     *
     * @throws IOException thrown on test failure
     */
    @Test
    public void testApply() throws IOException {
        final IOBiFunction<Path, LinkOption[], Boolean> isDirectory = Files::isDirectory;
        assertEquals(true, isDirectory.apply(PathUtils.current(), PathUtils.EMPTY_LINK_OPTION_ARRAY));
    }

    /**
     * Tests {@link IOBiFunction#apply(Object, Object)}.
     */
    @Test
    public void testApplyThrowsException() {
        final IOBiFunction<Path, LinkOption[], Boolean> isDirectory = (t, u) -> {
            throw new IOException("Boom!");
        };
        assertThrows(IOException.class, () -> isDirectory.apply(PathUtils.current(), PathUtils.EMPTY_LINK_OPTION_ARRAY));
    }

    @Test
    public void testNoopIOConsumer() throws IOException {
        assertNull(IOBiFunction.noop().apply(null, null));
        assertNull(IOBiFunction.NOOP.apply(null, null));
    }

}
