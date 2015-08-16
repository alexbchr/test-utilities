 /*
 * Copyright 2003-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexbchr.testutilities.testng.launch.components;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;

/**
 * @author Andrew Eisenberg
 * @created Oct 30, 2009
 *
 */
public class NoTests implements ITestContent {

    public String getAnnotationType() {
        return "";
    }

    public Collection getGroups() {
        return Collections.EMPTY_LIST;
    }

    public Set getTestMethods() {
        return Collections.EMPTY_SET;
    }

    public boolean hasTestMethods() {
        return false;
    }

    public boolean isTestMethod(IMethod imethod) {
        return false;
    }

    public boolean isTestNGClass() {
        return false;
    }

}